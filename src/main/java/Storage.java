import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Handles loading tasks from and saving tasks to the storage file.
 */
public class Storage {
    private final Path path;

    /**
     * Creates a storage object using the given relative file path.
     *
     * @param relativePath Relative path to the storage file.
     */
    public Storage(String relativePath) {
        this.path = Paths.get(relativePath);
    }

    /**
     * Loads tasks from the storage file into the given task list.
     *
     * @param taskList Task list to populate.
     * @throws NyeashException If the file cannot be read.
     */
    public void load(TaskList taskList) throws NyeashException {
        if (!Files.exists(path)) {
            return;
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(path);
        } catch (IOException e) {
            throw new NyeashException("Couldn't read save file: " + e.getMessage());
        }

        for (String line : lines) {
            if (line == null) {
                continue;
            }
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }

            try {
                Task t = parse(line);
                if (t != null && !taskList.isFull()) {
                    taskList.add(t);
                }
            } catch (Exception ignored) {
                // corrupted line -> skip
            }
        }
    }

    /**
     * Saves all tasks from the given task list into the storage file.
     *
     * @param taskList Task list to save.
     * @throws NyeashException If the file cannot be written.
     */
    public void save(TaskList taskList) throws NyeashException {
        try {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < taskList.size(); i++) {
                sb.append(format(taskList.get(i))).append(System.lineSeparator());
            }

            Files.writeString(path, sb.toString(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new NyeashException("Couldn't save tasks: " + e.getMessage());
        }
    }

    /**
     * Converts a task into its file storage format.
     *
     * @param t Task to format.
     * @return String representation suitable for saving to file.
     */
    private String format(Task t) {
        String done = t.isDone() ? "1" : "0";
        String desc = safe(t.getDescription());

        if (t instanceof Todo) {
            return "T | " + done + " | " + desc;
        }
        if (t instanceof Deadline d) {
            return "D | " + done + " | " + desc + " | " + safe(d.getBy());
        }
        if (t instanceof Event e) {
            return "E | " + done + " | " + desc + " | " + safe(e.getFrom()) + " | " + safe(e.getTo());
        }
        return "T | " + done + " | " + desc;
    }

    /**
     * Parses a line from the storage file into a task object.
     *
     * @param line One line from the storage file.
     * @return Parsed task, or null if the line is invalid.
     */
    private Task parse(String line) {
        String[] p = line.split("\\s*\\|\\s*");
        if (p.length < 3) {
            return null;
        }

        String type = p[0];
        boolean done = "1".equals(p[1]);
        String desc = p[2];

        Task t;
        switch (type) {
        case "T":
            t = new Todo(desc);
            break;
        case "D":
            if (p.length < 4) {
                return null;
            }
            t = new Deadline(desc, p[3]);
            break;
        case "E":
            if (p.length < 5) {
                return null;
            }
            t = new Event(desc, p[3], p[4]);
            break;
        default:
            return null;
        }

        if (done) {
            t.markAsDone();
        }
        return t;
    }

    /**
     * Replaces reserved file separator characters in a string.
     *
     * @param s Input string.
     * @return Safe string for storage.
     */
    private String safe(String s) {
        return s == null ? "" : s.replace("|", "/");
    }
}