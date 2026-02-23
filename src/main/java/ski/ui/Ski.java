package ski.ui;


import java.util.ArrayList;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import java.util.Scanner;

import ski.task.Deadline;
import ski.task.Event;
import ski.task.Task;
import ski.task.ToDo;

public class Ski {
    private static ArrayList<Task> tasks = new ArrayList<>();
    private static final String LINE_DIVIDER = "--------------------------------------------------------------";
    private static final String OPENING = LINE_DIVIDER + System.lineSeparator();
    private static final String CLOSING = System.lineSeparator() + LINE_DIVIDER + System.lineSeparator();
    private static final String LOGO = """
                     _____   __  __    _____
                    |  _ _|  | |/ /   |_   _|
                    |_ _ _   |   /      | |
                     _ _  |  | |\\ \\   __| |__
                    |_____|  |_| \\_\\ |_______|
                """;
    private static final String GREETINGS = """
                 Welcome. What would you like to do today?

                         To add a new todo, deadline  or event, enter:
                         - 'todo [task name]'
                         - 'deadline [task name] /by [date/time]'
                         - 'event [task name] /from [date/time] /to [date/time]'

                         To show the current task list, enter 'list'.
                         To mark a task as done, enter 'mark [task list number]'.
                         To unmark a task, enter 'unmark [task list number]'.
                         To delete a task, enter 'delete [task list number]'.
                         Enter 'bye' to leave.
                 """;

    private static void printGreetings() {
        System.out.println(OPENING + "Greetings from");
        System.out.println(LOGO);
        System.out.println(GREETINGS);
    }

    /** Prints task list from file to terminal */
    private static void printFileContents(String filePath) throws FileNotFoundException {
        File f = new File(filePath);
        Scanner s = new Scanner(f);
        while (s.hasNext()) {
            String[] details = s.nextLine().split("\\|");
            String type = details[0].trim();
            String status = details[1].trim();
            String description = details[2].trim();
            Task task = new Task(description);
            switch (type) {
            case "T":
                task = new ToDo(description);
                break;
            case "D":
                String by = details[3].trim();
                task = new Deadline(description, by);
                break;
            case "E":
                String from = details[3].trim();
                String to = details[4].trim();
                task = new Event(description, from, to);
                break;
            default:
                break;
            }
            tasks.add(task);
            task.setDone(Objects.equals(status, "/"));
        }
        s.close();
        showList();
    }

    /** Writes task list to file */
    private static void writeToFile(String filePath) throws IOException {
        FileWriter fw = new FileWriter(filePath);
        for (int i = 0; i < tasks.size(); i++) {
            fw.write(tasks.get(i).stringToFile());
            if (i != tasks.size() - 1) {
                fw.write(System.lineSeparator());
            }
        }
        fw.close();
    }

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

    /** Deletes task from list*/
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

    /** Returns request word from user input */
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
    
    /** Returns details of task depending on task type */
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

    /** Checks if request requires user-provided task details, task list number, or none,
     * and extracts and returns accordingly from user input */
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

        printGreetings();

        Path pathList = Paths.get("./data/list.txt");
        Path parentDir = pathList.getParent();
        try {
            if (parentDir != null) {
                boolean parentDirExists = Files.exists(parentDir);
                if (!parentDirExists) {
                    System.out.print(OPENING);
                    System.out.println("data/ does not exist.");
                    Files.createDirectories(parentDir);
                    System.out.println("data/ created." + CLOSING);
                }

                boolean fileExists = Files.exists(pathList);
                if (!fileExists) {
                    System.out.print(OPENING);
                    System.out.println("data/list.txt does not exist.");
                    Files.createFile(pathList);
                    System.out.println("data/list.txt created." + CLOSING);
                }
            } else {
                System.out.println(OPENING + "Path does not have a parent." + CLOSING);
            }

        } catch (IOException e) {
            System.out.println(OPENING +
                    "Failed to create directory(ies)/file for list.txt: " + e.getMessage()
                    + CLOSING);
        }

        String tempFile = "./temp/temp.txt";
        Path pathTemp = Paths.get(tempFile);
        Path parentTempDir = pathTemp.getParent();
        try {
            if (parentTempDir != null) {
                boolean parentTempDirExists = Files.exists(parentTempDir);
                if (!parentTempDirExists) {
                    System.out.print(OPENING);
                    System.out.println("temp/ does not exist.");
                    Files.createDirectories(parentTempDir);
                    System.out.println("temp/ created." + CLOSING);
                }
            }
        } catch (IOException e) {
            System.out.println(OPENING
                    + "Failed to create directory(ies) for temp.txt: " + e.getMessage()
                    + CLOSING);
        }

        try {
            Files.copy(pathList, pathTemp, REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println(OPENING
                    + "Copying list.txt to temp.txt went wrong: " + e.getMessage()
                    + CLOSING);
        }

        try {
            printFileContents(tempFile);
        } catch (FileNotFoundException e) {
            System.out.println(OPENING + "File not found" + CLOSING);
        }

        boolean isByeRequest = false;
        while (!isByeRequest) {
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

                try {
                    writeToFile(tempFile);
                } catch (IOException e) {
                    System.out.println(OPENING + "Writing to file went wrong after adding task: "
                            + e.getMessage() + CLOSING);
                }
                break;
            case "delete":
                deleteTask(task);

                try {
                    writeToFile(tempFile);
                } catch (IOException e) {
                    System.out.println(OPENING + "Writing to file went wrong after deletion: "
                            + e.getMessage() + CLOSING);
                }
                break;
            case "mark":
            case "unmark":
                changeTaskStatus(task, request.equals("mark"));

                try {
                    writeToFile(tempFile);
                } catch (IOException e) {
                    System.out.println(OPENING + "Writing to file went wrong after changing task status: "
                            + e.getMessage() + CLOSING);
                }
                break;
            case "list":
                showList();
                break;
            case "bye":
                System.out.println(OPENING + "Goodbye, have a nice day." + CLOSING);
                try {
                    Files.copy(pathTemp, pathList, REPLACE_EXISTING);
                } catch (IOException e) {
                    System.out.println(OPENING + "Copying temp.txt to list.txt went wrong: " + e.getMessage()
                            + CLOSING);
                    break;
                }

                try {
                    Files.delete(pathTemp);
                } catch (NoSuchFileException e) {
                    System.out.println(OPENING + "No such file found: " + e.getMessage() + CLOSING);
                    break;
                } catch (DirectoryNotEmptyException e) {
                    System.out.println(OPENING + "Directory not empty: " + e.getMessage() + CLOSING);
                    break;
                } catch (IOException e) {
                    System.out.println(OPENING + "Something went wrong: " + e.getMessage() + CLOSING);
                    break;
                } catch (SecurityException e) {
                    System.out.println(OPENING + "Insufficient permission to delete file: " + e.getMessage()
                            + CLOSING);
                    break;
                }
                isByeRequest = true;
                break;
            default:
                System.out.println(OPENING + "Not sure what you mean." + CLOSING);
            }
        }
    }
}
