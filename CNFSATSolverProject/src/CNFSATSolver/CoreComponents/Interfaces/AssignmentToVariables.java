package CNFSATSolver.CoreComponents.Interfaces;

public interface AssignmentToVariables {

    public long getTimeToSolve();

    public boolean getSolveable();

    public VariableI[] getVariables();

    public void setSolveable(boolean value, long timeToSolve);

    public boolean getTimedOut();

}
