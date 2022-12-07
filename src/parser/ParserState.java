package parser;

public enum ParserState {
    NORMAL("q"),
    BACK("b"),
    FINAL("f"),
    ERROR("e");

    ParserState(String string) {
        this.string = string;
    }

    private final String string;

    @Override
    public String toString() {
        return string;
    }
}
