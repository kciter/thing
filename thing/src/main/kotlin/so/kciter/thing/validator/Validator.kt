package so.kciter.thing.validator

class Validator<T>(val hint: String, val templateValues: List<String>, val test: (T) -> Boolean) {
  infix fun and(other: Validator<T>): Validator<T> =
    Validator(hint, templateValues) { test(it) && other.test(it) }

  infix fun or(other: Validator<T>): Validator<T> =
    Validator(hint, templateValues) { test(it) || other.test(it) }
}

