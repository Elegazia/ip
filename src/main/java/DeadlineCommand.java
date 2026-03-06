public class DeadlineCommand extends Command {
    private final Deadline deadline;

    public DeadlineCommand(Deadline deadline) {
        this.deadline = deadline;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws NyeashException {
        if (taskList.isFull()) {
            throw new NyeashException("I'M TOO FULL... (max 100 tasks)");
        }

        taskList.add(deadline);
        storage.save(taskList);
        ui.showTaskAdded(deadline, taskList.size());
    }
}