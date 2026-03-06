import java.util.ArrayList;
import java.util.Scanner;

public class Ui {
    private static final String LINE = "_".repeat(60);
    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public void showLine() {
        System.out.println(LINE);
    }

    public void showBox(String message) {
        System.out.println(LINE);
        System.out.println(message);
        System.out.println(LINE);
    }

    public void showWelcome(String logo) {
        System.out.println("Hello from NYEASH!\n" + logo);
        showBox("Hello! I'm NYEASH!\nI AM HUNGRY!!!!");
    }

    public void showGoodbye() {
        showBox("Please bring me more food next time!");
    }

    public void showLoadingError(String message) {
        showBox("Couldn't load saved tasks, starting fresh.\n" + message);
    }

    public void showError(String message) {
        showBox(message);
    }

    public String readCommand() {
        return scanner.nextLine().trim();
    }

    public void showList(TaskList taskList) {
        if (taskList.size() == 0) {
            showBox("NO FOOD HERE... give me tasks.");
            return;
        }

        System.out.println(LINE);
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskList.size(); i++) {
            System.out.println((i + 1) + "." + taskList.get(i));
        }
        System.out.println(LINE);
    }

    public void showFindResults(ArrayList<Task> matches) {
        if (matches.isEmpty()) {
            showBox("No matching tasks found.");
            return;
        }

        System.out.println(LINE);
        System.out.println("Here are the matching tasks in your list:");
        for (int i = 0; i < matches.size(); i++) {
            System.out.println((i + 1) + "." + matches.get(i));
        }
        System.out.println(LINE);
    }

    public void showMarked(Task task) {
        System.out.println(LINE);
        System.out.println("Good job on finishing this! NYEASH is very proud of you!");
        System.out.println("  " + task);
        System.out.println(LINE);
    }

    public void showUnmarked(Task task) {
        System.out.println(LINE);
        System.out.println("OK, NYEASH unmarked it. Better finish it cause this is above my paygrade!");
        System.out.println("  " + task);
        System.out.println(LINE);
    }

    public void showTaskAdded(Task task, int size) {
        System.out.println(LINE);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + size + " tasks in the list.");
        System.out.println(LINE);
    }

    public void showTaskDeleted(Task task, int size) {
        System.out.println(LINE);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + size + " tasks in the list.");
        System.out.println(LINE);
    }

    public void close() {
        scanner.close();
    }
}