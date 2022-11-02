package AutomatedTestingWithMiniSat.AutomatedTestingFiles;

public class GetMiniSatCPUTime {

    public static void main(String[] args) {
        String input = args[0];
        String[] strings = input.split("\n");
        for (int i = strings.length - 1; i >= 0; i--) {
            String t = strings[i];
            String s = t.trim();
            if (s.contains("CPU time")) {
                StringBuilder string1 = new StringBuilder();
                string1.append("MiniSat ");
                boolean space = false;
                for (int j = 0; j < s.length(); j++) {
                    char ch = s.charAt(j);
                    if (ch == ' ') {
                        if (!space) {
                            string1.append(ch);
                            space = true;
                        }
                    } else {
                        string1.append(ch);
                        space = false;
                    }
                }

                System.out.print(string1.toString());
                return;
            }
        }
        System.out.print("No Time");
    }


}
