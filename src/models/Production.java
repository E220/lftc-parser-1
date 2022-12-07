package models;

import java.util.List;

public record Production(List<Symbol> lhs, List<Symbol> rhs) {
    @Override
    public String toString() {
        return lhs + " -> " + rhs;
    }
}
