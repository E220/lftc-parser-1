package parser.operations;

import models.NonTerminal;
import models.Production;
import models.Symbol;
import models.Terminal;
import parser.ParserState;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ParserOperations {

    private static final ParserOperation expand = new ParserOperation(
            ParserState.State.NORMAL,
            (parser, input) -> {
                final Symbol topOfInputStack = parser.state().getInputStack().peek();
                return topOfInputStack.isNonTerminal();
            },
            parser -> {
                final ParserState state = parser.state();

                final NonTerminal nonTerminal = new NonTerminal(state.getInputStack().pop());
                state.getWorkingStack().push(nonTerminal);

                final Production firstProduction = parser.grammar().productionsFor(nonTerminal).get(0);
                parser.output().push(firstProduction);
                final List<Symbol> rhs = firstProduction.rhs();
                for (int i = rhs.size() - 1; i >= 0; i--) {
                    state.getInputStack().push(rhs.get(i));
                }
            }
    );

    private static final ParserOperation advance = new ParserOperation(
            ParserState.State.NORMAL,
            (parser, input) -> {
                final Symbol topOfInputStack = parser.state().getInputStack().peek();
                return topOfInputStack.isTerminal() && topOfInputStack.equals(new Terminal(input));
            },
            parser -> {
                final ParserState state = parser.state();
                final Symbol topOfInputStack = state.getInputStack().pop();
                state.getWorkingStack().push(topOfInputStack);
                state.setPosition(state.getPosition() + 1);
            }
    );

    private static final ParserOperation momentaryInsuccess = new ParserOperation(
            ParserState.State.NORMAL,
            (parser, input) -> {
                final Symbol topOfInputStack = parser.state().getInputStack().peek();
                return topOfInputStack.isTerminal() && topOfInputStack.equals(new Terminal(input));
            },
            parser -> parser.state().setState(ParserState.State.BACK)
    );

    private static final ParserOperation back = new ParserOperation(
            ParserState.State.BACK,
            (parser, input) -> {
                final Symbol topOfWorkingStack = parser.state().getWorkingStack().peek();
                return topOfWorkingStack.isTerminal();
            },
            parser -> {
                final ParserState state = parser.state();
                state.getInputStack().push(state.getWorkingStack().pop());
                state.setPosition(state.getPosition() - 1);
            }
    );

    private static final ParserOperation anotherTry = new ParserOperation(
            ParserState.State.BACK,
            (parser, input) -> { throw new RuntimeException("Not yet implemented"); },
            parser -> { throw new RuntimeException("Not yet implemented"); }
    );

    private static final ParserOperation anotherTryNext = new ParserOperation(
            ParserState.State.BACK,
            (parser, input) -> {
                throw new RuntimeException("Not yet implemented");
            },
            parser -> {
                throw new RuntimeException("Not yet implemented");
            }
    );

//    private static final

    private static final ParserOperation success = new ParserOperation(
            ParserState.State.NORMAL,
            (parser, input) -> parser.state().getInputStack().empty(),
            parser -> parser.state().setState(ParserState.State.FINAL)
    );

    public static final List<ParserOperation> operations = Arrays.asList(
            expand,
            advance,
            momentaryInsuccess,
            back,
            anotherTry,
            success
    );
}
