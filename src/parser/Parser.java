package parser;

import grammar.Grammar;
import models.Terminal;
import parser.operations.ParserOperation;
import parser.operations.ParserOperations;

import java.util.List;

public record Parser(Grammar grammar, ParserState state) {

    public Parser(Grammar grammar) {
        this(grammar, new ParserState(grammar.startingSymbol()));
    }

    public void parse(List<Terminal> input) {
        final List<ParserOperation> operations = ParserOperations.operations;
        while(state.getState() != ParserState.State.ERROR && state.getState() != ParserState.State.FINAL) {
            for (final ParserOperation operation : operations) {
                if (operation.condition().met(this, input.get(state.getPosition()))) {
                    operation.action().execute(this);
                    break;
                }
            }
        }
    }
}
