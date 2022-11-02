package AutomatedTestingWithMiniSat.AutomatedTestingFiles;

public class GetPath {
    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("There were no arguments");
            return;
        }

        String arg = args[0];
        arg = arg.trim();
        StringBuilder res = new StringBuilder();

        for (int i = 0; i < arg.length(); i++) {

            char character = arg.charAt(i);
            if (character == ' ') {
                res.append("\\ ");
            } else {
                res.append(character);
            }
        }

        if (res.charAt(res.length() -1) == '/') {
            res.append("*.cnf");
        } else {
            res.append("/*.cnf");
        }
        System.out.print(res.toString());
    }

}
