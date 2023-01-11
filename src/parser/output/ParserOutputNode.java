package parser.output;

import models.Symbol;

public final class ParserOutputNode {
    private final int index;
    private final Symbol symbol;
    private final Integer parentIndex;
    private Integer firstChildIndex;

    public ParserOutputNode(int index, Symbol symbol, Integer parentIndex) {
        this.index = index;
        this.symbol = symbol;
        this.parentIndex = parentIndex;
    }

    public ParserOutputNode(int index, Symbol symbol) {
        this(index, symbol, null);
    }

    @Override
    public String toString() {
        return "{" + index + ", " + symbol + ", " + parentIndex + ", " + firstChildIndex + "}";
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public Integer getParentIndex() {
        return parentIndex;
    }

    public Integer getFirstChildIndex() {
        return firstChildIndex;
    }

    public void setFirstChildIndex(Integer firstChildIndex) {
        this.firstChildIndex = firstChildIndex;
    }
}
