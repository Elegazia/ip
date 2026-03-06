import java.util.ArrayList;

/**
 * Represents a list of tasks with a fixed maximum capacity.
 */
public class TaskList {
    private final int capacity;
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list with the given capacity.
     *
     * @param capacity Maximum number of tasks allowed.
     */
    public TaskList(int capacity) {
        this.capacity = capacity;
        this.tasks = new ArrayList<>();
    }

    /**
     * Returns the number of tasks currently in the list.
     *
     * @return Current task count.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns whether the task list has reached capacity.
     *
     * @return True if full, false otherwise.
     */
    public boolean isFull() {
        return tasks.size() >= capacity;
    }

    /**
     * Returns the task at the given index.
     *
     * @param index Index of the task.
     * @return Task at the specified index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Adds a task to the list.
     *
     * @param t Task to add.
     */
    public void add(Task t) {
        if (isFull()) {
            throw new IllegalStateException("TaskList is full");
        }
        tasks.add(t);
    }

    /**
     * Removes and returns the task at the given index.
     *
     * @param index Index of the task to remove.
     * @return The removed task.
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    public ArrayList<Task> findMatching(String keyword) {
        ArrayList<Task> matches = new ArrayList<>();
        String loweredKeyword = keyword.toLowerCase();

        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(loweredKeyword)) {
                matches.add(task);
            }
        }

        return matches;
    }
}