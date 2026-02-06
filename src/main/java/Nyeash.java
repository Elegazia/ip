import java.util.Scanner;

public class Nyeash {
    private static final String LINE = "_".repeat(60);

    private static void printBox(String message) {
        System.out.println(LINE);
        System.out.println(message);
        System.out.println(LINE);
    }

    // ===================== Level 4 Task Types (Inheritance) =====================

    public static class Task {
        protected String description;
        protected boolean isDone;

        public Task(String description) {
            this.description = description;
            this.isDone = false;
        }

        public String getStatusIcon() {
            return (isDone ? "X" : " ");
        }

        public void markAsDone() {
            this.isDone = true;
        }

        public void markAsNotDone() {
            this.isDone = false;
        }

        // for polymorphic display
        public String getTypeIcon() {
            return "";
        }

        @Override
        public String toString() {
            return getTypeIcon() + "[" + getStatusIcon() + "] " + description;
        }
    }

    public static class Todo extends Task {
        public Todo(String description) {
            super(description);
        }

        @Override
        public String getTypeIcon() {
            return "[T]";
        }
    }

    public static class Deadline extends Task {
        protected String by;

        public Deadline(String description, String by) {
            super(description);
            this.by = by;
        }

        @Override
        public String getTypeIcon() {
            return "[D]";
        }

        @Override
        public String toString() {
            return "[D][" + getStatusIcon() + "] " + description + " (by: " + by + ")";
        }
    }

    public static class Event extends Task {
        protected String from;
        protected String to;

        public Event(String description, String from, String to) {
            super(description);
            this.from = from;
            this.to = to;
        }

        @Override
        public String getTypeIcon() {
            return "[E]";
        }

        @Override
        public String toString() {
            return "[E][" + getStatusIcon() + "] " + description + " (from: " + from + " to: " + to + ")";
        }
    }

    // ===================== Helpers =====================

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

    // uses only + concatenation
    private static String concatTokens(String[] arr, int startInclusive, int endExclusive) {
        String result = "";
        for (int i = startInclusive; i < endExclusive; i++) {
            if (!result.equals("")) result += " ";
            result += arr[i];
        }
        return result;
    }

    // ===================== Main =====================

    public static void main(String[] args) {
        String logo ="⠀⠀⣠⣴⣶⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣶⣄⡀⠀\n"
                + "⠀⣸⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡄\n"
                + "⣸⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷\n"
                + "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n"
                + "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n"
                + "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n"
                + "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⠿⠿⠿⠿⠿⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n"
                + "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠟⣴⣿⣿⣿⣿⣿⣿⣶⣍⠻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n"
                + "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠃⠎⠋⠿⣿⣿⣿⣿⣿⣿⣿⠷⣌⠻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n"
                + "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠟⠰⠀⠀⢀⢀⣼⣿⣿⣿⣿⣿⣟⠀⠀⠑⠙⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n"
                + "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⢡⣄⡀⠀⣨⣾⣿⣿⣿⣿⣿⣿⣿⣷⣐⢀⣸⣎⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n"
                + "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⢋⣼⣿⣿⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣆⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n"
                + "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⢡⣿⣿⣿⣿⣿⣿⣿⡿⣫⣬⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣆⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n"
                + "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⢏⣴⣿⣿⣿⣿⣿⣿⣿⣿⠸⢿⣻⡆⠻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣆⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿\n"
                + "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⢟⣵⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣦⣥⣶⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣆⢻⣿⣿⣿⣿⣿⣿⣿⣿\n"
                + "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⢣⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡙⣿⣿⣿⣿⣿⣿⣿⠻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣧⡻⣿⣿⣿⣿⣿⣿⣿\n"
                + "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⢏⣴⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⡌⢙⣛⠛⢛⣛⢁⣼⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⡘⣿⣿⣿⣿⣿⣿\n"
                + "⣿⣿⣿⣿⣿⣿⣿⣿⡿⢣⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡇⣾⡿⣆⣻⣿⡘⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⠙⣿⣿⣿⣿⣿\n"
                + "⣿⣿⣿⣿⣿⣿⣿⣿⢇⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣧⣜⠿⣿⣿⡿⢃⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣦⡹⣿⣿⣿⣿\n"
                + "⣿⣿⣿⣿⣿⣿⣿⡏⣼⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣶⣶⣶⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⠸⣿⣿⣿\n"
                + "⣿⣿⣿⣿⣿⣿⡿⢸⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣇⢻⣿⣿\n"
                + "⣿⣿⣿⣿⣿⣿⣃⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡈⣿⣿\n"
                + "⢸⣿⣿⣿⣿⣿⣽⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⢿⣿⡻⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠟⠛⠻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⢹⣿\n"
                + "⠘⢿⣿⣿⣿⣿⢾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣶⣄⠁⢸⣿⣿⣿⣿⣿⣿⣿⣿⣿⣦⣶⣶⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡇⠃\n"
                + "⠀⠀⠙⠿⠿⣿⡇⠹⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡺⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⠛⠁⠀\n";

        System.out.println("Hello from NYEASH!\n" + logo);
        printBox("Hello! I'm NYEASH!\nI AM HUNGRY!!!!");

        Scanner sc = new Scanner(System.in);

        // ===== storage (max 100 tasks) =====
        Task[] tasks = new Task[100];
        int taskCount = 0;

        while (true) {
            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("bye")) {
                printBox("Please bring me more food next time!");
                break;
            }

            if (input.equalsIgnoreCase("list")) {
                if (taskCount == 0) {
                    printBox("NO FOOD HERE... give me tasks.");
                } else {
                    System.out.println(LINE);
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println((i + 1) + "." + tasks[i]);
                    }
                    System.out.println(LINE);
                }
                continue;
            }

            // mark N / unmark N
            String[] parts = input.split("\\s+");
            if (parts.length == 2 && (parts[0].equalsIgnoreCase("mark") || parts[0].equalsIgnoreCase("unmark"))) {
                if (!isInteger(parts[1])) {
                    printBox("Invalid task number.");
                    continue;
                }

                int idx = Integer.parseInt(parts[1]) - 1;
                if (idx < 0 || idx >= taskCount) {
                    printBox("That task number doesn't exist.");
                    continue;
                }

                if (parts[0].equalsIgnoreCase("mark")) {
                    tasks[idx].markAsDone();
                    System.out.println(LINE);
                    System.out.println("Good job on finishing this! NYEASH is very proud of you!");
                    System.out.println("  " + tasks[idx]);
                    System.out.println(LINE);
                } else {
                    tasks[idx].markAsNotDone();
                    System.out.println(LINE);
                    System.out.println("OK, NYEASH unmarked it. Better finish it cause this is above my paygrade!");
                    System.out.println("  " + tasks[idx]);
                    System.out.println(LINE);
                }
                continue;
            }

            // ===== Level 4 add commands =====
            if (taskCount >= 100) {
                printBox("I'M TOO FULL... (max 100 tasks)");
                continue;
            }

            Task newTask;

            // todo <desc>
            if (parts.length >= 2 && parts[0].equalsIgnoreCase("todo")) {
                String desc = concatTokens(parts, 1, parts.length).trim();
                if (desc.isEmpty()) {
                    printBox("Empty todo.");
                    continue;
                }
                newTask = new Todo(desc);

                // deadline <desc> /by <by>
            } else if (parts.length >= 2 && parts[0].equalsIgnoreCase("deadline")) {
                int byIdx = findIndex(parts, "/by");
                if (byIdx == -1) {
                    printBox("Missing /by.");
                    continue;
                }

                String desc = concatTokens(parts, 1, byIdx).trim();
                String by = concatTokens(parts, byIdx + 1, parts.length).trim();

                if (desc.isEmpty() || by.isEmpty()) {
                    printBox("Invalid deadline.");
                    continue;
                }

                newTask = new Deadline(desc, by);

                // event <desc> /from <from> /to <to>
            } else if (parts.length >= 2 && parts[0].equalsIgnoreCase("event")) {
                int fromIdx = findIndex(parts, "/from");
                int toIdx = findIndex(parts, "/to");

                if (fromIdx == -1 || toIdx == -1 || toIdx < fromIdx) {
                    printBox("Missing /from or /to.");
                    continue;
                }

                String desc = concatTokens(parts, 1, fromIdx).trim();
                String from = concatTokens(parts, fromIdx + 1, toIdx).trim();
                String to = concatTokens(parts, toIdx + 1, parts.length).trim();

                if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
                    printBox("Invalid event.");
                    continue;
                }

                newTask = new Event(desc, from, to);

            } else {
                // Backwards compatible: plain text becomes a Todo
                newTask = new Todo(input);
            }

            tasks[taskCount] = newTask;
            taskCount++;

            System.out.println(LINE);
            System.out.println("Got it. I've added this task:");
            System.out.println("  " + newTask);
            System.out.println("Now you have " + taskCount + " tasks in the list.");
            System.out.println(LINE);
        }

        sc.close();
    }
}
