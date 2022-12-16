package tomasulogui;

public class IntAlu extends FunctionalUnit {
    public static final int EXEC_CYCLES = 1;

    public IntAlu(PipelineSimulator sim) {
        super(sim);
    }

    public int calculateResult(int station) {
        ReservationStation s = stations[station];
        return switch (s.getFunction()) {
            case ADD, ADDI -> s.data1 + s.data2;
            case SUB       -> s.data1 - s.data2;
            case AND, ANDI -> s.data1 & s.data2;
            case OR, ORI   -> s.data1 | s.data2;
            case XOR, XORI -> s.data1 ^ s.data2;
            case SLL       -> s.data1 << s.data2;
            case SRL       -> s.data1 >> s.data2;
            default        -> s.data1 >>> s.data2;
        };
    }

    public int getExecCycles() {
        return EXEC_CYCLES;
    }
}
