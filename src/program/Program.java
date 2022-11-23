package program;

import grammar.Grammar;
import grammar.GrammarFactory;
import grammar.GrammarFactoryException;
import menu.Menu;
import menu.MenuItem;
import models.NonTerminal;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Objects;

public class Program {
    private final Menu mainMenu;
    private final Menu printMenu;

    public Grammar grammar;

    public Program() {
        mainMenu = new Menu(Arrays.asList(
                new MenuItem("read", "Read Grammar", this::readGrammar),
                new MenuItem("print", "Print Grammar", this::printGrammar),
                new MenuItem("check", "CFG check", this::checkCFG),
                new MenuItem("stop", "Stop program", this::stop)
        ));
        printMenu = new Menu(Arrays.asList(
                new MenuItem("non-term", "Print non-terminals", this::printNonTerminals),
                new MenuItem("term", "Print terminals", this::printTerminals),
                new MenuItem("prod", "Print productions", this::printProductions),
                new MenuItem("prod-for", "Print productions of non-terminal", this::printProductionsFor),
                new MenuItem("back", "Go back", this::goBack)
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
        if (Objects.isNull(grammar)) {
            mainMenu.printLine("No FA provided");
            return;
        }
        printMenu.run();
    }

    private void printNonTerminals() {
        printMenu.printLine(grammar.nonTerminals().toString());
    }

    private void printTerminals() {
        printMenu.printLine(grammar.terminals().toString());
    }

    private void printProductions() {
        printMenu.printLine(grammar.productions().toString());
    }

    private void printProductionsFor() {
        printMenu.printLine("Input non-terminal: ");
        final String string = printMenu.readLine();
        final NonTerminal nonTerminal = new NonTerminal(string);
        if (!grammar.nonTerminals().contains(nonTerminal)) {
            printMenu.printLine("Invalid non-terminal");
            return;
        }

        printMenu.printLine(grammar.productionsFor(nonTerminal).toString());
    }

    private void goBack() {
        printMenu.setRunning(false);
    }

    private void checkCFG() {
        mainMenu.printLine("Not implemented");
    }

    private void stop() {
        mainMenu.setRunning(false);
    }
}
