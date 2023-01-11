package parser;

import models.NonTerminal;
import models.Symbol;

import java.util.Stack;

public class ParserState {

    private State state;
    private int position;
    private final Stack<Symbol> workingStack;
    private final Stack<Symbol> inputStack;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Stack<Symbol> getWorkingStack() {
        return workingStack;
    }

    public Stack<Symbol> getInputStack() {
        return inputStack;
    }

    public ParserState(NonTerminal startingSymbol) {
        this.state = State.NORMAL;
        this.position = 0;
        this.workingStack = new Stack<>();
        this.inputStack = new Stack<>(){{ this.push(startingSymbol); }};
    }

    @Override
    public String toString() {
        return "(" + state.string + ", " + position + ", " + workingStack + ", " + inputStack + ")";
    }

    public enum State {
        NORMAL("q"),
        BACK("b"),
        FINAL("f"),
        ERROR("e");

        State(String string) {
            this.string = string;
        }

        private final String string;

        @Override
        public String toString() {
            return string;
        }
    }
}
