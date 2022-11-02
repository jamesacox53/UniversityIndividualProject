package CNFSATSolver.CDCLSolver.SolverComponents.RestartPolicy;

import CNFSATSolver.CDCLSolver.CoreClasses.VariablesAssignments;
import CNFSATSolver.CDCLSolver.SolverComponents.VariableBrancher.VariablePicker;

import java.util.ArrayList;

public class LubyRestartPolicy implements RestartPolicy {

    private int lubyUnitRun;
    private ArrayList<Integer> runLength = new ArrayList<>();
    private ArrayList<Integer> powersOf2 = new ArrayList<>();
    private int lubyi = 1;
    private int restartThreshold;


    public LubyRestartPolicy(int lubyUnitRun) {
        this.lubyUnitRun = lubyUnitRun;
        powersOf2.add(1);
        powersOf2.add(2);
        powersOf2.add(4);
        runLength.add(0);
        runLength.add(1);
        restartThreshold = lubyUnitRun * runLength.get(1);
    }


    @Override
    public boolean restart(int numConflicts, VariablePicker picker, VariablesAssignments assignments) {
        if (numConflicts >= restartThreshold) {
            lubyi++;
            int currPowerOf2 = powersOf2.get(powersOf2.size() - 1);
            int pow2LessThanOrEqual = powersOf2.get(powersOf2.size() - 2);
            int run = -1;
            if (lubyi == (currPowerOf2 - 1)) {
                powersOf2.add(2 * currPowerOf2);
                run = pow2LessThanOrEqual;
                runLength.add(run);
            } else {
                int prevRunPos = lubyi - pow2LessThanOrEqual + 1;
                run = runLength.get(prevRunPos);
                runLength.add(run);
            }

            restartThreshold += (lubyUnitRun * run);

            assignments.removeAllAboveDecisionLevels(0, picker);
            return true;
        }
        return false;
    }
}
