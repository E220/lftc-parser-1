package parser.operations;

import models.Terminal;
import parser.Parser;

import java.util.List;

@FunctionalInterface
public interface ParserCondition {
    boolean met(Parser parser, List<Terminal> input, int position);
}
