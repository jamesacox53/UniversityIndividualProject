package AutomatedTestingWithMiniSat.AutomatedTestingFiles;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CheckIfAlreadySolved {

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("There were no arguments");
            return;
        }

        String inputFilePath = args[0];

        File file = new File(inputFilePath);

        try {
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String trimmedLine = line.trim();

                if (trimmedLine.contains("SATISFIABLE") || trimmedLine.contains("UNSATISFIABLE")) {
                    System.out.print("true");
                    return;
                }

                if (trimmedLine.startsWith("p")) {
                    System.out.print("false");
                    return;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
