package CNFRandomBalancedInstanceGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

public class CNFProblemGenerator {

    public static void generateCNFProblems(String[] args) {

        if (args.length < 4) {
            System.out.println("Not enough arguments");
            return;
        }

        int numVariables = Integer.parseInt(args[0]);
        int numClauses = Integer.parseInt(args[1]);
        int numFilesGenerated = Integer.parseInt(args[2]);
        String outputDestination = args[3];

        for (int numGenerated = 1; numGenerated <= numFilesGenerated; numGenerated++) {

            String fileNameHeader = "cnf-balanced-" + numVariables + "-" + numClauses;

            int numExtension = 0;

            String fileName = fileNameHeader + "-" + numExtension + ".cnf";
            File file = new File(outputDestination, fileName);


            while (file.exists()) {
                numExtension++;
                fileName = fileNameHeader + "-" + numExtension + ".cnf";
                file = new File(outputDestination, fileName);
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

                writer.write("c FileName: " + fileName);
                writer.newLine();
                writer.write("c Randomly Generated Balanced Instance created by James Cox K1764041");
                writer.newLine();
                writer.write("c This file is generated randomly with numVariables: " + numVariables + " numClauses: " + numClauses);
                writer.newLine();

                writer.write("p cnf " + numVariables + " " + numClauses);

                Random random = new Random();

                int temp = ((3 * numClauses) / (5 * numVariables));

                int randomNumberBound = Math.max(3, temp);
                IntStream randomGeneratorClauseLength = random.ints(1, randomNumberBound + 1);
                PrimitiveIterator.OfInt randomGeneratorIteratorClauseLength = randomGeneratorClauseLength.iterator();

                int maxNumVeryShortClauses = Math.max(1, numClauses / 200);
                int numVeryShortClauses = 0;

                for (int clause = 0; clause < numClauses; clause++) {
                    writer.newLine();

                    StringBuilder clauseBuilder = new StringBuilder();

                    int clauseLength = randomGeneratorIteratorClauseLength.next();
                    ArrayList<Integer> variablesInClause = new ArrayList<>();

                    IntStream randomGeneratorVariableNumber = random.ints(1, numVariables + 1).distinct();
                    PrimitiveIterator.OfInt randomGeneratorIteratorVariableNumber = randomGeneratorVariableNumber.iterator();


                    while (clauseLength <= 2 && (numVeryShortClauses >= maxNumVeryShortClauses)) {
                        clauseLength = randomGeneratorIteratorClauseLength.next();
                    }

                    if (clauseLength <= 2) {
                        numVeryShortClauses++;
                    }

                    for (int var = 0; var < clauseLength; var++) {

                        int variableNum = randomGeneratorIteratorVariableNumber.next();
                        variablesInClause.add(variableNum);
                    }

                    Collections.sort(variablesInClause);

                    for (Integer var : variablesInClause) {

                        double randomNumber = random.nextDouble();
                        if (randomNumber <= 0.5) {
                            // positive
                            clauseBuilderPositiveVariable(clauseBuilder, var);
                        } else {
                            // negative
                            clauseBuilderNegativeVariable(clauseBuilder, var);
                        }
                    }
                    clauseBuilder.append("0");
                    writer.write(clauseBuilder.toString());
                }
            } catch (IOException e) {
                System.out.println("Unable to write to file");
                e.printStackTrace();
            }
        }
    }

    private static void clauseBuilderNegativeVariable(StringBuilder clauseBuilder, int variable) {
        clauseBuilder.append((-(variable)) + " ");
    }

    private static void clauseBuilderPositiveVariable(StringBuilder clauseBuilder, int variable) {
        clauseBuilder.append((variable) + " ");
    }


    public static void main(String[] args) {

        if (args.length <= 0) {
            System.out.println("No arguments");
        } else {
            generateCNFProblems(args);
        }
    }
}