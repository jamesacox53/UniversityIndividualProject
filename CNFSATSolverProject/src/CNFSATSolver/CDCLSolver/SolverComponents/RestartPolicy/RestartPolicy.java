package CNFSATSolver.CDCLSolver.SolverComponents.RestartPolicy;

import CNFSATSolver.CDCLSolver.CoreClasses.VariablesAssignments;
import CNFSATSolver.CDCLSolver.SolverComponents.VariableBrancher.VariablePicker;

public interface RestartPolicy {
    public boolean restart(int numConflicts, VariablePicker picker, VariablesAssignments assignments);
}
