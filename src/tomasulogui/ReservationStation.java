package tomasulogui;

import tomasulogui.IssuedInst.INST_TYPE;

public class ReservationStation {
  PipelineSimulator simulator;

  int tag1;
  int tag2;
  int data1;
  int data2;
  boolean data1Valid = false;
  boolean data2Valid = false;
  // destTag doubles as branch tag
  int destTag;
  IssuedInst.INST_TYPE function = IssuedInst.INST_TYPE.NOP;

  // following just for branches
  int addressTag;
  boolean addressValid = false;
  int address;
  boolean predictedTaken = false;

  int start = -1;

  public ReservationStation(PipelineSimulator sim) {
    simulator = sim;
  }

  public int getDestTag() {
    return destTag;
  }

  public int getData1() {
    return data1;
  }

  public int getData2() {
    return data2;
  }

  public boolean isPredictedTaken() {
    return predictedTaken;
  }

  public IssuedInst.INST_TYPE getFunction() {
    return function;
  }

  public void snoopy(CDB cdb) { // for FUs to snoop on cdb
      if (!data1Valid && cdb.getDataValid() && cdb.getDataTag() == tag1) {
          data1 = cdb.getDataValue();
          data1Valid = true;
      } else if (!data2Valid && cdb.getDataValid() && cdb.getDataTag() == tag2) {
          data2 = cdb.getDataValue();
          data2Valid = true;
    }
  }

  public boolean isReady() {
    return data1Valid && data2Valid;
  }

  //NOT COOL.
  public void loadInst(IssuedInst inst) {
    // insert inst into reservation station
    function = inst.opcode;
    boolean i = inst.getImmediate() != -1; 
    data1 = inst.regSrc1Value; // val1
    data2 = i ? inst.immediate : inst.regSrc2Value;
    destTag = inst.regDestTag;
    tag1 = inst.regSrc1Tag;
    tag2 = inst.regSrc2Tag;
    data1Valid = inst.regSrc1Valid;
    data2Valid = i ? true : inst.regSrc2Valid;
    if (inst.isBranch()) {
      addressValid = function == INST_TYPE.J || function == INST_TYPE.JAL;
      predictedTaken = inst.getBranchPrediction();
      address = inst.getBranchTgt();
      // addressTag = ins
    }
  }
}
