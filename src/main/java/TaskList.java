public class TaskList {
    private final Task[] tasks;
    private int taskCount;

    public TaskList(int capacity) {
        tasks = new Task[capacity];
        taskCount = 0;
    }

    public int size() {
        return taskCount;
    }

    public boolean isFull() {
        return taskCount >= tasks.length;
    }

    public Task get(int index) {
        return tasks[index];
    }

    public void add(Task t) {
        tasks[taskCount] = t;
        taskCount++;
    }
}
