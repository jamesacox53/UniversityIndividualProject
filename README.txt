User Guide

To use this software system the Java JDK version 10.0.1 or higher must be installed on the machine. 
To use this software, you must be able to run Java from the command line.

This software system includes Java ByteCode compiled for Java version 10.0.1. This software system 
can be run on many operating systems. These include: Windows, Linux and Mac. The only difference 
between running the software system on different operating systems is the way the user specifies 
the absolute file paths.

To use this software system on Windows please open the Windows Command Prompt.  To use this 
software system on Linux or Mac please open the Terminal. Once inside the Command 
Prompt/Terminal please navigate inside the software system folder. To navigate you use the cd 
command and specify the name of the directory you want to navigate to inside quotation marks “”. 
To get a list of the directories and files in the current directory on Windows you type the command 
dir, on Linux and Mac you type the command ls. For example, if the ExampleDirectory directory is 
inside the current directory and I want to navigate inside it I would type:

cd “ExampleDirectory”

Surrounding the name of the directory inside quotation marks allows the directory name to contain 
spaces. If you wanted to navigate inside the ‘Test Directory’ directory you would write:

cd “Test Directory”

Typing:

cd .. 

navigates back a directory.   

Inside the software system folder there is a sub folder called CNFSATSolverProject and a text file 
called README.txt. The CNFSATSolverProject folder includes the files for the software system. The 
README.txt file contains the user guide. It includes instructions for building and running this 
software system from the material I have submitted. To use the software system please navigate 
inside the CNFSATSolverProject directory. Inside CNFSATSolverProject there are two sub folders: out 
and src. The src folder contains all the source code of this project which can be examined. It contains 
all the source code files for this system. The out directory contains the complied Java ByteCode for 
Java version 10.0.1 that can be run. To run the software system, navigate inside the out folder. Next 
navigate inside the production folder. Navigate inside the CNFSATSolverProject folder.
 
The current directory is the CNFSATSolverProject directory. This is the directory where all code is run 
from. The CNFSATSolverProject directory contains 3 sub folders: CNFSATSolver, 
CNFRandomBalancedInstanceGenerator and AutomatedTestingWithMiniSat. These 3 sub folders 
contain the compiled code for the 3 main parts of the project. CNFSATSolver contains the compiled 
code of the SAT solver and is used to run the SAT solving software. 
CNFRandomBalancedInstanceGenerator contains the compiled code for the generator which 
generates randomly generated balanced .cnf instances. AutomatedTestingWithMiniSat contains the 
compiled code for testing a directory of .cnf problems with MiniSat-2.2.0.

To run the code do not navigate inside any of these directories. All code is run from the current 
CNFSATSolverProject directory as the Java code is in packages and Java code in packages has to be 
run like this.


SAT Solving Software

The command to run the SAT solving software has the format:

java CNFSATSolver.Main absolute_file_path command_line_arguments

The absolute_file_path is the absolute file path to the .cnf problem or directory that contains some 
.cnf problems that the user wishes the SAT solving software to solve. 

To specify an absolute file path it is different on Windows, Linux and Mac. For Windows please 
specify the absolute path to the problem/directory from the drive it is stored on. For example if I 
wanted to solve the .cnf problems in the directory ExampleCNFProblems which is on the C (C:) drive 
and is located at:   

C:\Users\james\Documents\Kings\Year 3\ExampleCNFProblems

Then the absolute file path for the ExampleCNFProblems directory is:

C:\Users\james\Documents\Kings\Year 3\ExampleCNFProblems

When specifying the absolute file path for all Windows, Linux and Mac to the command line for the 
software system you must surround the absolute file path in quotation marks “” as this means the 
absolute file path can contain spaces and won’t be interpreted as separate arguments. When the 
SAT solving software is run without command line arguments it is run with the defaults: CDCL, VSIDS, 
2WL, Geometric Restart Policy, Activity Based Clause Deletion, No saving results or verification to 
files, No printing verification to screen, No printing problem to screen and No individual or overall 
time out. On Windows to solve the .cnf files in the ExampleCNFProblems directory with the defaults 
you could type the command:

java CNFSATSolver.Main “C:\Users\james\Documents\Kings\Year 3\ExampleCNFProblems”

If you just wanted to solve the cnf-balanced-20-70-0.cnf file in ExampleCNFProblems folder on 
Windows with the defaults you could type the command:

java CNFSATSolver.Main “C:\Users\james\Documents\Kings\Year 3\ExampleCNFProblems\cnf-
balanced-20-70-0.cnf”

For all Windows, Linux and Mac when specifying a directory to solve all .cnf problems in or a 
single.cnf file the absolute file path cannot end with a slash / or \. For a directory of files, it must end 
with the name of the directory. For a single .cnf file it must end with .cnf. 

For Linux you cannot specify the path starting with ~/. The absolute path must be specified with 
/home/user_name/. Where user_name is the user name of the user where the .cnf file/files are 
stored on their account. Linux and Mac can get the absolute path to the current directory with the 
pwd command. To solve the .cnf files in ExampleCNFProblems on Linux where ExampleCNFProblems 
is stored on the ubuntu user account and ExampleCNFProblems is located at 
~/Documents/ExampleCNFProblems then the absolute file path is:

/home/ubuntu/Documents/ExampleCNFProblems

On Linux to solve the .cnf files in the ExampleCNFProblems directory with the defaults you could 
type the command:

java CNFSATSolver.Main “/home/ubuntu/Documents/ExampleCNFProblems”

For Mac you also cannot specify the path starting with ~/. The absolute path must be specified with 
/Users/user_name/. Where user_name is the user name of the user where the .cnf file/files are 
stored on their account. To solve the .cnf files in ExampleCNFProblems on Mac where 
ExampleCNFProblems is stored on the James user account and ExampleCNFProblems is located at 
~/Documents/ExampleCNFProblems then the absolute file path is:

/Users/James/Documents/ExampleCNFProblems

On Mac to solve the .cnf files in the ExampleCNFProblems directory with the defaults you could type 
the command:

java CNFSATSolver.Main “/Users/James/Documents/ExampleCNFProblems”


Command Line Arguments

The command_line_arguments part of the command represents command line arguments. There 
can be 0 or more command line arguments. The way command line arguments are given to 
Windows, Linux and Mac are the same. Each command line argument is separated by spaces. Each 
command line argument is surrounded with quotation marks “”. Command line arguments change 
the way the SAT solver solves. It can change the SAT solving algorithms and heuristics used and can 
set options. The command line arguments are specified after the absolute file path and can be given 
in any order. There are command line arguments that: set the SAT solving algorithm and heuristics, 
save the result of solving to files, print verification of satisfying assignments to screen, save 
verification of satisfying assignments to file, set a 5 minute individual problem time out, set an 
overall time out and print the problem to screen. 

The DPLL algorithm doesn’t work with heuristics so any heuristics specified with DPLL will be 
ignored. For CDCL the SAT solver can only use one heuristic of each heuristic type: Variable 
Branching, Data Structure, Restart Policy, Clause Deletion Policy. The command line arguments are 
read from left to right and the first SAT solving algorithm/heuristic read from left to right will be the 
algorithm/heuristic used. All other SAT solving algorithms/heuristics of the same heuristic type 
specified after will be ignored. If a command line argument is spelt incorrectly or if the software 
system doesn’t recognize the command line argument the command line argument is ignored.

If a heuristic type has no specified heuristic, then the default for each heuristic type is: 

SAT solving algorithm: CDCL,
Variable Branching: VSIDS,
Data Structure: 2WL,
Restart Policy: Geometric Restart Policy,
Clause Deletion Policy: Activity Based Clause Deletion.

Here are the different command line arguments:
Each command line argument is written in quotation marks “”.
“-DPLL” : Use the DPLL SAT solving algorithm.
“-CDCL” : Use the CDCL SAT solving algorithm.
“-2WL” : Use the Two Watched Literals Data Structure heuristic.
“-Counters” : Use the Counters Data Structure that is used by the basic CDCL algorithm.
“-VSIDS” : Use the VSIDS Variable Branching heuristic.
“-Ascending” : Use the Ascending Variable Branching heuristic that is used by the basic CDCL 
algorithm.
“-FixedRestart” : Used the Fixed Interval Restart Policy heuristic.
“-GeoRestart” : Use the Geometric Restart Policy heuristic.
“-Luby” : Use the Luby Restart Policy heuristic.
“-NoRestart” : Do not restart. This is used by the basic CDCL algorithm.
“-ActivDel” : Use the Activity Based Clause Deletion heuristic. 
“-NoDel” : Do not delete any learnt clauses. This is used by the basic CDCL algorithm.
“-ResCsv” : Saves the result of solving to a .csv file called results.csv in the directory of the 
problem/problems solved. The results.csv file doesn’t contain the satisfying assignments.
“-ResTxt” : Saves the result of solving to a .txt file called results.txt in the directory of the 
problem/problems solved. The results.txt file includes the satisfying assignments.
“-VerScreen” : Verifies the satisfying assignments with the verifier and prints the results of 
verification to screen. It prints each clause and replaces each literal in each clause with the variable’s 
assigned value and evaluates the literal. It prints 1 if the assignment satisfied the literal, 0 if the 
assignment didn’t satisfy the literal and u if the variable was unassigned. The user can then see 
where each clause was satisfied and verify if this satisfying assignment is actually a satisfying 
assignment. 
“-VerFile” : Verifies the satisfying assignments with the verifier and saves the results of verification 
to a file called verification.txt in the directory of the problem/problems solved.
“-PrintProb” : Prints the problem to screen. Doesn’t print any comments or the problem line.
“-5MinIndivTimeOut” : Sets it so that the solver stops solving an individual problem after 5 minutes if 
it has not found a solution. The solver will then begin solving the next problem.
“-OverallSecTimeOut=x” : Sets it so that the solver stops solving all problems after x seconds have 
passed from when the solver started initially solving.

The heuristic types of each heuristic command line argument are:
Data Structure heuristics: “-2WL”, “-Counters”.
Variable Branching heuristics: “-VSIDS”, “-Ascending”.
Restart Policy heuristics: “-FixedRestart”, “-GeoRestart”, “-Luby”, “-NoRestart”.
Clause Deletion Policy heuristics: “-ActivDel”, “-NoDel”.

If you were on Windows and wanted to solve the .cnf files in the ExampleCNFProblems directory 
which is located at: 
  
C:\Users\james\Documents\Kings\Year 3\ExampleCNFProblems

With: the Counters Data Structure heuristic, the Ascending Variable Branching heuristic, the Luby 
restart policy, the no learnt clause deletion heuristic, a 400 second overall time out, save the results 
to the results.csv file and print the verification to screen. You could write the command:
 
java CNFSATSolver.Main “C:\Users\james\Documents\Kings\Year 3\ExampleCNFProblems” 
“-Counters” “-Ascending” “-Luby” “-NoDel” “-OverallSecTimeOut=400” “-ResCsv” “-VerScreen”


Generate Randomly Generated Balanced Instances Software

To run the code to generate randomly generated balanced instances you have to still be in the 
CNFSATSolverProject directory. The format of the command to generate randomly generated 
balanced instances is:

java CNFRandomBalancedInstanceGenerator.CNFProblemGenerator numVar numClause 
numGenerate absolute_file_path

Where the numVar, numClause, numGenerate and absolute_file_path must be wrapped in 
quotation marks “”. The absolute_file_path is the absolute file path to the directory the user wants 
the randomly generated balanced instance .cnf files to be generated in. The same rules apply to this 
absolute_file_path as the absolute_file_path described in the SAT Solving Software section. Please 
refer to the SAT Solving Software section to see how to do the absolute_file_path for each operating 
system. numVar is the number of variables the user wants each problem to have. numClause is the 
number of clauses the user wants each problem to have. numGenerate is the number of randomly 
generated balanced instances the user wants to generate.

For example, if I wanted to generate 20 randomly generated balanced instances with 200 variables 
and 3000 clauses to the directory ExampleGeneratedProblems which has the absolute file path of:   

C:\Users\james\Documents\Kings\Year 3\ExampleGeneratedProblems

Then I would use the command:

java CNFRandomBalancedInstanceGenerator.CNFProblemGenerator “200” “3000” “20” 
“C:\Users\james\Documents\Kings\Year 3\ExampleGeneratedProblems”


Test Directory of .cnf files with MiniSat-2.2.0 Software

To run the code to test a directory which contains some .cnf file with MiniSat-2.2.0 you have to still 
be in the CNFSATSolverProject directory. This part of the software system only works on Linux. The 
code to test directories of .cnf files with MiniSat-2.2.0 is in the AutomatedTestingWithMiniSat folder. 
This code relies on MiniSat-2.2.0 however MiniSat-2.2.0 is not included with this software system. 
MiniSat-2.2.0 must be downloaded and installed separately. This software needs to know the path 
to the user’s minisat executable file. The current path to the minisat executable file is 
~/Documents/MiniSat/minisat/core/minisat. If this needs to be changed then the user needs to 
navigate into the AutomatedTestingWithMiniSat folder. The AutomatedTestingWithMiniSat folder 
contains a .sh file called automatedMiniSatTest.sh and a sub folder called AutomatedTestingFiles. 
The user needs to open up the automatedMiniSatTest.sh in a text editor and replace the path to the 
minisat executable on line 12 with the path to their minisat executable. This path can use ~/. The 
user then must navigate back to the CNFSATSolverProject directory where all code is run from. 

The command on the command line to run MiniSat-2.2.0 on a directory of .cnf problems and save 
the results into the .cnf problems is:

./AutomatedTestingWithMiniSat/automatedMiniSatTest.sh absolute_file_path

Where the absolute_file_path is the absolute file path to the directory of .cnf files you want to run 
MiniSat-2.2.0 on. The same rules apply to this absolute_file_path as the absolute_file_path 
described in the SAT Solving Software section. Please refer to the SAT Solving Software section to 
see how to do the absolute_file_path for each operating system.

