package thing

import thing.validator.ValidationResult


interface Thing<T> {
  val rule: Rule<T>
    get() = Rule {}

  @Suppress("UNCHECKED_CAST")
  fun normalize(): T = rule.normalize(this as T)

  @Suppress("UNCHECKED_CAST")
  fun validate(): ValidationResult<T> = rule.validate(this as T)
}
