import java.util.Scanner;

public class Green {
    private static final int ARRAY_SIZE = 100;
    private static Task[] tasks = new Task[ARRAY_SIZE];
    private static int taskIndex = 0;
    private static final String LINE_DIVIDER = "-----------------------------------------------\n";

    public static void addTask(Task newTask) {
        if (taskIndex < tasks.length && !isInList(newTask)) {
            tasks[taskIndex] = newTask;
            taskIndex++;
            System.out.println(LINE_DIVIDER +
                    "Task '" + newTask.getDescription() + "' has been added."
                    + System.lineSeparator() + LINE_DIVIDER);
        } else if (isInList(newTask)) {
            System.out.println(LINE_DIVIDER +
                    "Task '" + newTask.getDescription() + "' is already in the list."
                    + System.lineSeparator() + LINE_DIVIDER);
        } else {
            System.out.println(LINE_DIVIDER +
                    "Unable to add task. List is full."
                    + System.lineSeparator() + LINE_DIVIDER);
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

    public static void changeTaskStatus(Task t, boolean status) {
        for (int i = 0; i < taskIndex; i++) {
            if (tasks[i].getDescription().equals(t.getDescription())) {
                tasks[i].setDone(status);
                System.out.println(LINE_DIVIDER +
                        "Task '" + t.getDescription() + "' has been"
                        + (status ? " marked done." : " unmarked.")
                        + System.lineSeparator() + LINE_DIVIDER);
                return;
            }
        }
        System.out.println(LINE_DIVIDER +
                "Task '" + t.getDescription() + "' is not in the list. Would you like to add it?"
                + System.lineSeparator() + LINE_DIVIDER);
    }

    public static void showList() {
        System.out.println(LINE_DIVIDER + "Here is the list of things:");
        for (int i = 0; i < taskIndex; i++) {
            System.out.println("[" + tasks[i].getStatusIcon() + "] "
                    + (i + 1) + ". " + tasks[i].getDescription());
        }
        System.out.println(LINE_DIVIDER);
    }

    public static void main(String[] args) {
        String input;
        Scanner in = new Scanner(System.in);
        System.out.println(LINE_DIVIDER + "Greetings from");
        String logo =
                """
                     _____     _____    ____   ____   _    _
                    |  _ _|   |  _  \\  |  __| |  __| | \\  | |
                    | |  _ _  | |_| /  |  __| |  __| | |\\ | |
                    | |_| | | | | \\ \\  |  __| |  __| | | \\| |
                    |____/ \\_\\\\_|  \\_\\ \\____\\ \\____\\ \\_|  \\_|
                """;
        System.out.println(logo);
        System.out.println(
                """
                 What would you like to do today?
                 
                         To add a new task, enter 'add [task name]'.
                         To mark a task as done, enter 'mark [task name]'.
                         To unmark a task, enter 'unmark [task name]'.
                         To show the current to-do list, enter 'list'.
                         Type 'bye' to leave.
                 """
                 + LINE_DIVIDER);

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
                    System.out.println(LINE_DIVIDER
                            + "Missing task entry. What would you like to add?"
                            + System.lineSeparator() + LINE_DIVIDER);
                } else {
                    addTask(task);
                }
                break;
            case "mark":
                if (words.length < 2) {
                    System.out.println(LINE_DIVIDER
                            + "Missing task entry. What would you like to mark?"
                            + System.lineSeparator() + LINE_DIVIDER);
                } else {
                    changeTaskStatus(task, true);
                }
                break;
            case "unmark":
                if (words.length < 2) {
                    System.out.println(LINE_DIVIDER
                            + "Missing task entry. What would you like to unmark?"
                            + System.lineSeparator() + LINE_DIVIDER);
                } else {
                    changeTaskStatus(task, false);
                }
                break;
            case "list":
                showList();
                break;
            case "bye":
                byeFlag = true;
                System.out.println(LINE_DIVIDER
                        + "Goodbye, have a nice day"
                        + System.lineSeparator() + LINE_DIVIDER);
                break;
            default:
                break;
            }
        }
    }
}
