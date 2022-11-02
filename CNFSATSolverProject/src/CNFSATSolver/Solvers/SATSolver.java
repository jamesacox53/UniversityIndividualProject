package CNFSATSolver.Solvers;

import CNFSATSolver.CoreComponents.Interfaces.*;

public interface SATSolver {

    public AssignmentToVariables solve(SatProblem satProblem);

    public void setFiveMinTimeOut();

    public void setOverallTimeOutTime(long overallTime);

}
