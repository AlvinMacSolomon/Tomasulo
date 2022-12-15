package tomasulogui;

import tomasulogui.IssuedInst.INST_TYPE;

// ISSUE. XEUTECE. COMPLETE.

public class ReorderBuffer {
  public static final int size = 30;
  int frontQ = 0;
  int rearQ = 0;
  ROBEntry[] buff = new ROBEntry[size];
  int numRetirees = 0;

  PipelineSimulator simulator;
  RegisterFile regs;
  boolean halted = false;

  public ReorderBuffer(PipelineSimulator sim, RegisterFile registers) {
    simulator = sim;
    regs = registers;
  }

  public ROBEntry getEntryByTag(int tag) {
    return buff[tag];
  }

  public int getInstPC(int tag) {
    return buff[tag].getInstPC();
  }

  public boolean isHalted() {
    return halted;
  }

  public boolean isFull() {
    return (frontQ == rearQ && buff[frontQ] != null);
  }

  public int getNumRetirees() {
    return numRetirees;
  }

  public boolean retireInst() {
    // 3 cases
    // 1. regular reg dest inst
    // 2. isBranch w/ mispredict
    // 3. isStore
    ROBEntry retiree = buff[frontQ];
    
    if (retiree == null) {
      return false;
    }
    
    if (retiree.isHaltOpcode()) {
      halted = true;
      return true;
    }

    boolean shouldAdvance = true;

    readCDB(simulator.cdb);

    // this line should go somewhere down there
    if (retiree.getOpcode() == INST_TYPE.JAL || retiree.getOpcode() == INST_TYPE.JALR) {
        regs.setReg(31, retiree.instPC + 4);
        simulator.issue.jalhappened = false;
    }
    if (retiree.isBranch() || retiree.getOpcode() == INST_TYPE.NOP) {
        if (retiree.branchMispredicted()) {
            shouldAdvance = false;      
            simulator.setPC(retiree.getPredictTaken() ? retiree.getInstPC() + 4 : retiree.getbTgtAddr());
            java.util.Arrays.fill(buff, null);
            frontQ = rearQ = 0;
            simulator.squashAllInsts();
        } 
    } else if (!retiree.complete) shouldAdvance = false;
      else if (retiree.isStore()) {
          simulator.getMemory().setIntDataAtAddr(retiree.getStoreAddr(), retiree.getStoreData());
    } else if (!retiree.isBranch() && frontQ == regs.robSlot[retiree.getWriteReg()]) { // if this tag is assigned to the destination register, write to it and clear the robslot
          regs.regs[retiree.getWriteReg()] = retiree.getWriteValue();
          regs.robSlot[retiree.getWriteReg()] = -1;
    }

      // if mispredict branch, won't do normal advance
      if (shouldAdvance) {
        numRetirees++;
        buff[frontQ] = null;
        frontQ = (frontQ + 1) % size;
      }

    return false;
  }

  public void readCDB(CDB cdb) {
    // check entire ROB for someone waiting on this data
    int t = cdb.dataTag;
    if (t != -1 && cdb.getDataValid()) {
      buff[t].writeValue = cdb.dataValue;
      buff[t].complete = true;
      for (ROBEntry e : buff) {
        if (e != null && e.isStore()) {
            if (t == e.storeAddrTag) {
                e.storeAddr = cdb.getDataValue();
                e.storeAddrValid = true;
            } else if (t == e.storeDataTag) {
                e.storeData = cdb.getDataValue();
                e.storeDataValid = true;
            }
            if (e.storeAddrValid && e.storeDataValid) e.complete = true;
        }
      }
    }


    // could be destination reg
    // could be store address source.. wer're gonna pretend this line doesn't exist for now

    // TODO body of method
  }

  public void updateInstForIssue(IssuedInst inst) {
    // the task is to simply annotate the register fields
    // the dest reg will be assigned a tag, which is just our slot# a.k.a. robslot
    // all src regs will either be assigned a tag, read from reg, or forwarded from ROB

    // TODONE - possibly nothing if you use my model
    // I use the call to copyInstData below to do 2 things:
    // 1. update the Issued Inst
    // 2. fill in the ROB entry

    // first get a ROB slot
    if (buff[rearQ] != null) {
      throw new MIPSException("updateInstForIssue: no ROB slot avail");
    }
    ROBEntry newEntry = new ROBEntry(this);
    buff[rearQ] = newEntry;
    newEntry.copyInstData(inst, rearQ);

    rearQ = (rearQ + 1) % size;
  }

  public int getTagForReg(int regNum) {
    return (regs.getSlotForReg(regNum));
  }

  public int getDataForReg(int regNum) {
    return (regs.getReg(regNum));
  }

  public void setTagForReg(int regNum, int tag) {
    regs.setSlotForReg(regNum, tag);
  }

}
