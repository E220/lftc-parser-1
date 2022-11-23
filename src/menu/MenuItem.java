package menu;

public class MenuItem {

    private final String key;
    private final String title;
    private final Runnable command;

    public MenuItem(String key, String title, Runnable command) {
        this.key = key;
        this.title = title;
        this.command = command;
    }

    public String getKey() {
        return key;
    }

    public void run() {
        command.run();
    }

    @Override
    public String toString() {
        return key + " - " + title;
    }
}
