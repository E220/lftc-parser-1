package parser.output;

import models.Symbol;

public record ParserOutputNode(int index, Symbol symbol, Integer parentIndex, Integer rightSiblingIndex) {
    public ParserOutputNode(int index, Symbol symbol) {
        this(index, symbol, null, null);
    }

    public ParserOutputNode(int index, Symbol symbol, Integer parentIndex) {
        this(index, symbol, parentIndex, null);
    }
}
