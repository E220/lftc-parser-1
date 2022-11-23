package grammar;

import models.NonTerminal;
import models.Production;
import models.Terminal;

import java.util.List;
import java.util.Set;

public record Grammar(
        Set<Terminal> terminals,
        Set<NonTerminal> nonTerminals,
        NonTerminal startingSymbol,
        List<Production> productions
) {
}
