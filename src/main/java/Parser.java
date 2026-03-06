public class Parser {

    public static Command parse(String input) throws NyeashException {
        String trimmed = input == null ? "" : input.trim();
        if (trimmed.isEmpty()) {
            throw new NyeashException("You didn't type anything... feed me a command :(");
        }

        String[] parts = trimmed.split("\\s+");
        String cmd = parts[0].toLowerCase();

        switch (cmd) {
        case "bye":
            return new ByeCommand();

        case "list":
            return new ListCommand();

        case "mark":
            return new MarkCommand(parseTaskIndex(parts, "mark"));

        case "unmark":
            return new UnmarkCommand(parseTaskIndex(parts, "unmark"));

        case "delete":
            return new DeleteCommand(parseTaskIndex(parts, "delete"));

        case "todo":
            return new TodoCommand(parseTodo(parts));

        case "deadline":
            return new DeadlineCommand(parseDeadline(parts));

        case "event":
            return new EventCommand(parseEvent(parts));

        case "find":
            return new FindCommand(parseFindKeyword(parts));

        default:
            throw new NyeashException(
                    "I'm not sure what that means. Try: todo, deadline, event, list, mark, unmark, delete, find, bye");
        }
    }

    private static int parseTaskIndex(String[] parts, String commandWord) throws NyeashException {
        if (parts.length != 2) {
            throw new NyeashException("Usage: " + commandWord + " <task number>");
        }
        if (!isInteger(parts[1])) {
            throw new NyeashException("Task number must be an integer!!");
        }

        return Integer.parseInt(parts[1]) - 1;
    }

    private static Todo parseTodo(String[] parts) throws NyeashException {
        if (parts.length < 2) {
            throw new NyeashException("I need something for the todo!");
        }

        String desc = concatTokens(parts, 1, parts.length).trim();
        if (desc.isEmpty()) {
            throw new NyeashException("I need something for the todo!");
        }

        return new Todo(desc);
    }

    private static Deadline parseDeadline(String[] parts) throws NyeashException {
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

        return new Deadline(desc, by);
    }

    private static Event parseEvent(String[] parts) throws NyeashException {
        int fromIdx = findIndex(parts, "/from");
        int toIdx = findIndex(parts, "/to");

        if (fromIdx == -1 || toIdx == -1 || toIdx < fromIdx) {
            throw new NyeashException(
                    "Event must include /from and /to. EXAMPLE: event project meeting /from 2pm /to 4pm");
        }

        String desc = concatTokens(parts, 1, fromIdx).trim();
        String from = concatTokens(parts, fromIdx + 1, toIdx).trim();
        String to = concatTokens(parts, toIdx + 1, parts.length).trim();

        if (desc.isEmpty()) {
            throw new NyeashException("Event description cannot be empty.");
        }
        if (from.isEmpty()) {
            throw new NyeashException("Event start time cannot be empty. Use /from <time>.");
        }
        if (to.isEmpty()) {
            throw new NyeashException("Event end time cannot be empty. Use /to <time>.");
        }

        return new Event(desc, from, to);
    }

    private static String parseFindKeyword(String[] parts) throws NyeashException {
        if (parts.length < 2) {
            throw new NyeashException("Usage: find <keyword>");
        }

        String keyword = concatTokens(parts, 1, parts.length).trim();
        if (keyword.isEmpty()) {
            throw new NyeashException("Usage: find <keyword>");
        }

        return keyword;
    }

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
}