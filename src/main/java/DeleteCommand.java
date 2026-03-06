public class DeleteCommand extends Command {
    private final int index;

    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws NyeashException{
        Task removed = tasks.remove(index);
        storage.save(tasks);
        ui.showTaskDeleted(removed, tasks.size());
    }
}