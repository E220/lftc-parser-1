package parser.output;

import models.NonTerminal;
import models.Production;
import models.Symbol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

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

    public void push(Production production) {
        final List<Symbol> lhs = production.lhs();
        if (lhs.get(0) != table.get(cursorPos).symbol()) {
            throw new IllegalStateException();
        }

        pushSymbols(production.rhs());
        moveCursorForward();
    }

    public Production pop() {
        if (table.isEmpty()) {
            throw new IllegalStateException();
        }

        final Production lastProduction = getLastProduction(true);
        moveCursorBackwards();
        return lastProduction;
    }

    public Production peek() {
        if (table.isEmpty()) {
            return null;
        }

        return getLastProduction(false);
    }

    private void pushSymbols(List<Symbol> symbols) {
        final int symbolCount = symbols.size();
        for (int i = 0; i < symbolCount - 1; i++) {
            pushNode(new ParserOutputNode(table.size(), symbols.get(i), cursorPos, table.size() + 1));
        }
        pushNode(new ParserOutputNode(table.size(), symbols.get(symbolCount - 1), cursorPos));
    }

    private void pushNode(ParserOutputNode node) {
        if (printerPos < table.size()) {
            table.set(printerPos, node);
        } else {
            table.add(node);
        }
        printerPos++;
    }

    private void moveCursorForward() {
        for (cursorPos++; cursorPos < printerPos; cursorPos++) {
            if (table.get(cursorPos).symbol().isNonTerminal()) {
                break;
            }
        }
    }

    private void moveCursorBackwards() {
        for (cursorPos--; cursorPos >= 0; cursorPos--) {
            if (table.get(cursorPos).symbol().isNonTerminal()) {
                break;
            }
        }
    }

    private Production getLastProduction(boolean shouldPop) {
        final Integer parentIndex = table.get(printerPos - 1).parentIndex();
        final Stack<Symbol> rhs = new Stack<>();
        while(table.get(printerPos - 1).parentIndex().equals(parentIndex)) {
            rhs.push(table.get(printerPos - 1).symbol());
            if (shouldPop) {
                printerPos--;
            }
        }

        final Symbol parent = table.get(parentIndex).symbol();
        Collections.reverse(rhs);
        return new Production(Collections.singletonList(parent), rhs);
    }
}
