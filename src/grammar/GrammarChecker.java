package grammar;

import models.Production;
import models.Symbol;

import java.util.List;

public class GrammarChecker {
    public static boolean checkCFG(Grammar grammar) {
        return grammar.productions().stream()
                .map(Production::lhs)
                .flatMap(List::stream)
                .allMatch(Symbol::isNonTerminal);
    }
}
