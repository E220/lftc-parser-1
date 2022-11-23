package menu;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private final LinkedHashMap<String, MenuItem> items;
    private boolean running;
    private final Scanner scanner = new Scanner(System.in);

    public Menu(List<MenuItem> items) {
        this.items = new LinkedHashMap<>();
        items.forEach(item -> this.items.put(item.getKey(), item));
    }

    public void run() {
        this.setRunning(true);
        while (this.running) {
            this.items.values().forEach(System.out::println);
            final String input = this.readLine();
            if (this.items.containsKey(input)) {
                this.items.get(input).run();
            }
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public String readLine() {
        return this.scanner.nextLine();
    }

    public void printLine(String string) {
        System.out.println(string);
    }
}
