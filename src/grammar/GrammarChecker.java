package grammar;

import models.NonTerminal;
import models.Production;

import java.util.List;
import java.util.Set;

public class GrammarChecker {
    public static boolean checkCFG(Grammar grammar) {
        final Set<NonTerminal> nonTerminals = grammar.nonTerminals();
        return grammar.productions().stream()
                .map(Production::lhs)
                .flatMap(List::stream)
                .allMatch(symbol -> nonTerminals.contains(new NonTerminal(symbol)));
    }
}
