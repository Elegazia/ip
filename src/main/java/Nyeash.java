import java.util.Scanner;

public class Nyeash {
    private static final String LINE = "_".repeat(60);

    private static void printBox(String message) {
        System.out.println(LINE);
        System.out.println(message);
        System.out.println(LINE);
    }

    private static boolean isInteger(String s) {
        if (s == null || s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) return false;
        }
        return true;
    }

    private static int findIndex(String[] arr, String target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(target)) return i;
        }
        return -1;
    }

    private static String concatTokens(String[] arr, int startInclusive, int endExclusive) {
        String result = "";
        for (int i = startInclusive; i < endExclusive; i++) {
            if (!result.equals("")) result += " ";
            result += arr[i];
        }
        return result;
    }

    private static void handleInput(String input, TaskList taskList) throws NyeashException {
        if (input.isEmpty()) {
            throw new NyeashException("You didn't type anything... feed me a command :(");
        }

        // Split once here, reuse everywhere
        String[] parts = input.split("\\s+");
        String cmd = parts[0].toLowerCase();

        // bye
        if (cmd.equals("bye")) {
            return;
        }

        // list
        if (cmd.equals("list")) {
            if (taskList.size() == 0) {
                printBox("NO FOOD HERE... give me tasks.");
            } else {
                System.out.println(LINE);
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskList.size(); i++) {
                    System.out.println((i + 1) + "." + taskList.get(i));
                }
                System.out.println(LINE);
            }
            return;
        }

        // mark N / unmark N
        if (cmd.equals("mark") || cmd.equals("unmark")) {
            if (parts.length != 2) {
                throw new NyeashException("Usage: " + cmd + " <task number>");
            }
            if (!isInteger(parts[1])) {
                throw new NyeashException("Task number must be an integer!!");
            }

            int idx = Integer.parseInt(parts[1]) - 1;
            if (idx < 0 || idx >= taskList.size()) {
                throw new NyeashException("That task number doesn't exist in MY WORLD!");
            }

            if (cmd.equals("mark")) {
                taskList.get(idx).markAsDone();
                System.out.println(LINE);
                System.out.println("Good job on finishing this! NYEASH is very proud of you!");
                System.out.println("  " + taskList.get(idx));
                System.out.println(LINE);
            } else {
                taskList.get(idx).markAsNotDone();
                System.out.println(LINE);
                System.out.println("OK, NYEASH unmarked it. Better finish it cause this is above my paygrade!");
                System.out.println("  " + taskList.get(idx));
                System.out.println(LINE);
            }
            return;
        }

        if (taskList.isFull()) {
            throw new NyeashException("I'M TOO FULL... (max 100 tasks)");
        }

        Task newTask;

        // todo <desc>
        if (cmd.equals("todo")) {
            if (parts.length < 2) {
                throw new NyeashException("I need something for the todo!");
            }
            String desc = concatTokens(parts, 1, parts.length).trim();
            if (desc.isEmpty()) {
                throw new NyeashException("I need something for the todo!");
            }
            newTask = new Todo(desc);

        } else if (cmd.equals("deadline")) {
            int byIdx = findIndex(parts, "/by");
            if (byIdx == -1) {
                throw new NyeashException("Deadline must include /by. Example: deadline return book /by Sunday");
            }

            String desc = concatTokens(parts, 1, byIdx).trim();
            String by = concatTokens(parts, byIdx + 1, parts.length).trim();

            if (desc.isEmpty()) {
                throw new NyeashException("Deadline description cannot be empty.");
            }
            if (by.isEmpty()) {
                throw new NyeashException("Deadline time cannot be empty. Use /by <time>.");
            }

            newTask = new Deadline(desc, by);

            // event <desc> /from <from> /to <to>
        } else if (cmd.equals("event")) {
            int fromIdx = findIndex(parts, "/from");
            int toIdx = findIndex(parts, "/to");

            if (fromIdx == -1 || toIdx == -1 || toIdx < fromIdx) {
                throw new NyeashException("Event must include /from and /to. EXAMPLE: event project meeting /from 2pm /to 4pm");
            }

            String desc = concatTokens(parts, 1, fromIdx).trim();
            String from = concatTokens(parts, fromIdx + 1, toIdx).trim();
            String to = concatTokens(parts, toIdx + 1, parts.length).trim();

            if (desc.isEmpty()) throw new NyeashException("Event description cannot be empty.");
            if (from.isEmpty()) throw new NyeashException("Event start time cannot be empty. Use /from <time>.");
            if (to.isEmpty()) throw new NyeashException("Event end time cannot be empty. Use /to <time>.");

            newTask = new Event(desc, from, to);

        } else {
            throw new NyeashException("I'm not sure what that means. Try: todo, deadline, event, list, mark, unmark, bye");
        }

        taskList.add(newTask);

        System.out.println(LINE);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + newTask);
        System.out.println("Now you have " + taskList.size() + " tasks in the list.");
        System.out.println(LINE);
    }

    public static void main(String[] args) {
        String logo = """
                ⠀⠀⣠⣴⣶⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣶⣄.
                ⠀⣸⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡄
                ⣸⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷
                ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
                ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
                ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
                ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⠿⠿⠿⠿⠿⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
                ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠟⣴⣿⣿⣿⣿⣿⣿⣶⣍⠻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
                ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠃⠎⠋⠿⣿⣿⣿⣿⣿⣿⣿⠷⣌⠻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
                ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠟⠰⠀⠀⢀⢀⣼⣿⣿⣿⣿⣿⣟⠀⠀⠑⠙⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
                ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⢡⣄⡀⠀⣨⣾⣿⣿⣿⣿⣿⣿⣿⣷⣐⢀⣸⣎⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
                ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⢋⣼⣿⣿⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣆⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
                ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⢡⣿⣿⣿⣿⣿⣿⣿⡿⣫⣬⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣆⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
                ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⢏⣴⣿⣿⣿⣿⣿⣿⣿⣿⠸⢿⣻⡆⠻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣆⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿
                ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⢟⣵⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣦⣥⣶⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣆⢻⣿⣿⣿⣿⣿⣿⣿⣿
                ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⢣⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡙⣿⣿⣿⣿⣿⣿⣿⠻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣧⡻⣿⣿⣿⣿⣿⣿⣿
                ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⢏⣴⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⡌⢙⣛⠛⢛⣛⢁⣼⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⡘⣿⣿⣿⣿⣿⣿
                ⣿⣿⣿⣿⣿⣿⣿⣿⡿⢣⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡇⣾⡿⣆⣻⣿⡘⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⠙⣿⣿⣿⣿⣿
                ⣿⣿⣿⣿⣿⣿⣿⣿⢇⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣧⣜⠿⣿⣿⡿⢃⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣦⡹⣿⣿⣿⣿
                ⣿⣿⣿⣿⣿⣿⣿⡏⣼⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣶⣶⣶⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⠸⣿⣿⣿
                ⣿⣿⣿⣿⣿⣿⡿⢸⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣇⢻⣿⣿
                ⣿⣿⣿⣿⣿⣿⣃⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡈⣿⣿
                ⢸⣿⣿⣿⣿⣿⣽⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⢿⣿⡻⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠟⠛⠻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⢹⣿
                ⠘⢿⣿⣿⣿⣿⢾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣶⣄⠁⢸⣿⣿⣿⣿⣿⣿⣿⣿⣿⣦⣶⣶⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡇⠃
                ⠀⠀⠙⠿⠿⣿⡇⠹⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡺⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⠛⠁⠀
                """;

        System.out.println("Hello from NYEASH!\n" + logo);
        printBox("Hello! I'm NYEASH!\nI AM HUNGRY!!!!");

        Scanner sc = new Scanner(System.in);
        TaskList taskList = new TaskList(100);

        while (true) {
            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("bye")) {
                printBox("Please bring me more food next time!");
                break;
            }

            try {
                handleInput(input, taskList);
            } catch (NyeashException e) {
                printBox(e.getMessage());
            }
        }

        sc.close();
    }
}
