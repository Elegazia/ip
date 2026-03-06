public class TodoCommand extends Command {
    private final Todo todo;

    public TodoCommand(Todo todo) {
        this.todo = todo;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws NyeashException {
        if (taskList.isFull()) {
            throw new NyeashException("I'M TOO FULL... (max 100 tasks)");
        }

        taskList.add(todo);
        storage.save(taskList);
        ui.showTaskAdded(todo, taskList.size());
    }
}