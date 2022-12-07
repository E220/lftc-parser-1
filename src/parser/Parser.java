package parser;

import grammar.Grammar;
import models.NonTerminal;
import models.Production;
import models.Symbol;
import models.Terminal;

import java.util.List;
import java.util.Stack;

public class Parser {

    private ParserState state;
    private int position;
    private Stack<Symbol> workingStack;
    private Stack<Symbol> inputStack;

    public Parser(ParserState state, int position, Stack<Symbol> workingStack, Stack<Symbol> inputStack) {
        this.state = state;
        this.position = position;
        this.workingStack = workingStack;
        this.inputStack = inputStack;
    }
    private void expand(Grammar grammar) {
        // remove non-terminal from input stack
        final NonTerminal terminal = new NonTerminal(inputStack.pop());

        // add non-terminal to working stack
        workingStack.push(terminal);

        // add rhs of non-terminal's first production to input stack
        final Production firstProduction = grammar.productionsFor(terminal).get(0);
        final List<Symbol> rhs = firstProduction.rhs();
        for (int i = rhs.size() - 1; i >= 0; i--) {
            inputStack.push(rhs.get(i));
        }
    }

    private void advance() {
        // remove terminal from input stack
        final Terminal terminal = new Terminal(inputStack.pop());

        // add terminal to working stack
        workingStack.push(terminal);

        // increment position
        position++;
    }

    private void momentaryInsuccess() {
        // set state to back
        state = ParserState.BACK;
    }

    private void back() {
        // remove terminal from working stack
        final Terminal terminal = new Terminal(workingStack.pop());

        // add terminal to input stack
        inputStack.push(terminal);

        // decrease position
        position--;
    }

    private void anotherTry() {
        throw new RuntimeException("Not yet implemented");
    }

    private void success() {
        // set state to final
        state = ParserState.FINAL;
    }
}
