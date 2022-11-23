package models;

public record Terminal(String string) implements Symbol {
    @Override
    public String getString() {
        return string;
    }
}
