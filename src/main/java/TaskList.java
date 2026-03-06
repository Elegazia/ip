import java.util.ArrayList;

public class TaskList {
    private final int capacity;
    private final ArrayList<Task> tasks;

    public TaskList(int capacity) {
        this.capacity = capacity;
        this.tasks = new ArrayList<>();
    }

    public int size() {
        return tasks.size();
    }

    public boolean isFull() {
        return tasks.size() >= capacity;
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    public void add(Task t) {
        if (isFull()) {
            throw new IllegalStateException("TaskList is full");
        }
        tasks.add(t);
    }

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