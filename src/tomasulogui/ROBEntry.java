package tomasulogui;

public class ROBEntry {
  ReorderBuffer rob;

  // TODO - add many more fields into entry
  // I deleted most, and only kept those necessary to compile GUI
  boolean complete = false;
  boolean predictTaken = false;
  boolean mispredicted = false;
  int instPC = -1;
  int writeReg = -1;
  int writeValue = -1;

  IssuedInst.INST_TYPE opcode;

  // our vars
  int storeAddr;
  int storeAddrTag;
  boolean storeAddrValid;

  int storeData;
  int storeDataTag;
  boolean storeDataValid;

  boolean busy; // gets set by the FU?
  boolean branch;
  int bTgtAddr;

  boolean writeValid;

  public ROBEntry(ReorderBuffer buffer) {
    rob = buffer;
  }

  public boolean isComplete() {
    return complete;
  }

  public boolean branchMispredicted() {
    return mispredicted;
  }

  public boolean getPredictTaken() {
    return predictTaken;
  }

  public int getInstPC() {
    return instPC;
  }

  public IssuedInst.INST_TYPE getOpcode () {
    return opcode;
  }


  public boolean isHaltOpcode() {
    return (opcode == IssuedInst.INST_TYPE.HALT);
  }

  public void setBranchTaken(boolean result) {
  // TODO - maybe more than simple set
  }

  public int getWriteReg() {
    return writeReg;
  }

  public int getWriteValue() {
    return writeValue;
  }

  public void setWriteValue(int value) {
    writeValue = value;
  }

  public void copyInstData(IssuedInst inst, int frontQ) {
    instPC = inst.getPC();
    inst.setRegDestTag(frontQ);

    rob.simulator.btb.predictBranch(inst);

    // update the instruction
      // for the source regs
      // 1. it's either in the register file
      // 2. it could be in the rob but complete 
      // 3. it could also be in the rob but not complete
    
    // update the field
    writeReg = inst.getRegDest();
    branch = inst.determineIfBranch();
    opcode = inst.getOpcode();
    branch = inst.determineIfBranch();
    bTgtAddr = inst.getBranchTgt();
    predictTaken = inst.getBranchPrediction();


    if (isStore() ) {
      // storeAddr = inst.
    }

    // operand 1 (copy this for operand 2?)
    if (rob.simulator.regs.robSlot[inst.getRegSrc1()] == -1) {
      writeValue = rob.simulator.regs.getReg(inst.getRegSrc1());
      writeValid = true;
    } else {
      // if rob has valid data at tag, get data from reorder buffer
      // else send the tag
    }


    // TODO - This is a long and complicated method, probably the most complex
    // of the project.  It does 2 things:
    // 1. update the instruction, as shown in 2nd line of code above
    // 2. update the fields of the ROBEntry, as shown in the 1st line of code above

  }

  public boolean isStore() {
      return opcode == IssuedInst.INST_TYPE.STORE;
  }

  public boolean isBusy() {
    return busy;
  }

  public void setBusy(boolean b) {
      busy = b;
  }
    
  public boolean isBranch() {
    return branch;
  }

  public int getStoreAddr() {
    return storeAddr;
  }

  public boolean getStoreAddrValid() {
    return storeAddrValid;
  }

  public int getStoreData() {
    return storeData;
  }
  
  public boolean getStoreDataValid() {
    return storeDataValid;
  }

  public int getbTgtAddr() {
    return bTgtAddr;
  }

}
