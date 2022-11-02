package CNFSATSolver.CDCLSolver.CoreClasses.Interfaces;

import CNFSATSolver.CoreComponents.Interfaces.*;

public interface Variable extends VariableI {

    public int getVariable();

    public SingleVariableAssignment getSingleVariableAssignment();

    public boolean equals(Object obj);

    public int hashCode();

    public String toString();

}
