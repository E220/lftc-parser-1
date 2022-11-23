package models;

public record Terminal(String string) implements Atom {
    @Override
    public String getString() {
        return string;
    }
}
