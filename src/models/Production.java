package models;

import java.util.List;

public record Production(List<Atom> lhs, List<Atom> rhs) {
}
