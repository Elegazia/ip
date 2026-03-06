public class UnmarkCommand extends Command {
    private final int index;

    public UnmarkCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws NyeashException {
        if (index < 0 || index >= taskList.size()) {
            throw new NyeashException("That task number doesn't exist in MY WORLD!");
        }

        Task task = taskList.get(index);
        task.markAsNotDone();
        storage.save(taskList);
        ui.showUnmarked(task);
    }
}