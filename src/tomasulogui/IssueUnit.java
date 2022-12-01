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

      int rs = -1;

      if (simulator.getROB().isFull()) {

      }

      issuee = IssuedInst.createIssuedInst(simulator.getMemory().getInstAtAddr(simulator.getPC()));
      // PERFECT
      switch (issuee.getOpcode()) {
          case NOP, HALT, STORE:
            t = EXEC_TYPE.NONE;
            break;
          case MUL:
            t = EXEC_TYPE.MULT;
            break;
          case DIV:
            t = EXEC_TYPE.DIV;
            break;
          case ADD, ADDI, SUB, AND, ANDI, OR, ORI, XOR, XORI, SLL, SRL, SRA:
            t = EXEC_TYPE.ALU;
            break;
          case LOAD:
            t = EXEC_TYPE.LOAD;
            break;
          case J, JAL, JR, JALR, BEQ, BNE, BLTZ, BLEZ, BGTZ, BGEZ:
            t = EXEC_TYPE.BRANCH;
            break;
      }
      // increment pc

      switch (t) {
        case LOAD:
          break;
        case ALU:
          rs = simulator.alu.nextAvailRS();
          if (rs != -1) {
            
          }
          break;
        case MULT:
          rs = simulator.multiplier.nextAvailRS();
          if (rs != -1) {
            
          }
          break;
        case BRANCH:
          // set the program counter if it is a branch
          break;
        default:
          
      }

      simulator.getROB().updateInstForIssue(issuee);

      // to issue, we make an IssuedInst, filling in what we know
      // We check the BTB, and put prediction if branch, updating PC
      //     if pred taken, incr PC otherwise
      // We then send this to the ROB, which fills in the data fields
      // We then check the CDB, and see if it is broadcasting data we need,
      //    so that we can forward during issue

      // We then send this to the FU, who stores in reservation station
    }

  }
