public class Parser {

    public static String getCommandWord(String input) throws NyeashException {
        String trimmed = input == null ? "" : input.trim();
        if (trimmed.isEmpty()) {
            throw new NyeashException("You didn't type anything... feed me a command :(");
        }

        String[] parts = trimmed.split("\\s+");
        return parts[0].toLowerCase();
    }

    public static int parseTaskIndex(String input, String commandWord) throws NyeashException {
        String[] parts = splitInput(input);

        if (parts.length != 2) {
            throw new NyeashException("Usage: " + commandWord + " <task number>");
        }
        if (!isInteger(parts[1])) {
            throw new NyeashException("Task number must be an integer!!");
        }

        return Integer.parseInt(parts[1]) - 1;
    }

    public static Todo parseTodo(String input) throws NyeashException {
        String[] parts = splitInput(input);

        if (parts.length < 2) {
            throw new NyeashException("I need something for the todo!");
        }

        String desc = concatTokens(parts, 1, parts.length).trim();
        if (desc.isEmpty()) {
            throw new NyeashException("I need something for the todo!");
        }

        return new Todo(desc);
    }

    public static Deadline parseDeadline(String input) throws NyeashException {
        String[] parts = splitInput(input);
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

    public static Event parseEvent(String input) throws NyeashException {
        String[] parts = splitInput(input);
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

    private static String[] splitInput(String input) throws NyeashException {
        String trimmed = input == null ? "" : input.trim();
        if (trimmed.isEmpty()) {
            throw new NyeashException("You didn't type anything... feed me a command :(");
        }
        return trimmed.split("\\s+");
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