package parser.operations;

import parser.ParserState;

public record ParserOperation(ParserState.State ifState, ParserCondition condition, ParserAction action) {}
