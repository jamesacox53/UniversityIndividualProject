package AutomatedTestingWithMiniSat.AutomatedTestingFiles;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class UnSatisfiable {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("There were no arguments");
            return;
        }

        String inputFilePath = args[0];
        File file = new File(inputFilePath);
        ArrayList<String> newFile = new ArrayList<>();

        String time = args[1].trim();

        try {
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String trimmedLine = line.trim();
                if (trimmedLine.startsWith("p")) {
                    newFile.add("c UNSATISFIABLE");
                    newFile.add("c " + time);
                }
                newFile.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            for (int i = 0; i < newFile.size(); i++) {
                String line = newFile.get(i);
                if (i == (newFile.size() - 1)) {
                    writer.write(line);
                } else {
                    writer.write(line);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            System.out.println("Unable to write to file");
            e.printStackTrace();
        }
    }
}

