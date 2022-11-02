package CNFSATSolver.CDCLSolver.SolverComponents.RestartPolicy;

import CNFSATSolver.CDCLSolver.CoreClasses.VariablesAssignments;
import CNFSATSolver.CDCLSolver.SolverComponents.VariableBrancher.VariablePicker;

public class NoRestarts implements RestartPolicy {

    public NoRestarts() {
    }

    @Override
    public boolean restart(int numConflicts, VariablePicker picker, VariablesAssignments assignments) {
        return false;
    }
}
