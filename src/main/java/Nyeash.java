public class Nyeash {
    private static final Ui ui = new Ui();

    private static boolean isInteger(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static int findIndex(String[] arr, String target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(target)) {
                return i;
            }
        }
        return -1;
    }

    private static String concatTokens(String[] arr, int startInclusive, int endExclusive) {
        String result = "";
        for (int i = startInclusive; i < endExclusive; i++) {
            if (!result.equals("")) {
                result += " ";
            }
            result += arr[i];
        }
        return result;
    }

    private static void handleInput(String input, TaskList taskList, Storage storage) throws NyeashException {
        if (input.isEmpty()) {
            throw new NyeashException("You didn't type anything... feed me a command :(");
        }

        String[] parts = input.split("\\s+");
        String cmd = parts[0].toLowerCase();

        if (cmd.equals("bye")) {
            return;
        }

        if (cmd.equals("list")) {
            ui.showList(taskList);
            return;
        }

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
                storage.save(taskList);
                ui.showMarked(taskList.get(idx));
            } else {
                taskList.get(idx).markAsNotDone();
                storage.save(taskList);
                ui.showUnmarked(taskList.get(idx));
            }
            return;
        }

        if (cmd.equals("delete")) {
            if (parts.length != 2) {
                throw new NyeashException("Usage: delete <task number>");
            }
            if (!isInteger(parts[1])) {
                throw new NyeashException("Task number must be an integer!!");
            }

            int idx = Integer.parseInt(parts[1]) - 1;
            if (idx < 0 || idx >= taskList.size()) {
                throw new NyeashException("That task number doesn't exist in MY WORLD!");
            }

            Task removed = taskList.remove(idx);
            storage.save(taskList);
            ui.showTaskDeleted(removed, taskList.size());
            return;
        }

        if (cmd.equals("todo")) {
            if (taskList.isFull()) {
                throw new NyeashException("I'M TOO FULL... (max 100 tasks)");
            }
            if (parts.length < 2) {
                throw new NyeashException("The description of a todo cannot be empty.");
            }

            String description = concatTokens(parts, 1, parts.length).trim();
            if (description.isEmpty()) {
                throw new NyeashException("The description of a todo cannot be empty.");
            }

            Task t = new Todo(description);
            taskList.add(t);
            storage.save(taskList);
            ui.showTaskAdded(t, taskList.size());
            return;
        }

        if (cmd.equals("deadline")) {
            if (taskList.isFull()) {
                throw new NyeashException("I'M TOO FULL... (max 100 tasks)");
            }

            int byIndex = findIndex(parts, "/by");
            if (byIndex == -1) {
                throw new NyeashException("Use: deadline <description> /by <time>");
            }
            if (byIndex == 1) {
                throw new NyeashException("The description of a deadline cannot be empty.");
            }
            if (byIndex == parts.length - 1) {
                throw new NyeashException("The /by time cannot be empty.");
            }

            String description = concatTokens(parts, 1, byIndex).trim();
            String by = concatTokens(parts, byIndex + 1, parts.length).trim();

            if (description.isEmpty()) {
                throw new NyeashException("The description of a deadline cannot be empty.");
            }
            if (by.isEmpty()) {
                throw new NyeashException("The /by time cannot be empty.");
            }

            Task t = new Deadline(description, by);
            taskList.add(t);
            storage.save(taskList);
            ui.showTaskAdded(t, taskList.size());
            return;
        }

        if (cmd.equals("event")) {
            if (taskList.isFull()) {
                throw new NyeashException("I'M TOO FULL... (max 100 tasks)");
            }

            int fromIndex = findIndex(parts, "/from");
            int toIndex = findIndex(parts, "/to");

            if (fromIndex == -1 || toIndex == -1 || fromIndex >= toIndex) {
                throw new NyeashException("Use: event <description> /from <start> /to <end>");
            }
            if (fromIndex == 1) {
                throw new NyeashException("The description of an event cannot be empty.");
            }
            if (fromIndex == parts.length - 1 || toIndex == parts.length - 1) {
                throw new NyeashException("The /from or /to time cannot be empty.");
            }

            String description = concatTokens(parts, 1, fromIndex).trim();
            String from = concatTokens(parts, fromIndex + 1, toIndex).trim();
            String to = concatTokens(parts, toIndex + 1, parts.length).trim();

            if (description.isEmpty()) {
                throw new NyeashException("The description of an event cannot be empty.");
            }
            if (from.isEmpty() || to.isEmpty()) {
                throw new NyeashException("The /from or /to time cannot be empty.");
            }

            Task t = new Event(description, from, to);
            taskList.add(t);
            storage.save(taskList);
            ui.showTaskAdded(t, taskList.size());
            return;
        }

        throw new NyeashException(
                "I'm not sure what that means. Try: todo, deadline, event, list, mark, unmark, delete, bye");
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


        ui.showWelcome(logo);

        TaskList taskList = new TaskList(100);
        Storage storage = new Storage("data/nyeash.txt");

        try {
            storage.load(taskList);
        } catch (NyeashException e) {
            ui.showLoadingError(e.getMessage());
        }

        while (true) {
            String input = ui.readCommand();

            if (input.equalsIgnoreCase("bye")) {
                ui.showGoodbye();
                break;
            }

            try {
                handleInput(input, taskList, storage);
            } catch (NyeashException e) {
                ui.showError(e.getMessage());
            }
        }

        ui.close();
    }
}

