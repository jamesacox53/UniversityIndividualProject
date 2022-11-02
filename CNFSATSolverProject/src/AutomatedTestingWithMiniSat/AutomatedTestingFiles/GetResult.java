package AutomatedTestingWithMiniSat.AutomatedTestingFiles;

public class GetResult {

    public static void main(String[] args) {
        String input = args[0];
        String[] strings = input.split("\n");
        System.out.print(strings[strings.length - 1].trim());
        
    }

}
