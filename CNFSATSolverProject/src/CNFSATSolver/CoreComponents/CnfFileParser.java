package CNFSATSolver.CoreComponents;

import CNFSATSolver.CoreComponents.Interfaces.SatProblem;
import CNFSATSolver.CoreComponents.Interfaces.CnfFileParserI;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public abstract class CnfFileParser<Variable, Literal, Clause, CnfFormula, VariablesAssignments>
                                            implements CnfFileParserI {

    public SatProblem parseFileToCreateFormula(String inputFilePath) {

        File file = new File(inputFilePath);
        boolean seenProblem = false;
        String type = "";
        int numVariables = -1;
        int numClauses = -1;
        CnfFormula cnfFormula = null;
        VariablesAssignments assignments = null;
        Variable[] variables = null;
        Literal[] positiveLiterals = null;
        Literal[] negatedLiterals = null;
        try {
            Scanner scanner = new Scanner(file);

            while (!seenProblem && scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.isEmpty()) {
                    continue;
                }

                String[] tokens = line.trim().split(" ");

                if (("c").equals(tokens[0])) {
                    continue;
                }

                if (("p").equals(tokens[0])) {
                    seenProblem = true;

                    for (int i = 1; i < tokens.length; i++) {
                        String token = tokens[i];
                        if (!(token).isEmpty()) {
                            if (type.equals("")) {
                                type = token;
                            } else if (numVariables == -1) {
                                numVariables = Integer.parseInt(token);
                            } else if (numClauses == -1) {
                                numClauses = Integer.parseInt(token);
                            }
                        }
                    }

                    cnfFormula = createCnfFormula(numVariables, numClauses);
                    variables = createVariables(numVariables);
                    positiveLiterals = createPositiveLiterals(variables);
                    negatedLiterals = createNegatedLiterals(variables);
                    assignments = createVariablesAssignments(variables, positiveLiterals, negatedLiterals, numVariables);
                }
            }
            if (seenProblem) {
                Clause clause = createClause(numVariables);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();

                    if (line.isEmpty()) {
                        continue;
                    }

                    String[] tokens = line.trim().split(" ");

                    for (String token : tokens) {
                        if (token.isEmpty()) {
                            continue;
                        }

                        if (token.equals("0")) {

                            addProblemConstraintClause(cnfFormula, clause);
                            clause = createClause(numVariables);

                        } else {

                            int integer = Integer.parseInt(token);
                            int position = Math.abs(integer) - 1;

                            if (integer < 0) {
                                addLiteralToClause(clause, negatedLiterals[position]);
                            } else {
                                addLiteralToClause(clause, positiveLiterals[position]);
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return createSatProblem(cnfFormula, assignments);
    }

    public Variable[] createVariables(int numVariables) {
        Variable[] variables = createArrayVariables(numVariables);
        for (int i = 0; i < variables.length; i++) {
            variables[i] = createVariable(i + 1);
        }
        return variables;
    }

    public Literal[] createPositiveLiterals(Variable[] variables) {
        Literal[] positiveLiterals = createArrayLiterals(variables.length);
        for (int i = 0; i < variables.length; i++) {
            positiveLiterals[i] = createLiteral(variables[i], false);
        }
        return positiveLiterals;
    }

    public Literal[] createNegatedLiterals(Variable[] variables) {
        Literal[] negatedLiterals = createArrayLiterals(variables.length);
        for (int i = 0; i < variables.length; i++) {
            negatedLiterals[i] = createLiteral(variables[i], true);
        }
        return negatedLiterals;
    }

    protected abstract VariablesAssignments createVariablesAssignments(Variable[] variables, Literal[] positiveLiterals,
                                                                   Literal[] negatedLiterals, int numVariables);

    protected abstract CnfFormula createCnfFormula(int numVariables, int numClauses);

    protected abstract Clause createClause(int numVariables);

    protected abstract Variable createVariable(int variableNum);

    protected abstract Literal createLiteral(Variable variable, boolean isNegated);

    protected abstract Variable[] createArrayVariables(int numVariables);

    protected abstract Literal[] createArrayLiterals(int numVariables);

    protected abstract void addProblemConstraintClause(CnfFormula cnfFormula, Clause clause);

    protected abstract void addLiteralToClause(Clause clause, Literal literal);

    protected abstract SatProblem createSatProblem(CnfFormula cnfFormula, VariablesAssignments assignments);
}
