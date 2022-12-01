package tomasulogui;

public class BranchUnit
        extends FunctionalUnit {

    public static final int EXEC_CYCLES = 1;

    public BranchUnit(PipelineSimulator sim) {
        super(sim);
    }

    public int calculateResult(int station) {
        // todo fill in
        ReservationStation s = stations[station];
        switch (s.getFunction()) {
            case BEQ:
                // s.
            case BNE:
            case BLTZ:
            case BLEZ:
            case BGEZ:
            case BGTZ:
                break;
            default:
                break;
        }
        return 0;
    }

    public int getExecCycles() {
        return EXEC_CYCLES;
    }

    public void squashAll() {
        stations[0] = stations[1] = null;
    }
}
