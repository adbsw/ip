/*public class Duke {
    public static void main(String[] args) {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        System.out.println("Hello from\n" + logo);
    }
}*/
import java.util.Scanner;

public class Green {
    public static void main(String[] args) {
        String line;
        String lineDivider = "-----------------------------------------------\n";
        String byeCommand = "bye";
        Scanner in = new Scanner(System.in);
        System.out.println(lineDivider + "Hi! I'm Green.\nWhat can I do for you today?\n" + lineDivider);
        while (true) {
            line = in.nextLine();
            if (line.equals(byeCommand)) {
                System.out.println(lineDivider + "Goodbye! Hope to see you again!\n" + lineDivider);
                break;
            }
            System.out.println(lineDivider + line + "\n" + lineDivider);
        }

        /*String textBlock = """
                -----------------------------------------------
                Hi! I'm Green.
                What can I do for you today?
                -----------------------------------------------
                Goodbye! Hope to see you again!
                -----------------------------------------------
                """;
        System.out.println(textBlock);*/
    }
}
