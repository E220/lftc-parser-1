package parser.operations;

import models.Terminal;
import parser.Parser;

@FunctionalInterface
public interface ParserCondition {
    boolean met(Parser parser, Terminal input);
}
