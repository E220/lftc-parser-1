package parser.operations;

import parser.Parser;

@FunctionalInterface
public interface ParserAction {
    void execute(Parser parser);
}
