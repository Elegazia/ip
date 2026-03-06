/**
 * Represents a deadline task.
 */
public class Deadline extends Task {
    private final String by;

    /**
     * Creates a deadline task with a description and deadline.
     *
     * @param description Description of the deadline task.
     * @param by Deadline of the task.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the type icon for a deadline task.
     *
     * @return Deadline type icon.
     */
    @Override
    public String getTypeIcon() {
        return "[D]";
    }

    /**
     * Returns a formatted string representation of the deadline task.
     *
     * @return Formatted deadline task string.
     */
    @Override
    public String toString() {
        return "[D][" + getStatusIcon() + "] " + description + " (by: " + by + ")";
    }

    /**
     * Returns the deadline time.
     *
     * @return Deadline string.
     */
    public String getBy() {
        return by;
    }
}