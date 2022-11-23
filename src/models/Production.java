package models;

import java.util.List;

public record Production(List<Symbol> lhs, List<Symbol> rhs) {
}
