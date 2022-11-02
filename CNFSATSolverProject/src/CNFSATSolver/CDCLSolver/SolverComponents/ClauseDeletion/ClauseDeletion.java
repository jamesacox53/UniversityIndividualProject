package CNFSATSolver.CDCLSolver.SolverComponents.ClauseDeletion;

import CNFSATSolver.CDCLSolver.CoreClasses.Interfaces.*;

public interface ClauseDeletion {

    public void decayClauseActivities();

    public void bumpClauseActivity(Clause clause);

    public void deleteClauses();

}
