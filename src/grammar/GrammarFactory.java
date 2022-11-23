package grammar;

import models.NonTerminal;
import models.Production;
import models.Symbol;
import models.Terminal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GrammarFactory {
    public static Grammar fromFile(String filename) throws FileNotFoundException {
        try(final Scanner scanner = new Scanner(new File(filename))) {
            final Set<NonTerminal> nonTerminals = readNonTerminals(scanner);
            final Set<Terminal> terminals = readTerminals(scanner, nonTerminals);
            final NonTerminal startingSymbol = readStartingSymbol(scanner, nonTerminals);

            final List<Production> productions = new LinkedList<>();
            while(scanner.hasNextLine()) {
                productions.add(readProduction(scanner, nonTerminals, terminals));
            }
            return new Grammar(terminals, nonTerminals, startingSymbol, productions);
        }
    }

    private static NonTerminal readStartingSymbol(Scanner scanner, Set<NonTerminal> nonTerminals) {
        final String string = scanner.nextLine().split("\\s")[0];
        if (!nonTerminals.contains(new NonTerminal(string))) {
            throw new GrammarFactoryException("Starting symbol is not non-terminal");
        }
        return new NonTerminal(string);
    }

    private static Set<NonTerminal> readNonTerminals(Scanner scanner) {
        return Stream.of(scanner.nextLine().split("\\s"))
                .map(NonTerminal::new)
                .collect(Collectors.toSet());
    }

    private static Set<Terminal> readTerminals(Scanner scanner, Set<NonTerminal> nonTerminals) {
        return Stream.of(scanner.nextLine().split("\\s"))
                .peek(string -> {
                    if (nonTerminals.contains(new NonTerminal(string))) {
                        throw new GrammarFactoryException("Terminal is also non-terminal: " + string);
                    }
                })
                .map(Terminal::new)
                .collect(Collectors.toSet());
    }

    private static Production readProduction(Scanner scanner, Set<NonTerminal> nonTerminals, Set<Terminal> terminals) {
        final String[] sides = scanner.nextLine().split("->");
        final List<Symbol> lhs = parseProductionSide(sides[0], nonTerminals, terminals);
        final List<Symbol> rhs = parseProductionSide(sides[1], nonTerminals, terminals);
        return new Production(lhs, rhs);
    }

    private static List<Symbol> parseProductionSide(String side, Set<NonTerminal> nonTerminals, Set<Terminal> terminals) {
        return Stream.of(side.strip().split("\\s"))
                .peek(string -> {
                    if (!nonTerminals.contains(new NonTerminal(string)) && !terminals.contains(new Terminal(string))) {
                        throw new GrammarFactoryException("Unknown symbol in production: " + string);
                    }
                })
                .map(string -> nonTerminals.contains(new NonTerminal(string)) ?
                        new NonTerminal(string) :
                        new Terminal(string)
                )
                .collect(Collectors.toList());
    }
}
