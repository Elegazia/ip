import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Storage {
    private final Path path;

    public Storage(String relativePath) {
        this.path = Paths.get(relativePath); // e.g. "data/nyeash.txt"
    }

    public void load(TaskList taskList) throws NyeashException {
        if (!Files.exists(path)) return; // first run: no file yet

        List<String> lines;
        try {
            lines = Files.readAllLines(path);
        } catch (IOException e) {
            throw new NyeashException("Couldn't read save file: " + e.getMessage());
        }

        for (String line : lines) {
            if (line == null) continue;
            line = line.trim();
            if (line.isEmpty()) continue;

            try {
                Task t = parse(line);
                if (t != null && !taskList.isFull()) {
                    taskList.add(t);
                }
            } catch (Exception ignored) {
                // corrupted line -> skip (stretch goal)
            }
        }
    }

    public void save(TaskList taskList) throws NyeashException {
        try {
            Path parent = path.getParent();
            if (parent != null) Files.createDirectories(parent); // folder may not exist

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

    // Format:
    // T | 1 | read book
    // D | 0 | return book | June 6th
    // E | 0 | meeting | 2pm | 4pm
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

    private Task parse(String line) {
        String[] p = line.split("\\s*\\|\\s*");
        if (p.length < 3) return null;

        String type = p[0];
        boolean done = "1".equals(p[1]);
        String desc = p[2];

        Task t;
        switch (type) {
        case "T":
            t = new Todo(desc);
            break;
        case "D":
            if (p.length < 4) return null;
            t = new Deadline(desc, p[3]);
            break;
        case "E":
            if (p.length < 5) return null;
            t = new Event(desc, p[3], p[4]);
            break;
        default:
            return null;
        }

        if (done) t.markAsDone();
        return t;
    }

    private String safe(String s) {
        return s == null ? "" : s.replace("|", "/"); // don’t break file format
    }
}