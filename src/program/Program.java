package program;

import grammar.Grammar;
import grammar.GrammarChecker;
import grammar.GrammarFactory;
import grammar.GrammarFactoryException;
import menu.Menu;
import menu.MenuItem;
import models.NonTerminal;
import models.Terminal;
import parser.Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Program {
    private final Menu mainMenu;
    private final Menu printMenu;

    public Grammar grammar;

    public Program() {
        mainMenu = new Menu(Arrays.asList(
                new MenuItem("read", "Read Grammar", this::readGrammar),
                new MenuItem("print", "Print Grammar", this::printGrammar),
                new MenuItem("check", "CFG check", this::checkCFG),
                new MenuItem("parse", "Parse input", this::parseInput),
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
            mainMenu.printLine("No grammar provided");
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
        if (Objects.isNull(grammar)) {
            mainMenu.printLine("No grammar provided");
            return;
        }
        mainMenu.printLine(GrammarChecker.checkCFG(grammar) ? "CFG" : "Not CFG");
    }

    private void stop() {
        mainMenu.setRunning(false);
    }

    private void parseInput() {
        if (Objects.isNull(grammar)) {
            mainMenu.printLine("No grammar provided");
            return;
        }
        if (!GrammarChecker.checkCFG(grammar)) {
            mainMenu.printLine("Grammar not CFG");
        }

        mainMenu.printLine("Input file name");
        final String filename = mainMenu.readLine();

        final List<Terminal> input = new ArrayList<>();
        try(final Scanner scanner = new Scanner(new File("src/" + filename))) {
            while(scanner.hasNextLine()) {
                input.addAll(Arrays.stream(scanner.nextLine().split("\\s")).map(Terminal::new).toList());
            }
        } catch (FileNotFoundException e) {
            mainMenu.printLine("File not found");
            return;
        }
        final Parser parser = new Parser(grammar);
        parser.parse(input);
        System.out.println(parser.output());
    }
}
