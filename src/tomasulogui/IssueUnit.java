package tomasulogui;

public class IssueUnit {
    private enum EXEC_TYPE {
        NONE, LOAD, ALU, MULT, DIV, BRANCH
    };

    PipelineSimulator simulator;
    IssuedInst issuee;
    Object fu;

    EXEC_TYPE t;

    boolean jalhappened = false;
    boolean isHalt = false;

    public IssueUnit(PipelineSimulator sim) {
        simulator = sim;
    }

    public void execCycle() {
        if (simulator.getROB().isFull() || jalhappened || simulator.isHalted) return;

        issuee = IssuedInst.createIssuedInst(simulator.getMemory().getInstAtAddr(simulator.getPC()));
        issuee.setPC(simulator.getPC());

        switch (issuee.getOpcode()) {
            case ADD, ADDI, SUB, AND, ANDI, OR, ORI, XOR, XORI, SLL, SRL, SRA:
                if (simulator.alu.rsAvail()) { // no room in the inn :(
                    simulator.getROB().updateInstForIssue(issuee);
                    if (simulator.cdb.getDataValid() && simulator.cdb.getDataTag() == issuee.getRegSrc1Tag())
                        issuee.setRegSrc1Value(simulator.cdb.getDataValue());
                    if (simulator.cdb.getDataValid() && simulator.cdb.getDataTag() == issuee.getRegSrc2Tag())
                        issuee.setRegSrc2Value(simulator.cdb.getDataValue());
                    simulator.alu.acceptIssue(issuee);
                }
                break;
            case MUL:
                if (simulator.multiplier.rsAvail()) {
                    simulator.getROB().updateInstForIssue(issuee);
                    if (simulator.cdb.getDataValid() && simulator.cdb.getDataTag() == issuee.getRegSrc1Tag())
                        issuee.setRegSrc1Value(simulator.cdb.getDataValue());
                    if (simulator.cdb.getDataValid() && simulator.cdb.getDataTag() == issuee.getRegSrc2Tag())
                        issuee.setRegSrc2Value(simulator.cdb.getDataValue());
                    simulator.multiplier.acceptIssue(issuee);
                }
                break;
            case LOAD:
                if (simulator.loader.isReservationStationAvail()) {
                    simulator.getROB().updateInstForIssue(issuee);// maybe check for cdb too
                    if (simulator.cdb.getDataValid() && simulator.cdb.getDataTag() == issuee.getRegSrc1Tag())
                        issuee.setRegSrc1Value(simulator.cdb.getDataValue());
                    if (simulator.cdb.getDataValid() && simulator.cdb.getDataTag() == issuee.getRegSrc2Tag())
                        issuee.setRegSrc2Value(simulator.cdb.getDataValue());
                    simulator.loader.acceptIssue(issuee);
                }
                break;
            case BEQ, BNE, BLTZ, BLEZ, BGTZ, BGEZ:
                if (simulator.branchUnit.rsAvail()) {
                    simulator.getROB().updateInstForIssue(issuee);
                    if (simulator.cdb.getDataValid() && simulator.cdb.getDataTag() == issuee.getRegSrc1Tag())
                        issuee.setRegSrc1Value(simulator.cdb.getDataValue());
                    if (simulator.cdb.getDataValid() && simulator.cdb.getDataTag() == issuee.getRegSrc2Tag())
                        issuee.setRegSrc2Value(simulator.cdb.getDataValue());
                    simulator.branchUnit.acceptIssue(issuee);
                }
                break;
            case JR, JALR:
                if (simulator.regs.getSlotForReg(issuee.getRegSrc1()) != -1)
                    break;
            default:
                simulator.getROB().updateInstForIssue(issuee);
                if (simulator.cdb.getDataValid() && simulator.cdb.getDataTag() == issuee.getRegSrc1Tag())
                    issuee.setRegSrc1Value(simulator.cdb.getDataValue());
                if (simulator.cdb.getDataValid() && simulator.cdb.getDataTag() == issuee.getRegSrc2Tag())
                    issuee.setRegSrc2Value(simulator.cdb.getDataValue());

        }

        // to issue, we make an IssuedInst, filling in what we know
        // We check the BTB, and put prediction if branch, updating PC
        // if pred taken, incr PC otherwise
        // We then send this to the ROB, which fills in the data fields
        // We then check the CDB, and see if it is broadcasting data we need,
        // so that we can forward during issue

        // We then send this to the FU, who stores in reservation station
    }

}
