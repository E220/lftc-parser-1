package models;

public record NonTerminal(String string) implements Atom {
    @Override
    public String getString() {
        return string;
    }
}
