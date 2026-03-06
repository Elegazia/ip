/**
 * Represents an event task.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an event task with a description, start time, and end time.
     *
     * @param description Description of the event.
     * @param from Start time of the event.
     * @param to End time of the event.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the type icon for an event task.
     *
     * @return Event type icon.
     */
    @Override
    public String getTypeIcon() {
        return "[E]";
    }

    /**
     * Returns a formatted string representation of the event task.
     *
     * @return Formatted event task string.
     */
    @Override
    public String toString() {
        return "[E][" + getStatusIcon() + "] " + description + " (from: " + from + " to: " + to + ")";
    }

    /**
     * Returns the start time of the event.
     *
     * @return Start time string.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns the end time of the event.
     *
     * @return End time string.
     */
    public String getTo() {
        return to;
    }
}