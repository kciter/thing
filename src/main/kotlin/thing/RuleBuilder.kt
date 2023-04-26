package thing

import thing.normalizer.NormalizationRule
import thing.normalizer.NormalizationRuleBuilder
import thing.redactor.RedactionRule
import thing.redactor.RedactionRuleBuilder
import thing.validator.*

@DslMarker
annotation class RuleDsl

@RuleDsl
open class RuleBuilder<T> {
  private val normalizationRules = mutableListOf<NormalizationRule<T>>()
  private val validationRules = mutableListOf<ValidationRule<T>>()
  private val redactionRules = mutableListOf<RedactionRule<T>>()

  @Suppress("FunctionName")
  open fun Normalization(init: NormalizationRuleBuilder<T>.() -> Unit) {
    val builder = NormalizationRuleBuilder<T>()
    val normalization = builder.apply(init).build()
    normalizationRules.add(normalization)
  }

  @Suppress("FunctionName")
  open fun Validation(init: ValidationRuleBuilder<T>.() -> Unit) {
    val builder = ValidationRuleBuilder<T>()
    val validation = builder.apply(init).build()
    validationRules.add(validation)
  }

  @Suppress("FunctionName")
  open fun Redaction(init: RedactionRuleBuilder<T>.() -> Unit) {
    val builder = RedactionRuleBuilder<T>()
    val redaction = builder.apply(init).build()
    redactionRules.add(redaction)
  }

  fun build(): Rule<T> {
    return Rule(
      validationRules = validationRules,
      normalizationRules = normalizationRules,
      redactionRules = redactionRules
    )
  }
}
