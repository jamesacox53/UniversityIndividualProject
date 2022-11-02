package CNFSATSolver.CoreComponents.Interfaces;

public interface SatProblem {

    public int getNumProblemClauses();

    public ClauseI getClauseNum(int index);

    public boolean getSolveable();

}
