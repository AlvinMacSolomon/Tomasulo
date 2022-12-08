package tomasulogui;

public class IntAlu extends FunctionalUnit{
  public static final int EXEC_CYCLES = 1;

  public IntAlu(PipelineSimulator sim) {
    super(sim);
  }


  public int calculateResult(int station) {
    ReservationStation s = stations[station];
    switch (s.getFunction()) {
      case ADD, ADDI: return s.data1 + s.data2;
      case SUB:       return s.data1 - s.data2;
      case AND, ANDI: return s.data1 & s.data2;
      case OR, ORI:   return s.data1 | s.data2;
      case XOR, XORI: return s.data1 ^ s.data2;
      case SLL: return s.data1 << s.data2;
      case SRL: return s.data1 >> s.data2;
      default: return s.data1 >>> s.data2;
    }
  }

  public int getExecCycles() {
    return EXEC_CYCLES;
  }

  public void squashAll() {
    stations[0] = stations[1] = null;
  }
}
