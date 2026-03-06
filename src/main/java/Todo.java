/**
 * Represents a todo task.
 */
public class Todo extends Task {

    /**
     * Creates a todo task with the given description.
     *
     * @param description Description of the todo task.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the type icon for a todo task.
     *
     * @return Todo type icon.
     */
    @Override
    public String getTypeIcon() {
        return "[T]";
    }
}