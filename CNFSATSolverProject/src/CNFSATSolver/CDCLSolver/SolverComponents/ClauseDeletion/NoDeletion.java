package CNFSATSolver.CDCLSolver.SolverComponents.ClauseDeletion;

import CNFSATSolver.CDCLSolver.CoreClasses.Interfaces.*;

public class NoDeletion implements ClauseDeletion {
    @Override
    public void decayClauseActivities() {
    }

    @Override
    public void bumpClauseActivity(Clause clause) {
    }

    @Override
    public void deleteClauses() {
    }
}
