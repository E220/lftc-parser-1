package parser.operations;

import models.NonTerminal;
import models.Production;
import models.Symbol;
import models.Terminal;
import parser.ParserState;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Stack;

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
                pushItemsInReverse(state.getInputStack(), rhs);
            }
    );

    private static <T> void pushItemsInReverse(Stack<T> stack, List<T> items) {
        for (int i = items.size() - 1; i >= 0; i--) {
            stack.push(items.get(i));
        }
    }

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

    private static final ParserOperation anotherTryNext = new ParserOperation(
            ParserState.State.BACK,
            (parser, input) -> {
                final Symbol topOfWorkingStack = parser.state().getWorkingStack().peek();
                if (topOfWorkingStack.isTerminal()) {
                    return false;
                }

                final List<Production> productions = parser.grammar().productionsFor(new NonTerminal(topOfWorkingStack));
                final Production lastProductionTried = parser.output().peek();
                final int lastIndexTried = productions.indexOf(lastProductionTried);
                return lastIndexTried + 1 < productions.size();
            },
            parser -> {
                final ParserState state = parser.state();
                final Stack<Symbol> inputStack = state.getInputStack();

                state.setState(ParserState.State.NORMAL);
                final Production lastProductionTried = parser.output().pop();
                for (int i = 0; i < lastProductionTried.rhs().size(); i++) {
                    inputStack.pop();
                }
                pushItemsInReverse(inputStack, lastProductionTried.rhs());
            }
    );

    private static final ParserOperation anotherTryBack = new ParserOperation(
            ParserState.State.BACK,
            (parser, input) -> {
                final Symbol topOfWorkingStack = parser.state().getWorkingStack().peek();
                return topOfWorkingStack.isNonTerminal();
            },
            parser -> {
                final ParserState state = parser.state();
                final Stack<Symbol> inputStack = state.getInputStack();

                final Production lastProductionTried = parser.output().pop();
                for (int i = 0; i < lastProductionTried.rhs().size(); i++) {
                    inputStack.pop();
                }

                final Symbol topOfWorkingStack = state.getWorkingStack().pop();
                inputStack.push(topOfWorkingStack);
            }
    );

    private static final ParserOperation anotherTryError = new ParserOperation(
            ParserState.State.BACK,
            (parser, input) -> {
                final Stack<Symbol> workingStack = parser.state().getWorkingStack();
                final Symbol topOfWorkingStack = workingStack.peek();
                return workingStack.size() == 1 && topOfWorkingStack.equals(parser.grammar().startingSymbol());
            },
            parser -> parser.state().setState(ParserState.State.ERROR)
    );

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
            anotherTryNext,
            anotherTryBack,
            anotherTryError,
            success
    );
}
