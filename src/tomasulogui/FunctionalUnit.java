package tomasulogui;

public abstract class FunctionalUnit {
  PipelineSimulator simulator;
  ReservationStation[] stations = new ReservationStation[2];
  
  public FunctionalUnit(PipelineSimulator sim) {
    simulator = sim;
    
  }

 
  public void squashAll() {
    // todo fill in
  }

  public abstract int calculateResult(int station);

  public abstract int getExecCycles();

  public void execCycle(CDB cdb) {
    //todo - start executing, ask for CDB, etc.
    int count = 0;
    int n = next();
    if (n == -1) return; // there's nothing in the stations
    cdb.dataValue = calculateResult(n);
    cdb.dataTag = stations[n].getDestTag();
    cdb.setDataValid(true);
  }



  public void acceptIssue(IssuedInst inst) {
    int rs = stations[0] == null ? 0 : 1; // determine which rs the instruction goes to
    stations[rs] = new ReservationStation(simulator);
    stations[rs].loadInst(inst);

  // todo - fill in reservation station (if available) with data from inst
  }

  public boolean rsAvail() {
      return stations[0] == null || stations[1] == null;
  }

  public int next() {
      if (stations[0] == null && stations[1] == null) return -1;
      if (stations[0] != null) return 0;
      if (stations[1] != null) return 1;

      return 0;
  }

}
