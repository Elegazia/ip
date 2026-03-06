public class EventCommand extends Command {
    private final Event event;

    public EventCommand(Event event) {
        this.event = event;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws NyeashException {
        if (taskList.isFull()) {
            throw new NyeashException("I'M TOO FULL... (max 100 tasks)");
        }

        taskList.add(event);
        storage.save(taskList);
        ui.showTaskAdded(event, taskList.size());
    }
}