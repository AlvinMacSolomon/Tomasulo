package tomasulogui;

public class IssueUnit {
  private enum EXEC_TYPE {
    NONE, LOAD, ALU, MULT, DIV, BRANCH} ;

    PipelineSimulator simulator;
    IssuedInst issuee;
    Object fu;

    EXEC_TYPE t;

    public IssueUnit(PipelineSimulator sim) {
      simulator = sim;
    }

    public void execCycle() {
      // an execution cycle involves:
      // 1. checking if ROB and Reservation Station available
      // 2. issuing to reservation station, if no structural hazard

      boolean stall = false;

      if (simulator.getROB().isFull()) {
          // stall
      }

      issuee = IssuedInst.createIssuedInst(simulator.getMemory().getInstAtAddr(simulator.getPC()));
      issuee.setPC(simulator.getPC());
      // nearly PERFECT
      switch (issuee.getOpcode()) {
        case ADD, ADDI, SUB, AND, ANDI, OR, ORI, XOR, XORI, SLL, SRL, SRA:
            if (!simulator.alu.acceptIssue(issuee)) {
              stall = false;
            } 
            break;
        case MUL:
            break;
        case DIV:
            break;
        case LOAD:
            break;
        case J, JAL, JR, JALR, BEQ, BNE, BLTZ, BLEZ, BGTZ, BGEZ:
            break;
        default: //case NOP, HALT, STORE:
            break;
          }
          // increment pc
          
          
          
          if (!stall) { // we might need to increment in case things become bad
            simulator.getROB().updateInstForIssue(issuee);
      }

      // to issue, we make an IssuedInst, filling in what we know
      // We check the BTB, and put prediction if branch, updating PC
      //     if pred taken, incr PC otherwise
      // We then send this to the ROB, which fills in the data fields
      // We then check the CDB, and see if it is broadcasting data we need,
      //    so that we can forward during issue

      // We then send this to the FU, who stores in reservation station
    }

  }
