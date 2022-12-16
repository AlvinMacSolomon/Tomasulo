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

    public IssuedInst.INST_TYPE getOpcode() {
        return opcode;
    }

    public boolean isHaltOpcode() {
        return (opcode == IssuedInst.INST_TYPE.HALT);
    }

    public void setBranchTaken(boolean result) {
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

        if (inst.regSrc1Used) {
            int tag = rob.getTagForReg(inst.regSrc1);
            if (tag == -1)
                inst.setRegSrc1Value(rob.regs.getReg(inst.regSrc1));
            else if (rob.getEntryByTag(tag).complete)
                inst.setRegSrc1Value(rob.getEntryByTag(tag).getWriteValue());
            else inst.setRegSrc1Tag(tag);
        }
        if (inst.regSrc2Used) {
            int tag = rob.getTagForReg(inst.regSrc2);
            if (tag == -1)
                inst.setRegSrc2Value(rob.regs.getReg(inst.regSrc2));
            else if (rob.getEntryByTag(tag).complete)
                inst.setRegSrc2Value(rob.getEntryByTag(tag).getWriteValue());
            else inst.setRegSrc2Tag(tag);
        }

        if (inst.getOpcode() == INST_TYPE.JAL || inst.getOpcode() == INST_TYPE.JALR) {
            inst.regDest = 31;
            rob.simulator.issue.jalhappened = true;
        }
        if (inst.regDestUsed) rob.setTagForReg(inst.getRegDest(), frontQ);

        rob.simulator.btb.predictBranch(inst);

        writeReg = inst.getRegDest();
        opcode = inst.getOpcode();
        branch = inst.determineIfBranch();
        bTgtAddr = inst.getBranchTgt();
        predictTaken = inst.getBranchPrediction();
        complete = opcode == INST_TYPE.NOP || opcode == INST_TYPE.J || opcode == INST_TYPE.JAL
                || opcode == INST_TYPE.JR  || opcode == INST_TYPE.JALR;

        if (opcode == INST_TYPE.J   || opcode == INST_TYPE.JR ||
            opcode == INST_TYPE.JAL || opcode == INST_TYPE.JALR)
            mispredicted = false;

        if (isStore()) {
            storeAddr = inst.getRegSrc1Value() + inst.getImmediate();
            storeAddrTag = inst.getRegSrc1Tag();
            storeAddrValid = inst.getRegSrc1Valid();
            storeData = writeValue = inst.getRegSrc2Value();
            storeDataTag = inst.getRegSrc2Tag();
            storeDataValid = inst.getRegSrc2Valid();
        }
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

    public void setStrAddr(int a) {
        storeAddr = a;
        storeAddrValid = true;
    }

    public void setStrData(int d) {
        storeData = d;
        storeDataValid = true;
    }

    public int getbTgtAddr() {
        return bTgtAddr;
    }

}
