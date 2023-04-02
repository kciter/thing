package thing

import thing.normalizer.Normalization
import thing.validator.Validation
import thing.validator.ValidationResult
import thing.validator.combineWith

class Rule<T>(
  private val validations: List<Validation<T>>,
  private val normalizations: List<Normalization<T>>
) {
  companion object {
    operator fun <T> invoke(init: RuleBuilder<T>.() -> Unit): Rule<T> {
      val builder = RuleBuilder<T>()
      return builder.apply(init).build()
    }
  }

  fun normalize(value: T): T =
    if (normalizations.isNotEmpty()) {
      normalizations.fold(value) { acc, normalization -> normalization.normalize(acc) }
    } else value

  fun validate(value: T): ValidationResult<T> =
    if (validations.isNotEmpty()) {
      validations.map { it.validate(value) }.reduce(ValidationResult<T>::combineWith)
    } else ValidationResult.Valid(value)
}
