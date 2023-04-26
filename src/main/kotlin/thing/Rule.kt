package thing

import thing.normalizer.NormalizationRule
import thing.redactor.RedactionRule
import thing.validator.ValidationRule
import thing.validator.ValidationResult
import thing.validator.combineWith

class Rule<T>(
  private val validationRules: List<ValidationRule<T>>,
  private val normalizationRules: List<NormalizationRule<T>>,
  private val redactionRules: List<RedactionRule<T>>
) {
  companion object {
    operator fun <T> invoke(init: RuleBuilder<T>.() -> Unit): Rule<T> {
      val builder = RuleBuilder<T>()
      return builder.apply(init).build()
    }
  }

  fun normalize(value: T): T =
    if (normalizationRules.isNotEmpty()) {
      normalizationRules.fold(value) { acc, normalizationRule -> normalizationRule.normalize(acc) }
    } else value

  fun validate(value: T): ValidationResult<T> =
    if (validationRules.isNotEmpty()) {
      validationRules.map { it.validate(value) }.reduce(ValidationResult<T>::combineWith)
    } else ValidationResult.Valid(value)

  fun redact(value: T): T =
    if (redactionRules.isNotEmpty()) {
      redactionRules.fold(value) { acc, redactionRule -> redactionRule.redact(acc) }
    } else value
}
