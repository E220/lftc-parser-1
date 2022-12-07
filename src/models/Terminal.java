package models;

public record Terminal(String string) implements Symbol {

    public Terminal(Symbol symbol) {
        this(symbol.getString());
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
