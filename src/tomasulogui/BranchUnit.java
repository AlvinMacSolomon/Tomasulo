package tomasulogui;

public class BranchUnit
        extends FunctionalUnit {

    public static final int EXEC_CYCLES = 1;

    public BranchUnit(PipelineSimulator sim) {
        super(sim);
    }

    public int calculateResult(int station) {
        // cool
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
        // if (take != s.predictedTaken) 
        // if (take) return s.address;
        return 0;
    }
    // jumps just sort of happen
    // we need to put r31 in the destination spot of the rob
    // on jal we need to mark the address as valid
    // we need to make sure it doesn't issue while squashing
    // we need tell the rob if it mispredicted through back channel, put a public function in the rob enty that we call from the destgtag in the res station to access the rob and call the entry's funtion to flag as mispredicated
    // 

    public int getExecCycles() {
        return EXEC_CYCLES;
    }

    public void squashAll() {
        stations[0] = stations[1] = null;
    }
}
