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
    public boolean isTerminal() {
        return false;
    }

    @Override
    public boolean isNonTerminal() {
        return true;
    }

    @Override
    public String toString() {
        return string;
    }
}
