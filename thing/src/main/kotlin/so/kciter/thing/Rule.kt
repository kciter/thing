package so.kciter.thing

import so.kciter.thing.normalizer.NormalizationRule
import so.kciter.thing.redactor.RedactionRule
import so.kciter.thing.validator.ValidationRule
import so.kciter.thing.validator.ValidationResult
import so.kciter.thing.validator.combineWith

class Rule<T>(
  internal val validationRules: List<ValidationRule<T>>,
  internal val normalizationRules: List<NormalizationRule<T>>,
  internal val redactionRules: List<RedactionRule<T>>
) {
  companion object {
    operator fun <T> invoke(init: RuleBuilder<T>.() -> Unit): Rule<T> {
//      return if (RuleCache.rules.containsKey(this::class)) {
//        @Suppress("UNCHECKED_CAST")
//        RuleCache.rules[this::class] as Rule<T>
//      } else {
//        val builder = RuleBuilder<T>()
//        val rule = builder.apply(init).build()
//        RuleCache.rules[this::class] = rule
//
//        rule
//      }

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
