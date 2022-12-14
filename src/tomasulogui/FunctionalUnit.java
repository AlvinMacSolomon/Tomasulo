package tomasulogui;

public abstract class FunctionalUnit {
    PipelineSimulator simulator;
    ReservationStation[] stations = new ReservationStation[2];

    int count = 0;

    boolean requestWriteback = false;
    boolean canWriteback = false;
    int writebackEntry = -1;
    int writeTag = -1;
    int writeData = -1;

    public FunctionalUnit(PipelineSimulator sim) {
        simulator = sim;
    }

    public void squashAll() {
        count = 0;
        requestWriteback = canWriteback = false;
        writebackEntry = writeTag = writeData = -1;
        stations = new ReservationStation[2];
    }

    public abstract int calculateResult(int station);

    public abstract int getExecCycles();

    public void execCycle(CDB cdb) {
        if (stations[0] != null) stations[0].snoopy(cdb);
        if (stations[1] != null) stations[1].snoopy(cdb);
        count++;
        int n = next();
        if (n == -1) return; // there's nothing in the stations
        ReservationStation s = stations[n];
        if (s.start == -1) s.start = count;
        if (count >= s.start + getExecCycles()) {
            if (canWriteback) {
                stations[n] = null;
                requestWriteback = false;
                canWriteback = false;
            } else if (s.data1Valid && s.data2Valid) {
                requestWriteback = true;
                writeData = calculateResult(n);
                writeTag = s.getDestTag();
            }
        }
    }

    public void acceptIssue(IssuedInst inst) {
        int rs = stations[0] == null ? 0 : 1; // determine which rs the instruction goes to
        stations[rs] = new ReservationStation(simulator);
        stations[rs].loadInst(inst);
    }

    public boolean rsAvail() {
        return stations[0] == null || stations[1] == null;
    }

    public int next() {
        if (stations[0] == null && stations[1] == null) return -1;
        if (stations[1] == null)                        return 0;
        if (stations[0] == null)                        return 1;
        if (stations[0].start < stations[1].start)      return 0;
        else                                            return 1;
    }

    public boolean isRequestingWriteback() {
        return requestWriteback;
    }

    public void setCanWriteback() {
        canWriteback = true;
    }

    public int getWriteTag() {
        return writeTag;
    }

    public int getWriteData() {
        return writeData;
    }

}
