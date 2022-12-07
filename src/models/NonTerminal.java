package models;

public record NonTerminal(String string) implements Symbol {

    public NonTerminal(Symbol symbol) {
        this(symbol.toString());
    }

    @Override
    public String getString() {
        return string;
    }

    @Override
    public String toString() {
        return string;
    }
}
