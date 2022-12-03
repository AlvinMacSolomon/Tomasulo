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
  }



  public boolean acceptIssue(IssuedInst inst) {
    if (stations[0] != null && stations[1] != null) return false;
    int rs = stations[0] == null ? 0 : 1; // determine which rs the instruction goes to
    stations[rs].loadInst(inst);





    return true;

  // todo - fill in reservation station (if available) with data from inst
  }

  public int nextAvailRS() {
      if (stations[0] == null) return 0;
      if (stations[1] == null) return 1;
      return -1; // return -1 if there are no available
  }

}
