package models;

public record NonTerminal(String string) implements Symbol {
    @Override
    public String getString() {
        return string;
    }
}
