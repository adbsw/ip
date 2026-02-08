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
// import java.util.Arrays;

public class Green {
    private static final int ARRAY_SIZE = 100;
    private static final Task[] tasks = new Task[ARRAY_SIZE];
    private static int taskIndex = 0;
    private static final String lineDivider = "-----------------------------------------------\n";

    public static void addTask(Task newTask) {
        if (taskIndex < tasks.length && !isInList(newTask)) {
            tasks[taskIndex] = newTask;
            taskIndex++;
            System.out.println(lineDivider +
                    "Task '" + newTask.getDescription() + "' has been added."
                    + System.lineSeparator() + lineDivider);
        } else if (isInList(newTask)) {
            System.out.println(lineDivider +
                    "Task '" + newTask.getDescription() + "' is already in the list."
                    + System.lineSeparator() + lineDivider);
        } else {
            System.out.println(lineDivider +
                    "Unable to add task. List is full."
                    + System.lineSeparator() + lineDivider);
        }
    }

    public static boolean isInList(Task t) {
        for (int i = 0; i < taskIndex; i++) {
            if (tasks[i].getDescription().equals(t.getDescription())) {
                return true;
            }
        }
        return false;
    }

    public static void markTask(Task t, boolean status) {
        for (int i = 0; i < taskIndex; i++) {
            if (tasks[i].getDescription().equals(t.getDescription())) {
                tasks[i].setDone(status);
                System.out.println(lineDivider);
                System.out.println(lineDivider +
                        "Task '" + t.getDescription() + "' has been" + (status ? " marked done." : " unmarked.")
                        + System.lineSeparator() + lineDivider);
                return;
            }
        }
        System.out.println(lineDivider +
                "Task '" + t.getDescription() + "' is not in the list. Would you like to add it?"
                + System.lineSeparator() + lineDivider);
    }

    public static void showList() {
        System.out.println(lineDivider + "Here is the list of things:");
        for (int i = 0; i < taskIndex; i++) {
            System.out.println("[" + tasks[i].getStatusIcon() + "] " + (i + 1) + ". " + tasks[i].getDescription());
        }
        System.out.println(lineDivider);
    }

    public static void main(String[] args) {
        String input;
        Scanner in = new Scanner(System.in);
        System.out.println("Greetings");
        System.out.println(
                lineDivider +
                        """
                                Hi! I'm Green. What can I do for you today?
                                
                                    To add a new task, enter 'add [task name]' .
                                    To mark a task as done, enter 'mark [task name]'.
                                    To unmark a task, enter 'unmark [task name]'.
                                    To show the current to-do list, enter 'list'.
                                    Type 'bye' to leave.
                                """
                        + lineDivider);

        boolean byeFlag = false;
        while (!byeFlag) {
            input = in.nextLine();
            String[] words = input.strip().split(" ", 2);
            String command = words[0];
            Task task = new Task(null);

            if (words.length > 1) {
                task.setDescription(words[1]);
            }

            switch (command) {
            case "add":
                if (words.length < 2) {
                    System.out.println(lineDivider +
                            "Missing task entry. What would you like to add?"
                            + System.lineSeparator() + lineDivider);
                } else {
                    addTask(task);
                }
                break;
            case "mark":
                if (words.length < 2) {
                    System.out.println(lineDivider +
                            "Missing task entry. What would you like to mark?"
                            + System.lineSeparator() + lineDivider);
                } else {
                    markTask(task, true);
                }
                break;
            case "unmark":
                if (words.length < 2) {
                    System.out.println(lineDivider +
                            "Missing task entry. What would you like to unmark?"
                            + System.lineSeparator() + lineDivider);
                } else {
                    markTask(task, false);
                }
                break;
            case "list":
                showList();
                break;
            case "bye":
                byeFlag = true;
                System.out.println(lineDivider + "Goodbye, have a nice day"
                        + System.lineSeparator() + lineDivider);
                break;
            default:
                break;
            }
        }
    }
}
