package tomasulogui;

import tomasulogui.IssuedInst.INST_TYPE;

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

  boolean branch;
  int bTgtAddr;

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
    mispredicted = predictTaken != result;
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
    
    // update the instruction
    // for the source regs
    // 1. it's either in the register file
    // 2. it could be in the rob but complete
    // 3. it could also be in the rob but not complete bc fu is still working (in which case only send tag)
    
    if (inst.regSrc1Used) {
      int tag = rob.getTagForReg(inst.regSrc1);
      if (tag == -1){
        inst.setRegSrc1Value(rob.regs.getReg(inst.regSrc1));
        inst.setRegSrc1Valid();
      } else {
        if (rob.getEntryByTag(tag).complete) {
          inst.setRegSrc1Value(rob.getEntryByTag(tag).getWriteValue()); 
          inst.setRegSrc1Valid();
        } else inst.setRegSrc1Tag(tag); // send the tag
      }
    }
    if (inst.regSrc2Used) {
        int tag = rob.getTagForReg(inst.regSrc2);
        if (tag == -1) {
          inst.setRegSrc2Value(rob.regs.getReg(inst.regSrc2));
          inst.setRegSrc2Valid();
        } else {
          if (rob.getEntryByTag(tag).complete) {
            inst.setRegSrc2Value(rob.getEntryByTag(tag).getWriteValue());
            inst.setRegSrc2Valid();
          } else inst.setRegSrc2Tag(tag); // send the tag
        }
      }
      
      if (inst.getOpcode() == INST_TYPE.JAL || inst.getOpcode() == INST_TYPE.JALR) {
        inst.regDest = 31;
        rob.simulator.issue.jalhappened = true;
      }
      if (inst.regDestUsed) rob.setTagForReg(inst.getRegDest(), frontQ);
      
      // if (!rob.simulator.isHalted)
          rob.simulator.btb.predictBranch(inst);
      
      // update the field
      writeReg = inst.getRegDest();
      branch = inst.determineIfBranch();
      opcode = inst.getOpcode();
      branch = inst.determineIfBranch();
      bTgtAddr = inst.getBranchTgt();
      predictTaken = inst.getBranchPrediction();
      complete = opcode == INST_TYPE.NOP || opcode == INST_TYPE.J || opcode == INST_TYPE.JAL
      || opcode == INST_TYPE.JR  || opcode == INST_TYPE.JALR;
      
      if (opcode == INST_TYPE.J   || opcode == INST_TYPE.JR ||
      opcode == INST_TYPE.JAL || opcode == INST_TYPE.JALR) {
        mispredicted = false; 
      }
      
      
      if (isStore()) {
        storeAddr = inst.getRegSrc1Value() + inst.getImmediate();
        storeAddrTag = inst.getRegSrc1Tag();
        storeAddrValid = inst.getRegSrc1Valid();
        storeData = writeValue = inst.getRegSrc2Value();
        storeDataTag = inst.getRegSrc2Tag();
        storeDataValid = inst.getRegSrc2Valid();
      }
      
      
      // TODO - This is a long and complicated method, probably the most complex
    // of the project.  It does 2 things:
    // 1. update the instruction, as shown in 2nd line of code above
    // 2. update the fields of the ROBEntry, as shown in the 1st line of code above
    
  }
  
  public boolean isStore() {
    return opcode == IssuedInst.INST_TYPE.STORE;
  }
  
  public void setBusy(boolean c) {
    complete = c;
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
