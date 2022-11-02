package CNFSATSolver.CDCLSolver.SolverComponents.RestartPolicy;

import CNFSATSolver.CDCLSolver.CoreClasses.VariablesAssignments;
import CNFSATSolver.CDCLSolver.SolverComponents.VariableBrancher.VariablePicker;

public class GeometricRestartPolicy implements RestartPolicy {

    private int initialNumConflictsRestart;
    private double geometricCommonRatio;
    private double restartThreshold;

    public GeometricRestartPolicy(int initialNumConflictsRestart, double geometricCommonRatio) {
        this.initialNumConflictsRestart = initialNumConflictsRestart;
        this.geometricCommonRatio = geometricCommonRatio;
        this.restartThreshold = initialNumConflictsRestart;
    }

    @Override
    public boolean restart(int numConflicts, VariablePicker picker, VariablesAssignments assignments) {
        if (numConflicts >= restartThreshold) {
            restartThreshold *= geometricCommonRatio;
            assignments.removeAllAboveDecisionLevels(0, picker);
            return true;
        }
        return false;
    }
}
