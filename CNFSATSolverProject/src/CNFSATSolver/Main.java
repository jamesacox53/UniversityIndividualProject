package CNFSATSolver;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import CNFSATSolver.CDCLSolver.CoreClasses.*;
import CNFSATSolver.CDCLSolver.SolverComponents.ClauseDeletion.ActivityDeletion;
import CNFSATSolver.CDCLSolver.SolverComponents.ClauseDeletion.ClauseDeletion;
import CNFSATSolver.CDCLSolver.SolverComponents.ClauseDeletion.NoDeletion;
import CNFSATSolver.CDCLSolver.SolverComponents.RestartPolicy.*;
import CNFSATSolver.CDCLSolver.SolverComponents.VariableBrancher.AscendingOrder;
import CNFSATSolver.CDCLSolver.SolverComponents.VariableBrancher.VSIDSVariablePicker;
import CNFSATSolver.CDCLSolver.SolverComponents.VariableBrancher.VariablePicker;
import CNFSATSolver.CoreComponents.Interfaces.*;
import CNFSATSolver.Solvers.*;
import CNFSATSolver.DPLLSolver.DPLLCnfFileParser;
import CNFSATSolver.CDCLSolver.CoreClasses.Interfaces.*;
import CNFSATSolver.CDCLSolver.FileParsers.*;

import java.util.concurrent.TimeUnit;

/**
 *  The CNFSATSolver package is a package that is used to solve boolean satisfiability problems written in DIMACS .cnf files.
 *  This class Main is the class that is used to executed and run the SAT Solver program. the class Main is run by the java
 *  interpreter at the command line and uses the other classes in the CNFSATSolver package to offer different functionality
 *  to the user.
 *
 *  The first argument that is given to this program at the command line is the absolute destination path of the .cnf file
 *  or directory that the user wants to solve. If a directory is given the program will solve all .cnf files in the directory.
 *
 *  After the first command line argument the user can optionally give other command line arguments to change the algorithm
 *  and heuristics used to solve the sat problem / sat problems. If no optional command line arguments are used or only some
 *  are used the solver will use defaults. By default the solver will use CDCL with the heuristics: Two watched literals,
 *  VSIDS, GeometricRestartPolicy and ClauseDeletion. By default the solver will only print the result to the screen and has
 *  no time constraints.
 *
 *  The user can change the type of SAT Solver to a DPLL solver by using the "-DPLL" argument. The DPLL Solver doesn't have
 *  any heuristics so using any of the heuristic command line arguments will be ignored. The argument "-CDCL" uses a CDCL
 *  solver to solve the problems. A CDCL solver uses heuristics. The CDCL solver uses different heuristics while solving.
 *  There are different types of heuristics that a CDCL solver uses. A CDCL solver uses a DataStructure heuristic, a
 *  VariableBrancher heuristic, a RestartPolicy heuristic and a ClauseDeletion heuristic. The solver must use exactly one
 *  heuristic of each type. If one type doesn't have a heuristic specified for it as a command line argument a default will
 *  be used. If a type has more than one heuristic for it specified it uses the first specified from left to right.
 *
 *  The CDCL heuristics separated by types and their command line arguments(in parenthesis) are:
 *
 *  DataStructure heuristics: Two watched Literals ("-2WL"), Counters ("-Counters").
 *  VariableBrancher heuristics: VSIDS ("-VSIDS"), Ascending Order ("-Ascending").
 *  RestartPolicy heuristics: No Restart ("-NoRestart"), Luby Restart Policy ("-Luby"), Geometric Restart Policy ("-GeoRestart"),
 *  Fixed Interval Restart Policy ("-FixedRestart").
 *  ClauseDeletion heuristics: No Clause Deletion ("-NoDel"), Activity Based Clause Deletion ("-ActivDel").
 *
 *  If no solver type or heuristics are specified at the command line the defaults are used:
 *
 *  DataStructure heuristics: Two watched Literals ("-2WL"), VariableBrancher heuristic: VSIDS ("-VSIDS"), RestartPolicy heuristic:
 *  Geometric Restart Policy ("-GeoRestart"), ClauseDeletion heuristic: Activity Based Clause Deletion ("-ActivDel"). With
 *  only printing the result to the screen, no verification or saving the result to a txt file or a csv file. Also no time
 *  constraints.
 *
 *  The user can also specify command line arguments to allow saving of results to files, verification of solution, and
 *  to add timing constraints. The user can use command line arguments to save results to files in the directory of the
 *  file/ files being solved.  "-ResTxt" to save the results to a txt file called results.txt. "-ResCsv" to save results
 *  to a csv file called results.csv. "-VerFile" To save the verification of a solution to a sat problem to a file called
 *  verification.txt. "-VerScreen" to print the verification of a solution to a problem to screen. And "-PrintProb" to
 *  print the problem to screen. The user can also specify command line time constraints. "-5MinIndivTimeOut" specifies that
 *  each problem has a 5 minute time out. "-OverallSecTimeOut=1000" specifies that there is an overall timeout where solving
 *  of all files times out after some time in seconds. Replace 1000 with the amount of seconds time to solve all files.
 *
 *  The user should give each command line argument as a string in between quotation marks "" and arguments separated by a
 *  space. Having each argument as a string allows a single argument to contain spaces which is very useful when giving the
 *  path of the .cnf file or directory. When the user is solving a directory the directory shouldn't have a slash / or \
 *  as the end character as the program won't be able to detect the directory. Instead the path should just end with the
 *  name of the directory with no / or \. If this program is being run on linux the user shouldn't use ~/ in the path to
 *  mean the users home directory, the user has to write /home/ and complete the path from there as the program can't recognise
 *  paths that start with ~/.
 *
 *  @author James Cox k1764041 UG 1752267
 *  @version 1.0
 *  @since 2019-10-07
 *
 */
public class Main {

    // tunable parameters for VSIDS
    private double vsidsDecayFactor = 0.55;
    private double vsidsBumpActivity = 1.2;
    private int vsidsDecayFrequency = 35;

    // tunable parameters for Fixed Interval Restart Policy
    private int fixedIntervalRestartInitialNumConflictsRestart = 50;
    private int fixedIntervalRestartRestartIncrement = 70;

    // tunable parameters for Geometric Restart Policy
    private int geometricRestartInitialNumConflictsRestart = 40;
    private double geometricRestartGeometricCommonRatio = 2.1;

    // tunable parameter for Luby Restart Policy
    private int lubyUnitRun = 128;

    // tunable parameters for Activity Based Clause Deletion.
    private double activityDeletionClauseDecay = 0.95;
    private double activityDeletionInitialMaxNumLearntClausesFactor = 0.4;
    private double activityDeletionGeometricCommonRatio = 1.05;

    // These instance variables hold the information on what type of solver and heuristics the cnf problems
    // will be solved with. If none are specified at the command line they are given default values.
    private String type = null;
    private String dataStructure = null;
    private String variableBrancher = null;
    private String restartPolicy = null;
    private String clauseDeletion = null;

    // These instance variables are set to provide extra functionality.
    private boolean resultToCsvFile = false;
    private boolean resultToTxtFile = false;
    private boolean verificationToScreen = false;
    private boolean verificationToFile = false;
    private boolean printProblem = false;
    private long totatTimeSpentSolving = 0;

    private boolean fiveMinuteTimeOut = false;
    private boolean overallTimeOut = false;
    private long overallTimeOutTime = 0;
    private int lengthOfOverallTimeOutTimeInSeconds = 0;

    /**
     * The main method is the method called by the java interpreter to execute an application.
     * Here the arguments given at the command line are stored as an array of strings where the arguments are
     * split by the space character. That's why its important to give each argument as a string so then an argument
     * can contain spaces.
     * @param args The command line arguments that are passed to the program split on space characters.
     */
    public static void main(String[] args) {
        Main solver = new Main();
        // run is the method that is used to solve cnf files.
        solver.run(args);
    }

    /**
     * The constructor for the class Main.
     */
    public Main() {
    }

    /**
     *  run is the method that is used to solve cnf files. It sets up the chosen solving algorithm and all the heuristics and then
     *  solves the .cnf file / files. The solver can do a variety of optional things like: save the results of the sat solving
     *  to a csv or to a txt file. It can verify solutions it found and print the verification to screen or save the verification
     *  to file. The solver can also run with a 5 minute individual problem time out or with an overall time out which is specified
     *  at the command line/
     * @param args Command line arguments that are passed to the program split on space characters.
     */
    public void run(String[] args) {


        if (args.length == 0) {
            System.out.println("There were no arguments.");
            return;
        }

        // the path location to the single .cnf file or directory of .cnf files that the user wants solved. It is the
        // first argument.
        String location = args[0];

        File file = new File(location);

        if (!file.exists()) {
            System.out.println("This file/directory does not exist.");
            return;
        }

        // Here the solver goes through the command line arguments and sets the type of solver the user wants to use.
        // The setParameters method also sets the optional things like save result to file.
        setParameters(args);

        // The directory is found as if the user wants to save any results or verification to files the files are saved
        // in the directory of where the file / files being solved are stored.
        File directory = null;

        if (file.isFile()) {

            String fileNameTemp = file.getName();

            if (!fileNameTemp.endsWith(".cnf")) {
                System.out.println("This file isn't a cnf file.");
                return;
            }

            directory = file.getParentFile();

        } else if (file.isDirectory()) {
            directory = file;
        } else {
            System.out.println("This is not a file or directory");
            return;
        }

        Writer resTxtWriter = null;

        Writer resCsvWriter = null;

        Writer verWriter = null;

        if (resultToTxtFile) {

            // If the user wants the application to save results to a txt file a new file called results.txt is
            // created in the directory of the file/ files being solved. If a file called results.txt already exists
            // then the contents of that file is overwritten.
            File resultsTxtFile = new File(directory, "results.txt");

            try {
                resTxtWriter = new BufferedWriter(new FileWriter(resultsTxtFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (resultToCsvFile) {

            File resultsCsvFile = new File(directory, "results.csv");

            try {
                resCsvWriter = new BufferedWriter(new FileWriter(resultsCsvFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (verificationToFile) {

            File verificationFile = new File(directory, "verification.txt");

            try {
                verWriter = new BufferedWriter(new FileWriter(verificationFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // This prints / saves to file the solving algorithm and the heuristics being used. It also prints / saves to file
        // the values of tunable parameters for the heuristcs being used.
        printSolverUsing(resTxtWriter);

        // If an overall time limit has been set this is setup here.
        setUpOverallTimedOut();

        if (file.isFile()) {
            solveSingleFile(file, resTxtWriter, resCsvWriter , verWriter);

        } else if (file.isDirectory()) {
            solveDirectory(file, resTxtWriter, resCsvWriter , verWriter);
        }

        // prints / writes the total time solved solved for.
        printTotTimeSolved(resTxtWriter);

        // if the application is writing to a file it writes everything currently held in the buffer to the file.
        if (resTxtWriter != null) {
            try {
                resTxtWriter.flush();
                resTxtWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (resCsvWriter != null) {
            try {
                resCsvWriter.flush();
                resCsvWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (verWriter != null) {
            try {
                verWriter.flush();
                verWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The solveDirectory method is used to solve a directory of .cnf files.
     *
     * @param directory The file of the directory.
     * @param resTxtWriter The writer for the results to a txt file called results.txt.
     * @param resCsvWriter The writer for the results to a csv file called results.csv.
     * @param verWriter The writer for the verification to a txt file called verification.txt.
     */
    private void solveDirectory(File directory, Writer resTxtWriter, Writer resCsvWriter , Writer verWriter) {

        if (overallTimeOut) {
            solveDirectoryOverallTimed(directory, resTxtWriter, resCsvWriter, verWriter);
        } else {
            solveDirectoryNotOverallTimed(directory, resTxtWriter, resCsvWriter, verWriter);
        }
    }

    /**
     * The solveDirectoryOverallTimed method is a method used to solve a directory when there is an overall time limit.
     * @param directory The file of the directory.
     * @param resTxtWriter The writer for the results to a txt file called results.txt.
     * @param resCsvWriter The writer for the results to a csv file called results.csv.
     * @param verWriter The writer for the verification to a txt file called verification.txt.
     */
    private void solveDirectoryOverallTimed(File directory, Writer resTxtWriter, Writer resCsvWriter , Writer verWriter) {

        // the files in the directory that are .cnf files.
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".cnf"));
        
        boolean overallTimedOut = false;
        int numNotTimedOut = 0;

        for (int i = 0; !overallTimedOut &&  i < files.length; i++) {

            File fileInstance = files[i];
            // solveFile solves an individual file. It returns a boolean saying if the solver timed out solving the file.
            boolean timedOut = solveFile(i, fileInstance, resTxtWriter, resCsvWriter, verWriter);

            if (!timedOut) {
                numNotTimedOut++;
            }

            if (System.currentTimeMillis() >= overallTimeOutTime) {
                overallTimedOut = true;

            // If the solver is writing the results to a csv file it writes the results of a file to a single line and then the
            // solver needs to write a new line character so that the results of the next file can be written to a new line.
            } else if (resultToCsvFile && (i < files.length - 1)) {
                writeNewLineCsv(resCsvWriter);
            }
        }

        String temp = "Solved " + numNotTimedOut + " out of " + files.length +" .cnf files in " +
                lengthOfOverallTimeOutTimeInSeconds + " seconds.";

        System.out.println(temp);

        if (resultToTxtFile) {
            try {
                resTxtWriter.write(temp);
                resTxtWriter.write('\n');
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The solveDirectoryNotOverallTimed method is a method used to solve a directory when there is no overall time limit.
     * @param directory The file of the directory.
     * @param resTxtWriter The writer for the results to a txt file called results.txt.
     * @param resCsvWriter The writer for the results to a csv file called results.csv.
     * @param verWriter The writer for the verification to a txt file called verification.txt.
     */
    private void solveDirectoryNotOverallTimed(File directory, Writer resTxtWriter, Writer resCsvWriter , Writer verWriter) {

        File[] files = directory.listFiles((dir, name) -> name.endsWith(".cnf"));

        int numNotTimedOut = 0;

        for (int i = 0; i < files.length; i++) {

            File fileInstance = files[i];
            // solveFile solves an individual file. It returns a boolean saying if the solver timed out solving the file.
            boolean timedOut = solveFile(i, fileInstance, resTxtWriter, resCsvWriter, verWriter);

            if (!timedOut) {
                numNotTimedOut++;
            }

            // If the solver is writing the results to a csv file it writes the results of a file to a single line and then the
            // solver needs to write a new line character so that the results of the next file can be written to a new line.
            if (resultToCsvFile && (i < files.length - 1)) {
                writeNewLineCsv(resCsvWriter);
            }
        }

        String temp = "Solved " + numNotTimedOut + " out of " + files.length +" .cnf files.";

        System.out.println(temp);

        if (resultToTxtFile) {
            try {
                resTxtWriter.write(temp);
                resTxtWriter.write('\n');
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The solveFile method actually solves a single .cnf file. It can write the results to a .txt file, or a .csv file.
     * The solveFile method can also call a verifier to verify the solution to a problem. It can write this verification
     * to screen or to a .txt file. This method can also print the problem to screen.
     * @param instanceNum The instance number of the current file being solved by the solver this session. Each file increments the instanceNum.
     * @param fileInstance The actual file being solved.
     * @param resTxtWriter The writer for the results to a txt file called results.txt.
     * @param resCsvWriter The writer for the results to a csv file called results.csv.
     * @param verWriter The writer for the verification to a txt file called verification.txt.
     * @return a boolean. True if the SAT Solving algorithm timed out while solving the file, False otherwise.
     */
    private boolean solveFile(int instanceNum, File fileInstance, Writer resTxtWriter, Writer resCsvWriter, Writer verWriter) {

        String location = fileInstance.getPath();

        String fileName = fileInstance.getName();

        // Here the solver parses the .cnf file problem with the appropriate parser for the type of solving algorithm and
        // heuristics the user wants the solver to solve with.
        SatProblem satProblem = parseProblem(location);

        // Here the solver with the wanted solving algorithm and heuristics is created.
        SATSolver solver = createSolver(satProblem);

        // Here the problem is solved and an assignment to the variables is returned. If the file was found to be
        // satisfiable then a satisfying assignment is returned. If the file was found to unsatisfiable then getSolveable()
        // method returns false.
        AssignmentToVariables assignments = solver.solve(satProblem);

        // Here the results of solving a file are kept as a list of strings to be printed out or saved to a file.
        List<String> results = getResults(instanceNum, fileName, assignments);

        boolean timedOut = assignments.getTimedOut();

        printResultToScreen(results);

        if (resultToTxtFile) {
            writeResultToTxtFile(resTxtWriter, results);
        }

        if (resultToCsvFile) {
            writeResultToCsvFile(instanceNum, fileName, resCsvWriter, results);
        }

        if (printProblem) {
            printProblem(instanceNum, fileName, satProblem);
        }

        if (verificationToScreen || verificationToFile) {

            // get the verification of a problem saved as a list of strings.
            List<String> verification = getVerifier(instanceNum, fileName, satProblem, timedOut);

            if (verificationToScreen) {
                printVerificationToScreen(verification);
            }

            if (verificationToFile) {
                writeVerificationToTxtFile(verWriter, verification);
            }

        }
        return timedOut;

    }

    /**
     * The solveSingleFile method is called to solve a single .cnf file.
     * @param fileInstance The actual file being solved.
     * @param resTxtWriter The writer for the results to a txt file called results.txt.
     * @param resCsvWriter The writer for the results to a csv file called results.csv.
     * @param verWriter The writer for the verification to a txt file called verification.txt.
     */
    private void solveSingleFile(File fileInstance, Writer resTxtWriter, Writer resCsvWriter, Writer verWriter) {

        int numNotTimedOut = 0;
        boolean timedOut = solveFile(0, fileInstance, resTxtWriter, resCsvWriter, verWriter);

        if (!timedOut) {
            numNotTimedOut++;
        }

        String temp;
        if (overallTimeOut) {
            temp = "Solved " + numNotTimedOut + " out of " + 1 + " file in " +
                    lengthOfOverallTimeOutTimeInSeconds + " seconds.";
        } else {
            temp = "Solved " + numNotTimedOut + " out of " + 1 +" file.";
        }

        System.out.println(temp);

        if (resultToTxtFile) {
            try {
                resTxtWriter.write(temp);
                resTxtWriter.write('\n');
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The setUpOverallTimedOut method sets the end overall time limit for when the solver has to stop solving.
     */
    private void setUpOverallTimedOut() {
        if (overallTimeOut) {
            overallTimeOutTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(lengthOfOverallTimeOutTimeInSeconds);
        }
    }

    /**
     * The printSolverUsing method prints out / writes to file the type of solving algorithm being used along with the heuristics
     * being used.
     * @param writer The writer for the results .txt file or null if the user didn't specify the results be written to a .txt file.
     */
    private void printSolverUsing(Writer writer) {

        List<String> res = new ArrayList<>();
        res.add("Solver is solving using:");

        if (type.equals("DPLL")) {
            res.add("Type=DPLL.");

        } else if (type.equals("CDCL")) {
            StringBuilder builder = new StringBuilder();

            builder.append("Type=" + type + ", ");
            builder.append("DataStructure=" + dataStructure + ", ");
            builder.append("VariableBrancher=" + variableBrancher + ", ");
            builder.append("RestartPolicy=" + restartPolicy + ", ");
            builder.append("ClauseDeletion=" + clauseDeletion + ".");

            res.add(builder.toString());
        }

        // Here we add a text line about the tunable parameters for the heuristics we are using and their values.
        addTunableParameters(res);

        // Here we add a text line about the time constraints the solver is using.
        addTimeConstraints(res);

        for (String line : res) {
            System.out.println(line);
        }

        if (resultToTxtFile) {
            for (String line : res) {
                try {
                    writer.write(line);
                    writer.write('\n');
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * The printTotTimeSolved method prints / writes to results.txt file the total overall time the solver spent solving
     * in seconds.
     * @param writer the writer for the results.txt text file to be written to
     */
    private void printTotTimeSolved(Writer writer) {
        double timeInSecs = totatTimeSpentSolving * 0.001;
        String totTimeSolving = String.format("%.2f", timeInSecs);

        String res = "Total time solver solved for: " + totTimeSolving + " seconds.";
        System.out.println(res);

        if (resultToTxtFile) {
            try {
                writer.write(res);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The parseProblem method is used to parse the .cnf file with the appropriate .cnf parser for the solving algorithm
     * and heuristics. It returns a SatProblem which is an interface for classes that hold the sat problem. Some solving
     * algorithms and heuristics require the sat problem to be held in special classes that have different functionality so
     * this method uses the correct parser for those algorithms and heuristics.
     * @param location The absolute path location to the .cnf file.
     * @return the cnf problem as represented as a SatProblem object.
     */
    private SatProblem parseProblem(String location) {

        // CnfFileParserI is an interface for parsers.
        CnfFileParserI parser;

        if (type.equals("DPLL")) {
            // use the special DPLL parser.
            parser = new DPLLCnfFileParser();

        } else if (type.equals("CDCL")) {
            // The specific parser used for CDCL depends on the heuristics being used as different data structures require
            // different files to be used to store the cnf problem. The getCDCLParser uses the correct parser for the CDCL heuristics used.
            parser = getCDCLParser();

        } else {

            return null;
        }

        SatProblem res = parser.parseFileToCreateFormula(location);

        return res;
    }

    /**
     * The createSolver method creates the solver used to solve the cnf problem depending on the solving algorithm
     * and heuristics used. The createSolver method also sets timeout options.
     * @param satProblem The cnf problem the solver is solving.
     * @return the solver that will solve the cnf file.
     */
    private SATSolver createSolver(SatProblem satProblem) {
        SATSolver solver = getSolver(satProblem);

        if (fiveMinuteTimeOut) {
            solver.setFiveMinTimeOut();
        }

        if (overallTimeOut) {
            solver.setOverallTimeOutTime(overallTimeOutTime);
        }

        return solver;
    }

    /**
     * The getSolver method creates the solver used to solve the cnf problem depending on the solving algorithm and
     * heuristics used.
     * @param satProblem The cnf problem the solver is solving.
     * @return the solver that will solve the cnf file.
     */
    private SATSolver getSolver(SatProblem satProblem) {

        if (type.equals("DPLL")) {
            return new DPLLSolver();

        } else if (type.equals("CDCL")) {
            return createCDCLSatSolver(satProblem);
        }
        return null;
    }

    /**
     * The createCDCLSolver method creates the CDCL solver used to solve the cnf problem depending on the heuristics used.
     * @param satProblem The cnf problem the solver is solving.
     * @return the solver that will solve the cnf file.
     */
    private SATSolver createCDCLSatSolver(SatProblem satProblem) {

        // The solver gets each component of the solver. A solver needs: a variable picker, a restart policy and a clause
        // deletion policy. VariablePicker, RestartPolicy and ClauseDeletion are all interfaces that allow a heuristic to
        // have its own functionality.
        VariablePicker variablePicker = getVariableBrancher(satProblem);

        RestartPolicy restartPolicy = getRestartPolicy(satProblem);

        ClauseDeletion clauseDeletion = getClauseDeletionPolicy(satProblem);

        return new CDCLSolver(variablePicker, restartPolicy, clauseDeletion);
    }

    /**
     * The getVariableBrancher method gets the corresponding CDCL variable brancher for the type of solver that
     * was specified by the user.
     * @param satProblem The cnf problem the solver is solving.
     * @return The variable brancher as the interface VariablePicker
     */
    private VariablePicker getVariableBrancher(SatProblem satProblem) {
        if (variableBrancher.equals("VSIDS")) {
            return getVSIDSVariablePicker(satProblem);

        } else if (variableBrancher.equals("AscendingOrder")) {
            return getAscendingVariablePicker(satProblem);
        }

        return null;
    }

    /**
     * The getRestartPolicy method gets the corresponding CDCL restart policy for the type of solver that
     * was specified by the user.
     * @param satProblem The cnf problem the solver is solving.
     * @return The restart policy as the interface RestartPolicy.
     */
    private RestartPolicy getRestartPolicy(SatProblem satProblem) {

        if (restartPolicy.equals("FixedIntervalRestartPolicy")) {
            return new FixedIntervalRestartPolicy(fixedIntervalRestartInitialNumConflictsRestart, fixedIntervalRestartRestartIncrement);

        } else if (restartPolicy.equals("GeometricRestartPolicy")) {
            return new GeometricRestartPolicy(geometricRestartInitialNumConflictsRestart, geometricRestartGeometricCommonRatio);

        } else if (restartPolicy.equals("Luby")) {
            return new LubyRestartPolicy(lubyUnitRun);

        } else if (restartPolicy.equals("NoRestartPolicy")) {
            return new NoRestarts();
        }

        return null;
    }

    /**
     * The getClauseDeletionPolicy method gets the corresponding CDCL clause deletion policy for the type of solver that
     * was specified by the user.
     * @param satProblem The cnf problem the solver is solving.
     * @return The clause deletion policy as the interface ClauseDeletion.
     */
    private ClauseDeletion getClauseDeletionPolicy(SatProblem satProblem) {

        if (clauseDeletion.equals("ActivityDeletion")) {

            SatProblemForCDCL cdclSatProblem = (SatProblemForCDCL) satProblem;
            CnfFormula formula = cdclSatProblem.left;

            double initialMaxNumLearntClauses = formula.getNumConstraintClauses() * activityDeletionInitialMaxNumLearntClausesFactor;

            List<Clause> learntClauses = formula.getLearntClauses();

            return new ActivityDeletion(learntClauses, activityDeletionClauseDecay, initialMaxNumLearntClauses, activityDeletionGeometricCommonRatio);

        } else if (clauseDeletion.equals("NoClauseDeletion")) {

            return new NoDeletion();

        }
        return null;
    }

    /**
     * The getAscendingVariablePicker method gets the Ascending CDCL variable brancher.
     * @param satProblem The cnf problem the solver is solving.
     * @return The Ascending variable brancher.
     */
    private VariablePicker getAscendingVariablePicker(SatProblem satProblem) {

        SatProblemForCDCL cdclSatProblem = (SatProblemForCDCL) satProblem;

        VariablesAssignments assignments = cdclSatProblem.right;

        Variable[] variables = assignments.getVariables();
        Literal[] positiveLiterals = assignments.getPositiveLiterals();
        Literal[] negatedLiterals = assignments.getNegatedLiterals();

        Comparator<Variable> ascendingOrder = new Comparator<Variable>() {
            @Override
            public int compare(Variable o1, Variable o2) {
                int var1 = o1.getVariable();
                int var2 = o2.getVariable();

                if (var1 < var2) {
                    return -1;
                } else if (var1 > var2) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };

        return new AscendingOrder(ascendingOrder, variables, positiveLiterals, negatedLiterals);
    }

    /**
     * The getVSIDSVariablePicker method gets the VSIDS CDCL variable brancher.
     * @param satProblem The cnf problem the solver is solving.
     * @return The VSIDS variable brancher.
     */
    private VariablePicker getVSIDSVariablePicker(SatProblem satProblem) {
        SatProblemForCDCL cdclSatProblem = (SatProblemForCDCL) satProblem;

        VariablesAssignments assignments = cdclSatProblem.right;

        Literal[] positiveLiterals = assignments.getPositiveLiterals();
        Literal[] negatedLiterals = assignments.getNegatedLiterals();

        Comparator<Literal> VSIDSMaxActivityComparator = new Comparator<Literal>() {
            @Override
            public int compare(Literal o1, Literal o2) {
                double activity1 = o1.getActivity();
                double activity2 = o2.getActivity();
                if (activity1 > activity2) {
                    return -1;
                } else if (activity1 < activity2) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };

        return new VSIDSVariablePicker(VSIDSMaxActivityComparator, positiveLiterals, negatedLiterals, vsidsDecayFactor, vsidsBumpActivity, vsidsDecayFrequency);
    }

    /**
     * The getCDCLParser method returns the corresponding parser for the different types of CDCL data structure heuristics.
     * @return The corresponding parser for the data structure heuristic used.
     */
    private CnfFileParserI getCDCLParser() {

        if (dataStructure.equals("2WL")) {
            return new TwoWatchedLiteralsCDCLCnfFileParser();

        } else if (dataStructure.equals("Counters")) {
            return new CountersCDCLCnfFileParser();
        }

    return null;
    }

    /**
     * The getResults method takes the assignment to variables and puts the result as a list of strings to be
     * printed out to the terminal or saved to the results.txt file. If the file was satisfiable it includes a satisfying assignment.
     * The output lines also include the time taken to solve the single file and the total time taken so far when solving.
     * @param instanceNum The instance number of the current file being solved by the solver this session. Each file increments the instanceNum.
     * @param problem The name of the file being solved.
     * @param resAssignments The assignment to variables.
     * @return a list of strings that is the result of solving the problem. The list of strings can be printed out or saved to file.
     */
    private List<String> getResults(int instanceNum, String problem, AssignmentToVariables resAssignments) {
        List<String> res = new ArrayList<>();

        res.add("InstanceNum: " + instanceNum);

        res.add("File: " + problem);

        if (resAssignments.getTimedOut()) {

            res.add("Timed Out");
            res.add("");

        } else if (resAssignments.getSolveable()) {

            res.add("SATISFIABLE");

            StringBuilder assignmentBuilder = new StringBuilder();

            VariableI[] variables = resAssignments.getVariables();
            for (VariableI variable : variables) {
                SingleVariableAssignmentI singleVariableAssignment = variable.getSingleVariableAssignment();
                if (singleVariableAssignment.isAssigned()) {
                    if (singleVariableAssignment.getValue()) {
                        assignmentBuilder.append(variable.getVariable() + " ");
                    } else {
                        assignmentBuilder.append((-variable.getVariable()) + " ");
                    }
                }
            }
            res.add(assignmentBuilder.toString());

        } else {
            res.add("UNSATISFIABLE");
            res.add("");
        }

        long timeToSolve = resAssignments.getTimeToSolve();
        totatTimeSpentSolving += timeToSolve;


        double timeInSecs = timeToSolve * 0.001;
        String timeInString = String.format("%.2f", timeInSecs);

        double totalTimeSpentSolvingSoFarInSecs = totatTimeSpentSolving * 0.001;
        String totalTimeSpentSolvingSoFarInString = String.format("%.2f", totalTimeSpentSolvingSoFarInSecs);

        res.add("Seconds to solve:");
        res.add(timeInString);

        res.add("Total time spent solving in seconds so far:");
        res.add(totalTimeSpentSolvingSoFarInString);

        return res;
    }

    /**
     * The printResultToScreen method prints the result of solving to the screen.
     * @param res the result of solving
     */
    private void printResultToScreen(List<String> res) {

        for (String line : res) {
            System.out.println(line);
        }

    }

    /**
     * The writeResultToTxtFile writes the result to a text file.
     * @param writer The writer for the text file.
     * @param res The result of solving the cnf file.
     */
    private void writeResultToTxtFile(Writer writer, List<String> res) {

        for (String line : res) {
            try {
                writer.write(line);
                writer.write('\n');
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The writeResultToCsvFile writes the result to a Csv file. The format of the csv file is: instanceNum, file name,
     * SATISFIABLE / UNSATISFIABLE / Timed Out, time to solve instance, total time spent solving so far.
     * @param instanceNum The instance number of the current file being solved by the solver this session. Each file increments the instanceNum.
     * @param problem The name of the file being solved.
     * @param writer The writer for the csv file.
     * @param res The result of solving the cnf file.
     */
    private void writeResultToCsvFile(int instanceNum, String problem, Writer writer, List<String> res) {

        StringBuilder csvLineBuilder = new StringBuilder();

        csvLineBuilder.append(instanceNum + ",");
        csvLineBuilder.append(problem + ",");
        // res.get(2) is Whether the problem is SATISFIABLE / UNSATISFIABLE / TimedOut.
        csvLineBuilder.append(res.get(2) + ",");
        // res.get(5) is the time taken to solve the instance.
        csvLineBuilder.append(res.get(5) + ",");
        // res.get(7) is the total time so far spent solving.
        csvLineBuilder.append(res.get(7));

        try {
            writer.write(csvLineBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * the writeNewLineCsv writes a new line to the csv file so another result can be written to the csv file.
     * @param writer The writer for the csv file.
     */
    private void writeNewLineCsv(Writer writer) {
        try {
            writer.write('\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The printVerificationToScreen method prints the results of verification of a solution to a problem to the screen.
     * @param ver the results of verification of a solution to a problem.
     */
    private void printVerificationToScreen(List<String> ver) {

        for (String line : ver) {
            System.out.println(line);
        }
    }

    /**
     * The writeVerificationToTxtFile method writes the results of verification of a solution to a text file.
     * @param writer The writer of the text file.
     * @param ver the results of verification of a solution to a problem.
     */
    private void writeVerificationToTxtFile(Writer writer, List<String> ver) {

        try {
            for (String line : ver) {
                writer.write(line);
                writer.write('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printProblem(int instanceNum, String problem, SatProblem satProblem) {

        System.out.println("InstanceNum: " + instanceNum);

        System.out.println("File " + problem);

        int numClauses = satProblem.getNumProblemClauses();

        for (int i = 0; i < numClauses; i++) {

            ClauseI clause = satProblem.getClauseNum(i);
            int numLits = clause.getNumLiterals();

            for (int j = 0; j < numLits; j++) {
                LiteralI lit = clause.getLitNum(j);
                System.out.print(lit.toString() + " ");
            }
                System.out.println();

        }

        System.out.println("Number of clauses: " + numClauses);
        System.out.println();

    }

    private List<String> getVerifier(int instanceNum, String problem, SatProblem satProblem, boolean timedOut) {

        List<String> toFile = new ArrayList<>();

        toFile.add("Verifier");

        toFile.add("InstanceNum: " + instanceNum);

        toFile.add("File " + problem);

        if (timedOut) {
            toFile.add("Timed Out");

            return toFile;
        }

        if (!satProblem.getSolveable()) {
            toFile.add("UNSATISFIABLE");

            return toFile;
        }

        int numClausesSatisfied = 0;
        boolean overallSatisfied = true;

        int numClauses = satProblem.getNumProblemClauses();

        for (int i = 0; i < numClauses; i++) {

            ClauseI clause = satProblem.getClauseNum(i);
            StringBuilder clauseString = new StringBuilder();

            boolean isClauseSatisfied = false;

            int numLits = clause.getNumLiterals();

            for (int j = 0; j < numLits; j++) {
                LiteralI lit = clause.getLitNum(j);

                clauseString.append(lit.toString() + " ");
            }

            clauseString.append("|| ");

            for (int j = 0; j < numLits; j++) {
                LiteralI lit = clause.getLitNum(j);

                boolean isNegated = lit.isNegated();
                VariableI variable = lit.getVariable();
                SingleVariableAssignmentI assignment = variable.getSingleVariableAssignment();
                boolean assigned = assignment.isAssigned();
                boolean value = assignment.getValue();

                if (!assigned) {
                    clauseString.append("u" + " ");

                } else if (isNegated) {
                    if (value) {
                        clauseString.append("0" + " ");
                    } else {
                        clauseString.append("1" + " ");
                        isClauseSatisfied = true;
                    }
                } else {
                    if (value) {
                        clauseString.append("1" + " ");
                        isClauseSatisfied = true;
                    } else {
                        clauseString.append("0" + " ");
                    }
                }
            }

            if (isClauseSatisfied) {
                numClausesSatisfied++;
                clauseString.append("Satisfied");
                toFile.add(clauseString.toString());
            } else {
                overallSatisfied = false;
                clauseString.append("UnSatisfied");
                toFile.add(clauseString.toString());
                return toFile;
            }
        }

        toFile.add("");

        if (overallSatisfied) {
            if (numClausesSatisfied == numClauses) {
                toFile.add("Satisfied all " + numClauses + " clauses.");
            }

            toFile.add("Satisfied " + numClausesSatisfied + " of the " + numClauses + " clauses.");
        } else {
            toFile.add("Not Satisfied");
        }

        toFile.add("");

        return toFile;
    }

    private void setParameters(String[] args) {

        for (int i = 1; i < args.length; i++) {
            String parameter = args[i];

            if (parameter.isEmpty()) {
                continue;
            }

            if (type == null) {
                if (parameter.equals("-DPLL")) {
                    type = "DPLL";
                    continue;

                } else if (parameter.equals("-CDCL")) {
                    type = "CDCL";
                    continue;
                }
            }

            if (dataStructure == null) {

                if (parameter.equals("-2WL")) {
                    dataStructure = "2WL";
                    continue;

                } else if (parameter.equals("-Counters")) {
                    dataStructure = "Counters";
                    continue;
                }
            }

            if (variableBrancher == null) {

                if (parameter.equals("-VSIDS")) {
                    variableBrancher = "VSIDS";
                    continue;

                } else if (parameter.equals("-Ascending")) {
                    variableBrancher = "AscendingOrder";
                    continue;
                }
            }

            if (restartPolicy == null) {

                if (parameter.equals("-FixedRestart")) {
                    restartPolicy = "FixedIntervalRestartPolicy";
                    continue;

                } else if (parameter.equals("-GeoRestart")) {
                    restartPolicy = "GeometricRestartPolicy";
                    continue;

                } else if (parameter.equals("-Luby")) {
                    restartPolicy = "Luby";
                    continue;

                } else if (parameter.equals("-NoRestart")) {
                    restartPolicy = "NoRestartPolicy";
                    continue;

                }
            }

            if (clauseDeletion == null) {

                if (parameter.equals("-ActivDel")) {
                    clauseDeletion = "ActivityDeletion";
                    continue;

                } else if (parameter.equals("-NoDel")) {
                    clauseDeletion = "NoClauseDeletion";
                    continue;
                }
            }

            if (parameter.equals("-ResCsv")) {
                resultToCsvFile = true;
                continue;
            }

            if (parameter.equals("-ResTxt")) {
                resultToTxtFile = true;
                continue;
            }

            if (parameter.equals("-VerScreen")) {
                verificationToScreen = true;
                continue;
            }

            if (parameter.equals("-VerFile")) {
                verificationToFile = true;
                continue;
            }

            if (parameter.equals("-PrintProb")) {
                printProblem = true;
                continue;
            }

            if (parameter.equals("-5MinIndivTimeOut")) {
                fiveMinuteTimeOut = true;
                continue;
            }

            if (parameter.startsWith("-OverallSecTimeOut=")) {

                overallTimeOut = true;

                String[] split = parameter.split("=");
                String overallTimeOutTimeTemp = split[1].trim();

                int time = Integer.parseInt(overallTimeOutTimeTemp);

                lengthOfOverallTimeOutTimeInSeconds = time;

                continue;
            }
        }

        if (type == null) {
            type = "CDCL";
        }

        if (dataStructure == null) {
            dataStructure = "2WL";
        }

        if (variableBrancher == null) {
            variableBrancher = "VSIDS";
        }

        if (restartPolicy == null) {
            restartPolicy = "GeometricRestartPolicy";
        }

        if (clauseDeletion == null) {
            clauseDeletion = "ActivityDeletion";
        }
    }

    private void addTimeConstraints(List<String> res) {

        StringBuilder builder = new StringBuilder();
        builder.append("TimedConstraints: ");

        if (fiveMinuteTimeOut && overallTimeOut) {
            builder.append("5MinIndivTimeOut, OverallSecTimeOut=" + lengthOfOverallTimeOutTimeInSeconds + ".");

        } else if (fiveMinuteTimeOut) {
            builder.append("5MinIndivTimeOut.");

        } else if (overallTimeOut) {
            builder.append("OverallSecTimeOut=" + lengthOfOverallTimeOutTimeInSeconds + ".");

        } else {
            builder.append("No TimedConstraints.");
        }
        res.add(builder.toString());
    }

    private void addTunableParameters(List<String> res) {

        StringBuilder builder = new StringBuilder();
        builder.append("Tunable parameters: ");
        if (type.equals("DPLL")) {
            builder.append("No tunable parameters.");

        } else if (type.equals("CDCL")) {
            builder.append(getDataStructureTunableParameters() + ", " + getVariableBrancherTunableParameters() +
                    ", " + getRestartPolicyTunableParameters() + ", " + getClauseDeletionTunableParameters() + ".");
        }

        res.add(builder.toString());
    }

    private String getDataStructureTunableParameters() {

        if (dataStructure.equals("2WL")) {
            return("2WL: No Tunable Parameters");

        } else if (dataStructure.equals("Counters")) {
            return("Counters: No Tunable Parameters");
        }
        return "";
    }

    private String getVariableBrancherTunableParameters() {

        if (variableBrancher.equals("VSIDS")) {
             return ("VSIDS: " + "vsidsDecayFactor " + vsidsDecayFactor + " vsidsBumpActivity " + vsidsBumpActivity
              + " vsidsDecayFrequency " + vsidsDecayFrequency);


        } else if (variableBrancher.equals("AscendingOrder")) {
            return ("AscendingOrder: No Tunable Parameters");
        }

        return "";
    }

    private String getRestartPolicyTunableParameters() {

        if (restartPolicy.equals("FixedIntervalRestartPolicy")) {
            return ("FixedIntervalRestartPolicy: " + "fixedIntervalRestartInitialNumConflictsRestart " + fixedIntervalRestartInitialNumConflictsRestart
            + " fixedIntervalRestartRestartIncrement " + fixedIntervalRestartRestartIncrement);

        } else if (restartPolicy.equals("GeometricRestartPolicy")) {
            return ("GeometricRestartPolicy: " + "geometricRestartInitialNumConflictsRestart " + geometricRestartInitialNumConflictsRestart
            + " geometricRestartGeometricCommonRatio " + geometricRestartGeometricCommonRatio);

        } else if (restartPolicy.equals("Luby")) {
            return ("Luby: " + "lubyUnitRun " + lubyUnitRun);

        } else if (restartPolicy.equals("NoRestartPolicy")) {
            return ("NoRestartPolicy: No Tunable Parameters");
        }
        return "";
    }

    private String getClauseDeletionTunableParameters() {

        if (clauseDeletion.equals("ActivityDeletion")) {
            return ("ActivityDeletion: " + "activityDeletionClauseDecay " + activityDeletionClauseDecay + " activityDeletionInitialMaxNumLearntClausesFactor "
            + activityDeletionInitialMaxNumLearntClausesFactor + " activityDeletionGeometricCommonRatio " + activityDeletionGeometricCommonRatio);

        } else if (clauseDeletion.equals("NoClauseDeletion")) {
            return ("NoClauseDeletion: No Tunable Parameters");
        }
        return "";
    }
}

