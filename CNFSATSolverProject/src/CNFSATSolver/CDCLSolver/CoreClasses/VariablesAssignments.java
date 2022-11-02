package CNFSATSolver.CDCLSolver.CoreClasses;

import CNFSATSolver.CDCLSolver.SolverComponents.VariableBrancher.VariablePicker;

import CNFSATSolver.CDCLSolver.CoreClasses.Interfaces.*;
import CNFSATSolver.CoreComponents.Interfaces.AssignmentToVariables;
import CNFSATSolver.CoreComponents.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class VariablesAssignments implements AssignmentToVariables {

    private Variable[] variables;
    private Literal[] positiveLiterals;
    private Literal[] negatedLiterals;
    private final Map<Integer, Pair<SingleVariableAssignment, List<SingleVariableAssignment>>> assignmentsAtDecisionLevel = new HashMap<>();
    private int numVariablesAssigned = 0;
    private int maxDecisionLevel = 0;
    private final int numVariables;
    private boolean solveable = false;
    private boolean timedOut = false;
    private long timeToSolve = 0;

    public VariablesAssignments(Variable[] variables, Literal[] positiveLiterals, Literal[] negatedLiterals, int numVariables) {

        this.numVariables = numVariables;
        this.variables = variables;
        this.positiveLiterals = positiveLiterals;
        this.negatedLiterals = negatedLiterals;
    }

    public int getNumVariables() {

        return numVariables;
    }

    public Variable[] getVariables() {
        return variables;
    }

    public void createDecisionLevel(int decisionLevel) {
        if (!assignmentsAtDecisionLevel.containsKey(decisionLevel)) {
            assignmentsAtDecisionLevel.put(decisionLevel, new Pair<SingleVariableAssignment, List<SingleVariableAssignment>>(null, new ArrayList<>()));
            if (decisionLevel > maxDecisionLevel) {
                maxDecisionLevel = decisionLevel;
            }
        }
    }

    public void addDecisionAtDecisionLevel(int decisionLevel, SingleVariableAssignment singleVariableAssignment) {
        if (assignmentsAtDecisionLevel.containsKey(decisionLevel)) {
            Pair<SingleVariableAssignment, List<SingleVariableAssignment>> assignments = assignmentsAtDecisionLevel.get(decisionLevel);
            assignments.left = singleVariableAssignment;
        }
    }

    public void addNonDecisionAtDecisionLevel(int decisionLevel, SingleVariableAssignment singleVariableAssignment) {
        if (assignmentsAtDecisionLevel.containsKey(decisionLevel)) {
            Pair<SingleVariableAssignment, List<SingleVariableAssignment>> assignments = assignmentsAtDecisionLevel.get(decisionLevel);
            assignments.right.add(singleVariableAssignment);
        }
    }

    public void removeAllAboveDecisionLevels(int currDecisionLevel, VariablePicker picker) {

        for (int level = currDecisionLevel + 1; level <= maxDecisionLevel; level++) {
            if (assignmentsAtDecisionLevel.containsKey(level)) {
                Pair<SingleVariableAssignment, List<SingleVariableAssignment>> assignments = assignmentsAtDecisionLevel.get(level);
                assignments.left.reset(this);

                picker.addVariableLiterals(assignments.left.getVariable());

                List<SingleVariableAssignment> assignmentsRight = assignments.right;
                for (SingleVariableAssignment singleVariableAssignment : assignmentsRight) {
                    singleVariableAssignment.reset(this);

                    picker.addVariableLiterals(singleVariableAssignment.getVariable());
                }

                assignmentsAtDecisionLevel.remove(level);
            }
        }

        if (currDecisionLevel < maxDecisionLevel) {
            maxDecisionLevel = currDecisionLevel;
        }
    }

    public SingleVariableAssignment getChosenAssignmentAtDecisionLevel(int decisionLevel) {
        if (assignmentsAtDecisionLevel.containsKey(decisionLevel)) {
            return assignmentsAtDecisionLevel.get(decisionLevel).left;
        }
        return null;
    }

    public List<SingleVariableAssignment> getPropagatedAssignmentsAtDecisionLevel(int decisionLevel) {
        return assignmentsAtDecisionLevel.get(decisionLevel).right;
    }

    public int getMaxCurrDecisionLevel() {
        return maxDecisionLevel;
    }

    public void incrementNumVariablesAssigned() {
        numVariablesAssigned += 1;
    }

    public void setTimedOut(long timeToSolve) {
        timedOut = true;
        this.timeToSolve = timeToSolve;
    }

    public boolean getTimedOut() {
        return timedOut;
    }

    public void decrementNumVariablesAssigned() {
        numVariablesAssigned -= 1;
    }

    public boolean allVariablesAssigned() {
        return (numVariablesAssigned == numVariables);
    }

    public Literal[] getPositiveLiterals() {
        return positiveLiterals;
    }

    public Literal[] getNegatedLiterals() {
        return negatedLiterals;
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
