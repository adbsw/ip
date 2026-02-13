import java.util.Scanner;

public class Green {
    private static final int ARRAY_SIZE = 100;
    private static Task[] tasks = new Task[ARRAY_SIZE];
    private static int taskCount = 0;
    private static final String LINE_DIVIDER = "---------------------------------------------------------\n";

    /** Adds task to current list */
    public static void addTask(Task newTask) {
        if (taskCount < tasks.length && !isInList(newTask)) {
            tasks[taskCount] = newTask;
            taskCount++;
            System.out.println(LINE_DIVIDER +
                    "'" + newTask.getDescription() + "' has been added."
                    + System.lineSeparator() + LINE_DIVIDER);
        } else if (isInList(newTask)) {
            System.out.println(LINE_DIVIDER +
                    "Failed to add '" + newTask.getDescription() + "'. It is already in the list."
                    + System.lineSeparator() + LINE_DIVIDER);
        } else {
            System.out.println(LINE_DIVIDER +
                    "Unable to add task. List is full."
                    + System.lineSeparator() + LINE_DIVIDER);
        }
    }

    /** Returns true if task exists in the current list */
    public static boolean isInList(Task t) {
        for (int i = 0; i < taskCount; i++) {
            if (tasks[i].equals(t)) {
                return true;
            }
        }
        return false;
    }

    /** Updates task status accordingly to user's request of 'mark' or 'unmark' */
    public static void changeTaskStatus(int num, boolean status) {
        if (num > taskCount|| num == 0) {
            System.out.println(LINE_DIVIDER + "There is no task numbered " + num + "."
                    + System.lineSeparator() + LINE_DIVIDER);
            return;
        }

        if (tasks[num - 1].isDone() == status) {
            System.out.println(LINE_DIVIDER
                    + "'" + num + ". " + tasks[num - 1].getDescription() + "'" + " was previously "
                    + (status ? "marked done." : "unmarked.")
                    + System.lineSeparator() + LINE_DIVIDER);
            return;
        }

        tasks[num - 1].setDone(status);
        System.out.println(LINE_DIVIDER
                + "'" + num + ". " + tasks[num - 1].getDescription() + "'" + " has been "
                + (status ? "marked done." : "unmarked.")
                + System.lineSeparator() + LINE_DIVIDER);
    }

    /** Prints current list of tasks */
    public static void showList() {
        if (taskCount == 0) {
            System.out.println(LINE_DIVIDER + "Your list is empty."
                    + System.lineSeparator() + LINE_DIVIDER);
            return;
        }

        System.out.println(LINE_DIVIDER + "Here is the list of things:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + ". " + tasks[i].toString());
        }
        System.out.println(LINE_DIVIDER);
    }

    public static String parseCommand(String[] words) {
        return switch (words[0]) {
            case "todo", "deadline", "event", "mark", "unmark", "list", "bye" -> words[0];
            default -> null;
        };
    }

    /** Extract and return corresponding details and class of task from user input */
    public static Task parseInput(String command, String[] words) {
        if (words.length <= 1) {
            return null;
        }

        String[] details = words[1].trim().split("/");
        String description = details[0].trim();
        switch (command) {
        case "todo":
            return new ToDo(description);
        case "deadline":
            String by = details[1].substring(("by".length() + 1)).trim();
            return new Deadline(description, by);
        case "event":
            String from = details[1].substring(("from".length() + 1)).trim();
            String to = details[2].substring(("to".length() + 1)).trim();
            return new Event(description, from, to);
        default:
            return new Task(description);
        }
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
                 
                         To add a new todo, deadline  or event, enter:
                         - 'todo [task name]'
                         - 'deadline [task name] /by [task's deadline]'
                         - 'event [task name] /from [event start] /to [event end]'
                 
                         To show the current task list, enter 'list'.
                         To mark a task as done, enter 'mark [task number]'.
                         To unmark a task, enter 'unmark [task number]'.
                         Enter 'bye' to leave.
                 """
                 + LINE_DIVIDER);

        boolean byeFlag = false;
        while (!byeFlag) {
            input = in.nextLine();

            String[] words = input.trim().split(" ",2);

            String command = parseCommand(words);
            if (command == null) {
                System.out.println(LINE_DIVIDER + "Invalid command entered."
                        + System.lineSeparator() + LINE_DIVIDER);
                continue;
            }

            Task task = parseInput(command, words);
            // if input is missing a required task entry, skip current iteration and proceed to the next
            if (!(command.equals("list") || command.equals("bye")) && (task == null || words.length < 2)) {
                System.out.println(LINE_DIVIDER + "Missing task entry."
                        + System.lineSeparator() + LINE_DIVIDER);
                continue;
            }

            switch (command) {
            case "todo":
            case "deadline":
            case "event":
                addTask(task);
                break;
            case "mark":
            case "unmark":
                int index = Integer.parseInt(task.getDescription());
                changeTaskStatus(index, command.equals("mark"));
                break;
            case "list":
                showList();
                break;
            case "bye":
                byeFlag = true;
                System.out.println(LINE_DIVIDER
                        + "Goodbye, have a nice day."
                        + System.lineSeparator() + LINE_DIVIDER);
                break;
            default: System.out.println(LINE_DIVIDER + "Not sure what you mean."
                    + System.lineSeparator() + LINE_DIVIDER);
            }
        }
    }
}
