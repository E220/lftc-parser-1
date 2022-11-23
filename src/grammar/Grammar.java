package grammar;

import models.NonTerminal;
import models.Production;
import models.Terminal;

import java.util.List;

public record Grammar(
        List<Terminal> terminals,
        List<NonTerminal> nonTerminals,
        List<Production> productions
) {
}
