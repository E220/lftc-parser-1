package program;

import grammar.Grammar;
import grammar.GrammarFactory;
import grammar.GrammarFactoryException;
import menu.Menu;
import menu.MenuItem;

import java.io.FileNotFoundException;
import java.util.Arrays;

public class Program {
    private final Menu mainMenu;

    public Grammar grammar;

    public Program() {
        mainMenu = new Menu(Arrays.asList(
                new MenuItem("read", "Read Grammar", this::readGrammar),
                new MenuItem("print", "Print Grammar", this::printGrammar),
                new MenuItem("check", "CFG check", this::checkCFG),
                new MenuItem("stop", "Stop program", this::stop)
        ));
    }

    public void run() {
        mainMenu.run();
    }

    private void readGrammar() {
        mainMenu.printLine("Input file name");
        final String filename = mainMenu.readLine();
        try {
            this.grammar = GrammarFactory.fromFile("src/" + filename);
        } catch (FileNotFoundException e) {
            mainMenu.printLine("File not found");
        } catch (GrammarFactoryException e) {
            mainMenu.printLine(e.getMessage());
        }
    }

    private void printGrammar() {
        mainMenu.printLine("Not implemented");
    }

    private void checkCFG() {
        mainMenu.printLine("Not implemented");
    }

    private void stop() {
        mainMenu.setRunning(false);
    }
}
