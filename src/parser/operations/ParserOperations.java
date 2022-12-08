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

    private static ParserOperation expand = new ParserOperation(
            ParserState.State.NORMAL,
            (parser, input) -> {
                final Set<NonTerminal> nonTerminals = parser.grammar().nonTerminals();
                final Symbol topOfInputStack = parser.state().getInputStack().peek();
                return nonTerminals.contains(new NonTerminal(topOfInputStack));
            },
            parser -> {
                final ParserState state = parser.state();

                final NonTerminal nonTerminal = new NonTerminal(state.getInputStack().pop());
                state.getWorkingStack().push(nonTerminal);

                final Production firstProduction = parser.grammar().productionsFor(nonTerminal).get(0);
                final List<Symbol> rhs = firstProduction.rhs();
                for (int i = rhs.size() - 1; i >= 0; i--) {
                    state.getInputStack().push(rhs.get(i));
                }
            }
    );

    private static ParserOperation advance = new ParserOperation(
            ParserState.State.NORMAL,
            (parser, input) -> {
                final Set<Terminal> terminals = parser.grammar().terminals();
                final Symbol topOfInputStack = parser.state().getInputStack().peek();
                final Terminal terminal = new Terminal(topOfInputStack);
                return terminals.contains(terminal) && terminal.equals(new Terminal(input));
            },
            parser -> {
                final ParserState state = parser.state();
                final Terminal terminal = new Terminal(state.getInputStack().pop());
                state.getWorkingStack().push(terminal);
                state.setPosition(state.getPosition() + 1);
            }
    );

    private static ParserOperation momentaryInsuccess = new ParserOperation(
            ParserState.State.NORMAL,
            (parser, input) -> {
                final Set<Terminal> terminals = parser.grammar().terminals();
                final Symbol topOfInputStack = parser.state().getInputStack().peek();
                final Terminal terminal = new Terminal(topOfInputStack);
                return terminals.contains(terminal) && !terminal.equals(new Terminal(input));
            },
            parser -> parser.state().setState(ParserState.State.BACK)
    );

    private static ParserOperation back = new ParserOperation(
            ParserState.State.BACK,
            (parser, input) -> {
                final Set<Terminal> terminals = parser.grammar().terminals();
                final Symbol topOfWorkingStack = parser.state().getWorkingStack().peek();
                return terminals.contains(new Terminal(topOfWorkingStack));
            },
            parser -> {
                final ParserState state = parser.state();
                state.getInputStack().push(state.getWorkingStack().pop());
                state.setPosition(state.getPosition() - 1);
            }
    );

    private static ParserOperation anotherTry = new ParserOperation(
            ParserState.State.BACK,
            (parser, input) -> { throw new RuntimeException("Not yet implemented"); },
            parser -> { throw new RuntimeException("Not yet implemented"); }
    );

    private static ParserOperation success = new ParserOperation(
            ParserState.State.NORMAL,
            (parser, input) -> parser.state().getInputStack().empty(),
            parser -> parser.state().setState(ParserState.State.FINAL)
    );

    public static List<ParserOperation> operations = Arrays.asList(
            expand,
            advance,
            momentaryInsuccess,
            back,
            anotherTry,
            success
    );
}
