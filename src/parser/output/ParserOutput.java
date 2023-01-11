package parser.output;

import models.NonTerminal;
import models.Production;
import models.Symbol;

import java.util.*;

public class ParserOutput {
    private final List<ParserOutputNode> table;
    private int cursorPos;
    private int printerPos;

    public ParserOutput(NonTerminal startingSymbol) {
        final ParserOutputNode firstNode = new ParserOutputNode(0, startingSymbol);
        table = new ArrayList<>();
        table.add(firstNode);
        cursorPos = 0;
        printerPos = 1;
    }

    @Override
    public String toString() {
        final StringBuilder string = new StringBuilder("[");
        for (int i = 0; i < printerPos; i++) {
            string.append(table.get(i));
        }
        string.append("], cursor: ");
        string.append(cursorPos);
        return string.toString();
    }

    public void push(Production production) {
        final List<Symbol> lhs = production.lhs();
        if (!lhs.get(0).equals(table.get(cursorPos).getSymbol())) {
            throw new IllegalStateException();
        }

        table.get(cursorPos).setFirstChildIndex(printerPos);
        pushSymbols(production.rhs()).ifPresentOrElse(
                nonTerminal -> cursorPos = nonTerminal,
                this::moveCursorForward
        );
    }

    public Production pop() {
        if (table.isEmpty()) {
            throw new IllegalStateException();
        }

        moveCursorBackwards();
        return getLastProduction(true);
    }

    public Production peek() {
        if (table.isEmpty()) {
            return null;
        }

        return getLastProduction(false);
    }

    public void moveCursorForward() {
        for (cursorPos++; cursorPos < printerPos; cursorPos++) {
            if (table.get(cursorPos).getSymbol().isNonTerminal()) {
                break;
            }
        }
    }

    public void moveCursorBackwards() {
        cursorPos = table.get(cursorPos).getParentIndex();
    }

    private Optional<Integer> pushSymbols(List<Symbol> symbols) {
        table.get(cursorPos).setFirstChildIndex(printerPos);
        Integer firstNonTerminalPos = null;
        for (final Symbol symbol : symbols) {
            if (symbol.isNonTerminal() && Objects.isNull(firstNonTerminalPos)) {
                firstNonTerminalPos = printerPos;
            }
            pushNode(new ParserOutputNode(printerPos, symbol, cursorPos));
        }
        return Optional.ofNullable(firstNonTerminalPos);
    }

    private void pushNode(ParserOutputNode node) {
        if (printerPos < table.size()) {
            table.set(printerPos, node);
        } else {
            table.add(node);
        }
        printerPos++;
    }

    private Production getLastProduction(boolean shouldPop) {
        // TODO: rewrite using PON.firstChild
        final Integer parentIndex = table.get(printerPos - 1).getParentIndex();
        final Stack<Symbol> rhs = new Stack<>();
        int position = printerPos - 1;

        while(parentIndex.equals(table.get(position).getParentIndex())) {
            rhs.push(table.get(position).getSymbol());
            position--;
            if (shouldPop) {
                printerPos--;
            }
        }

        final Symbol parent = table.get(parentIndex).getSymbol();
        Collections.reverse(rhs);
        return new Production(Collections.singletonList(parent), rhs);
    }
}
