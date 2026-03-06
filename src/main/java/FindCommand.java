import java.util.ArrayList;

public class FindCommand extends Command {
    private final String keyword;

    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws NyeashException {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new NyeashException("Usage: find <keyword>");
        }

        ArrayList<Task> matches = taskList.findMatching(keyword.trim());
        ui.showFindResults(matches);
    }
}