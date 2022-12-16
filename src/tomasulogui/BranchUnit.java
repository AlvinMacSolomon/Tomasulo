package tomasulogui;

public class BranchUnit
        extends FunctionalUnit {

    public static final int EXEC_CYCLES = 1;

    public BranchUnit(PipelineSimulator sim) {
        super(sim);
    }

    public int calculateResult(int station) {
        ReservationStation s = stations[station];
        simulator.reorder.getEntryByTag(s.destTag)
                .setBranchTaken(switch (s.getFunction()) {
                    case BEQ  -> s.getData1() == s.getData2();
                    case BNE  -> s.getData1() != s.getData2();
                    case BLEZ -> s.getData1() <= 0;
                    case BLTZ -> s.getData1() <  0;
                    case BGEZ -> s.getData1() >= 0;
                    case BGTZ -> s.getData1() >  0;
                    default -> true;
                });
        return 0;
    }

    public int getExecCycles() {
        return EXEC_CYCLES;
    }
}
