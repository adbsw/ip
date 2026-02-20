package green.ui;

import java.util.ArrayList;
import java.util.Scanner;

import green.task.Deadline;
import green.task.Event;
import green.task.Task;
import green.task.ToDo;

public class Green {
    private static ArrayList<Task> tasks = new ArrayList<>();
    private static final String LINE_DIVIDER = "--------------------------------------------------------------";
    private static final String OPENING = LINE_DIVIDER + System.lineSeparator();
    private static final String CLOSING = System.lineSeparator() + LINE_DIVIDER + System.lineSeparator();

    /** Adds task to current list */
    public static void addTask(Task newTask) {
        if (tasks.contains(newTask)) {
            System.out.println(OPENING +
                    "Failed to add '" + newTask.getDescription() + "'. It is already in the list."
                    + CLOSING);
            return;
        }

        tasks.add(newTask);
        System.out.println(OPENING + "'" + newTask.getDescription() + "' has been added." + CLOSING);
    }

    public static void deleteTask(Task task) {
        String description = task.getDescription();
        int listNum = tasks.indexOf(task) + 1;

        tasks.remove(task);
        System.out.println(OPENING
                + "'" + listNum + ". " + description + "'" + " has been removed from the list."
                + CLOSING);
    }

    /** Updates task status accordingly to user's request of 'mark' or 'unmark' */
    public static void changeTaskStatus(Task task, boolean status) {
        int listNum = tasks.indexOf(task) + 1;
        if (task.isDone() == status) {
            System.out.println(OPENING
                    + "'" + listNum + ". " + task.getDescription() + "'" + " was previously "
                    + (status ? "marked done." : "unmarked.")
                    + CLOSING);
            return;
            }

        task.setDone(status);
        System.out.println(OPENING
                + "'" + listNum + ". " + task.getDescription() + "'" + " has been "
                + (status ? "marked done." : "unmarked.")
                + CLOSING);
    }

    /** Prints current list of tasks */
    public static void showList() {
        if (tasks.isEmpty()) {
            System.out.println(OPENING + "Your list is empty." + CLOSING);
            return;
        }

        System.out.println(OPENING + "Here is the list of things:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i).toString());
        }
        System.out.println(LINE_DIVIDER + System.lineSeparator());
    }

    public static String parseRequest(String[] words) throws IllegalArgumentException {
        boolean notKnownRequest = !words[0].equals("todo") && !words[0].equals("deadline")
                && !words[0].equals("event") && !words[0].equals("delete")
                && !words[0].equals("mark") && !words[0].equals("unmark")
                && !words[0].equals("list") && !words[0].equals("bye");
        if (notKnownRequest) {
            throw new IllegalArgumentException();
        }
        return words[0];
    }

    public static Task getTaskDetails(String request, String words) throws ArrayIndexOutOfBoundsException {
        String[] details = words.trim().split("/");
        String description = details[0].trim();
        switch (request) {
        case "todo":
            return new ToDo(description);
        case "deadline":
            // Throw exception if user input for 'deadline' is missing a due time.
            if (details.length < 2) {
                throw new ArrayIndexOutOfBoundsException();
            }
            String by = details[1].substring(("by".length())).trim();
            return new Deadline(description, by);
        case "event":
            // Throw exception if user input for 'event' is missing a due time frame.
            if (details.length < 3) {
                throw new ArrayIndexOutOfBoundsException();
            }
            String from = details[1].substring(("from".length())).trim();
            String to = details[2].substring(("to".length())).trim();
            return new Event(description, from, to);
        default:
            return new Task(description);
        }
    }

    /** Extract and return corresponding details and class of task from user input */
    public static Task parseTask(String request, String[] words) throws ArrayIndexOutOfBoundsException {
        boolean noTaskDescrRequiredRequest = request.equals("list") || request.equals("bye");
        if (noTaskDescrRequiredRequest) {
            return null;
        }

        boolean taskNumRequiredRequest = request.equals("delete") || request.equals("mark")
                || request.equals("unmark");
        if (taskNumRequiredRequest) {
            try {
                int index = Integer.parseInt(words[1]) - 1;
                return tasks.get(index);
            } catch (NumberFormatException e) {
                System.out.println(OPENING
                        + "Only integers are accepted as task list number."
                        + CLOSING);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(OPENING
                        + "Task list number is missing. Please indicate one."
                        + CLOSING);
            } catch (IndexOutOfBoundsException e) {
                System.out.println(OPENING
                        + "Task list number does not exist."
                        + CLOSING);
            }
            return null;
        }

        // Throw exception if user input is missing a required task entry.
        if (words.length < 2) {
            throw new ArrayIndexOutOfBoundsException();
        }

        return getTaskDetails(request, words[1]);
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

            String request;
            try {
                request = parseRequest(words);
            } catch (IllegalArgumentException e) {
                System.out.println(OPENING + "Invalid request." + CLOSING);
                continue;
            }

            Task task;
            try {
                task = parseTask(request, words);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(OPENING + "Missing task entry, or missing deadline/event time entries."
                        + CLOSING);
                continue;
            }

            if ((request.equals("delete") || request.equals("mark") || request.equals("unmark"))
                    && task == null) {
                continue;
            }

            switch (request) {
            case "todo":
            case "deadline":
            case "event":
                addTask(task);
                break;
            case "delete":
                deleteTask(task);
                break;
            case "mark":
            case "unmark":
                changeTaskStatus(task, request.equals("mark"));
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
