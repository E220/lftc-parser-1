package grammar;

import models.NonTerminal;
import models.Production;
import models.Terminal;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record Grammar(
        Set<Terminal> terminals,
        Set<NonTerminal> nonTerminals,
        NonTerminal startingSymbol,
        List<Production> productions
) {
    public List<Production> productionsFor(NonTerminal nonTerminal) {
        return productions.stream()
                .filter(production -> production.lhs().contains(nonTerminal))
                .collect(Collectors.toList());
    }
}
