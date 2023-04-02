package thing.validator

class Constraint<T>(val hint: String, val templateValues: List<String>, val test: (T) -> Boolean) {
  infix fun and(other: Constraint<T>): Constraint<T> =
    Constraint(hint, templateValues) { test(it) && other.test(it) }

  infix fun or(other: Constraint<T>): Constraint<T> =
    Constraint(hint, templateValues) { test(it) || other.test(it) }
}

