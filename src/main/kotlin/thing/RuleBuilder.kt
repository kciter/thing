package thing

import thing.normalizer.Normalization
import thing.normalizer.NormalizationBuilder
import thing.validator.*
import kotlin.reflect.KProperty1

@DslMarker
annotation class RuleDsl

@RuleDsl
open class RuleBuilder<T> {
  private val normalizations = mutableListOf<Normalization<T>>()
  private val validations = mutableListOf<Validation<T>>()

  @Suppress("FunctionName")
  open fun Normalization(init: NormalizationBuilder<T>.() -> Unit) {
    val builder = NormalizationBuilder<T>()
    val normalization = builder.apply(init).build()
    normalizations.add(normalization)
  }

  @Suppress("FunctionName")
  open fun Validation(init: ValidationBuilder<T>.() -> Unit) {
    val builder = ValidationBuilder<T>()
    val validation = builder.apply(init).build()
    validations.add(validation)
  }

  fun build(): Rule<T> {
    return Rule(
      validations = validations,
      normalizations = normalizations
    )
  }
}
