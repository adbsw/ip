import java.util.Scanner;

public class Green {
    private static final int ARRAY_SIZE = 100;
    private static Task[] tasks = new Task[ARRAY_SIZE];
    private static int taskCount = 0;
    private static final String LINE_DIVIDER = "--------------------------------------------------------------";
    private static final String OPENING = LINE_DIVIDER + System.lineSeparator();
    private static final String CLOSING = System.lineSeparator() + LINE_DIVIDER + System.lineSeparator();

    /** Adds task to current list */
    public static void addTask(Task newTask) {
        if (isInList(newTask)) {
            System.out.println(OPENING +
                    "Failed to add '" + newTask.getDescription() + "'. It is already in the list."
                    + CLOSING);
            return;
        }

        if (taskCount >= tasks.length) {
            System.out.println(OPENING + "Unable to add task. List is full." + CLOSING);
            return;
        }

        tasks[taskCount] = newTask;
        taskCount++;
        System.out.println(OPENING + "'" + newTask.getDescription() + "' has been added." + CLOSING);
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
    public static void changeTaskStatus(int listNum, boolean status) {
        if (listNum > taskCount|| listNum == 0) {
            System.out.println(OPENING + "There is no task numbered " + listNum + "." + CLOSING);
            return;
        }

        int index = listNum - 1;
        if (tasks[index].isDone() == status) {
            System.out.println(OPENING
                    + "'" + listNum + ". " + tasks[index].getDescription() + "'" + " was previously "
                    + (status ? "marked done." : "unmarked.")
                    + CLOSING);
            return;
        }

        tasks[index].setDone(status);
        System.out.println(OPENING
                + "'" + listNum + ". " + tasks[index].getDescription() + "'" + " has been "
                + (status ? "marked done." : "unmarked.")
                + CLOSING);
    }

    /** Prints current list of tasks */
    public static void showList() {
        if (taskCount == 0) {
            System.out.println(OPENING + "Your list is empty." + CLOSING);
            return;
        }

        System.out.println(OPENING + "Here is the list of things:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + ". " + tasks[i].toString());
        }
        System.out.println(LINE_DIVIDER + System.lineSeparator());
    }

    public static String parseCommand(String[] words) {
        return switch (words[0]) {
            case "todo", "deadline", "event", "mark", "unmark", "list", "bye" -> words[0];
            default -> null;
        };
    }

    /** Extract and return corresponding details and class of task from user input */
    public static Task parseTask(String command, String[] words) {
        if (words.length <= 1) {
            return null;
        }

        String[] details = words[1].trim().split("/");
        String description = details[0].trim();

        switch (command) {
        case "todo":
            return new ToDo(description);
        case "deadline":
            String by = details[1].substring(("by".length())).trim();
            return new Deadline(description, by);
        case "event":
            String from = details[1].substring(("from".length())).trim();
            String to = details[2].substring(("to".length())).trim();
            return new Event(description, from, to);
        default:
            return new Task(description);
        }
    }

    public static void main(String[] args) {
        String input;
        Scanner in = new Scanner(System.in);
        System.out.println(OPENING + "Greetings from");
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
                         - 'deadline [task name] /by [date/time]'
                         - 'event [task name] /from [date/time] /to [date/time]'
                 
                         To show the current task list, enter 'list'.
                         To mark a task as done, enter 'mark [task list number]'.
                         To unmark a task, enter 'unmark [task list number]'.
                         Enter 'bye' to leave.
                 """
                 + CLOSING);

        boolean byeFlag = false;
        while (!byeFlag) {
            input = in.nextLine();

            String[] words = input.trim().split(" ",2);

            String command = parseCommand(words);
            if (command == null) {
                System.out.println(OPENING + "Invalid command entered." + CLOSING);
                continue;
            }

            Task task = parseTask(command, words);

            boolean isStandAloneCommand = command.equals("list") || command.equals("bye");
            boolean isSingleWordInput = task == null || words.length < 2;
            boolean isMissingTask = !isStandAloneCommand && isSingleWordInput;

            if (isMissingTask) {
                System.out.println(OPENING + "Missing task entry." + CLOSING);
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
                int listNum;
                try {
                    listNum = Integer.parseInt(task.getDescription());
                } catch (NumberFormatException e) {
                    System.out.println( OPENING
                            + "You need to indicate a task by its list number to mark/unmark."
                            + CLOSING);
                    break;
                }
                changeTaskStatus(listNum, command.equals("mark"));
                break;
            case "list":
                showList();
                break;
            case "bye":
                byeFlag = true;
                System.out.println(OPENING + "Goodbye, have a nice day." + CLOSING);
                break;
            default:
                System.out.println(OPENING + "Not sure what you mean." + CLOSING);
            }
        }
    }
}
