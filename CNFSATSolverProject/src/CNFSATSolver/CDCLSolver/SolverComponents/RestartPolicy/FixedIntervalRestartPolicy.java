package CNFSATSolver.CDCLSolver.SolverComponents.RestartPolicy;

import CNFSATSolver.CDCLSolver.CoreClasses.VariablesAssignments;
import CNFSATSolver.CDCLSolver.SolverComponents.VariableBrancher.VariablePicker;

public class FixedIntervalRestartPolicy implements RestartPolicy {

    private int restartThreshold;
    private int restartIncrement;

    public FixedIntervalRestartPolicy(int initialNumConflictsRestart, int restartIncrement) {
        this.restartThreshold = initialNumConflictsRestart;
        this.restartIncrement = restartIncrement;
    }

    @Override
    public boolean restart(int numConflicts, VariablePicker picker, VariablesAssignments assignments) {
        if (numConflicts >= restartThreshold) {
            restartThreshold += restartIncrement;
            assignments.removeAllAboveDecisionLevels(0, picker);
            return true;
        }
        return false;
    }
}
