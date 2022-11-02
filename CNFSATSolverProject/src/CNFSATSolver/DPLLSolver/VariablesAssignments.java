package CNFSATSolver.DPLLSolver;

import CNFSATSolver.CoreComponents.Interfaces.AssignmentToVariables;
import CNFSATSolver.CoreComponents.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class VariablesAssignments implements AssignmentToVariables {

    private Variable[] variables;
    private final Map<Integer, Pair<SingleVariableAssignment, List<SingleVariableAssignment>>> assignmentsAtDecisionLevel = new HashMap<>();
    private int maxDecisionLevel = 0;
    private final int numVariables;
    private boolean solveable = false;
    private boolean timedOut = false;
    private long timeToSolve = 0;

    public VariablesAssignments(Variable[] variables, int numVariables) {

        this.numVariables = numVariables;
        this.variables = variables;
    }

    public int getNumVariables() {

        return numVariables;
    }

    public Variable[] getVariables() {
        return variables;
    }

    public void createdDecisionLevel(int decisionLevel) {
        if (!assignmentsAtDecisionLevel.containsKey(decisionLevel)) {
            assignmentsAtDecisionLevel.put(decisionLevel, new Pair<SingleVariableAssignment, List<SingleVariableAssignment>>(null, new ArrayList<>()));
            maxDecisionLevel = decisionLevel;
        }
    }

    public void addDecisionAtDecisionLevel(int decisionLevel, SingleVariableAssignment singleVariableAssignment) {
        if (assignmentsAtDecisionLevel.containsKey(decisionLevel)) {
            Pair<SingleVariableAssignment, List<SingleVariableAssignment>> assignments = assignmentsAtDecisionLevel.get(decisionLevel);
            assignments.left = singleVariableAssignment;
        }
        maxDecisionLevel = decisionLevel;
    }

    public void addNonDecisionAtDecisionLevel(int decisionLevel, SingleVariableAssignment singleVariableAssignment) {
        if (assignmentsAtDecisionLevel.containsKey(decisionLevel)) {
            Pair<SingleVariableAssignment, List<SingleVariableAssignment>> assignments = assignmentsAtDecisionLevel.get(decisionLevel);
            assignments.right.add(singleVariableAssignment);
        }
        maxDecisionLevel = decisionLevel;
    }

    public void removeDecisionLevel(int decisionLevel, int currDecisionLevel) {
        if (assignmentsAtDecisionLevel.containsKey(decisionLevel)) {
            Pair<SingleVariableAssignment, List<SingleVariableAssignment>> assignments = assignmentsAtDecisionLevel.get(decisionLevel);
            if (assignments.left != null) {
                assignments.left.reset();
            }
            List<SingleVariableAssignment> assignmentsRight = assignments.right;
            if (!assignmentsRight.isEmpty()) {
                for (SingleVariableAssignment singleVariableAssignment : assignmentsRight) {
                    singleVariableAssignment.reset();
                }
            }

            assignmentsAtDecisionLevel.remove(decisionLevel);
        }
        maxDecisionLevel = currDecisionLevel;
    }

    public SingleVariableAssignment getChosenAssignmentAtDecisionLevel(int decisionLevel) {
        if (assignmentsAtDecisionLevel.containsKey(decisionLevel)) {
            return assignmentsAtDecisionLevel.get(decisionLevel).left;
        }
        return null;
    }

    public List<SingleVariableAssignment> getAssignmentsAtDecisionLevel(int decisionLevel) {
        return assignmentsAtDecisionLevel.get(decisionLevel).right;
    }

    public int getMaxCurrDecisionLevel() {
        return maxDecisionLevel;
    }

    public SingleVariableAssignment getNextUnassignedVariableAssignment() {
        for (Variable variable : variables) {
            SingleVariableAssignment assignment = variable.getSingleVariableAssignment();
            if (!assignment.isAssigned()) {
                return assignment;
            }
        }
        return null;
    }

    public void setTimedOut(long timeToSolve) {
        timedOut = true;
        this.timeToSolve = timeToSolve;
    }

    public boolean getTimedOut() {
        return timedOut;
    }

    public void setSolveable(boolean value, long timeToSolve) {

        solveable = value;
        this.timeToSolve = timeToSolve;
    }

    public long getTimeToSolve() {
        return timeToSolve;
    }

    public boolean getSolveable() {
        return solveable;
    }

    public void printDecisionLevelAssignments() {
        for (int decisionLevel : assignmentsAtDecisionLevel.keySet()) {
            Pair<SingleVariableAssignment, List<SingleVariableAssignment>> assignments = assignmentsAtDecisionLevel.get(decisionLevel);
            String leftString = "empty Left";
            String rightString = "empty Right";
            if (assignments.left != null) {
                leftString = assignments.left.toString();
            }
            if (!assignments.right.isEmpty()) {
                rightString = assignments.right.toString();
            }

            System.out.println("d:" + decisionLevel + " " + leftString + ", " + rightString);
        }

    }
}
