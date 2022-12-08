package tomasulogui;

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

  public void snoop(CDB cdb) {
    // TODO - add code to snoop on CDB each cycle
  }

  public boolean isReady() {
    return data1Valid && data2Valid;
  }

  public void loadInst(IssuedInst inst) {
    // insert inst into reservation station
    boolean i = inst.getImmediate() != -1; //cool
    data1 = inst.regSrc1;
    data2 = i ? inst.immediate : inst.regSrc2;
    tag1 = inst.regSrc1Tag;
    tag2 = inst.regSrc2Tag;
    data1Valid = inst.regSrc1Valid;
    data2Valid = i ? true : inst.regSrc2Valid;
    destTag = inst.regDestTag;
    if (inst.isBranch()) {
      predictedTaken = inst.getBranchPrediction();
      address = inst.getBranchTgt();
      // addressTag = inst.get
    }
  }
}
