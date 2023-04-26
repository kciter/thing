package so.kciter.thing.validator

import kotlin.reflect.KProperty1

@DslMarker
annotation class ValidationRuleDsl

@ValidationRuleDsl
open class ValidationRuleBuilder<T> {
  companion object {
    private enum class PropType {
      NonNull,
      Optional,
      Required
    }

    private interface PropKey<T> {
      fun build(builder: ValidationRuleBuilder<*>): ValidationRule<T>
    }

    private data class SingleValuePropKey<T, R>(
      val property: KProperty1<T, R>,
      val propType: PropType
    ): PropKey<T> {
      override fun build(builder: ValidationRuleBuilder<*>): ValidationRule<T> {
        @Suppress("UNCHECKED_CAST")
        val validations = (builder as ValidationRuleBuilder<R>).build()
        return when (propType) {
          PropType.NonNull -> NonNullValidationRule(property, validations)
          PropType.Optional -> OptionalValidationRule(property, validations)
          PropType.Required -> RequiredValidationRule(property, validations)
        }
      }
    }
  }

  private val validators = mutableListOf<Validator<T>>()
  private val subValidations = mutableMapOf<PropKey<T>, ValidationRuleBuilder<*>>()
  private val prebuiltValidationRules = mutableListOf<ValidationRule<T>>()

  open infix fun Validator<T>.hint(hint: String): Validator<T> =
    Validator(hint, this.templateValues, this.test).also {
      validators.remove(this)
      validators.add(it)
    }

  open fun addValidator(errorMessage: String, vararg templateValues: String, test: (T) -> Boolean): Validator<T> =
    Validator(errorMessage, templateValues.toList(), test).also { validators.add(it) }

  private fun <R> KProperty1<T, R?>.getOrCreateBuilder(propType: PropType): ValidationRuleBuilder<R> {
    val key = SingleValuePropKey(this, propType)
    @Suppress("UNCHECKED_CAST")
    return (subValidations.getOrPut(key) { ValidationRuleBuilder<R>() } as ValidationRuleBuilder<R>)
  }

  open operator fun <R> KProperty1<T, R>.invoke(init: ValidationRuleBuilder<R>.() -> Unit) {
    getOrCreateBuilder(PropType.NonNull).also(init)
  }

  open infix fun <R> KProperty1<T, R?>.ifPresent(init: ValidationRuleBuilder<R>.() -> Unit) {
    getOrCreateBuilder(PropType.Optional).also(init)
  }

  open infix fun <R> KProperty1<T, R?>.required(init: ValidationRuleBuilder<R>.() -> Unit) {
    getOrCreateBuilder(PropType.Required).also(init)
  }

  @JvmName("onEachIterable")
  infix fun <S, T: Iterable<S>> ValidationRuleBuilder<T>.onEach(init: ValidationRuleBuilder<S>.() -> Unit) {
    val builder = ValidationRuleBuilder<S>()
    init(builder)
    @Suppress("UNCHECKED_CAST")
    prebuiltValidationRules.add(IterableValidationRule(builder.build()) as ValidationRule<T>)
  }

  @JvmName("onEachArray")
  infix fun <T> ValidationRuleBuilder<Array<T>>.onEach(init: ValidationRuleBuilder<T>.() -> Unit) {
    val builder = ValidationRuleBuilder<T>()
    init(builder)
    @Suppress("UNCHECKED_CAST")
    prebuiltValidationRules.add(ArrayValidationRule(builder.build()) as ValidationRule<Array<T>>)
  }

  @JvmName("onEachMap")
  infix fun <K, V, T: Map<K, V>> ValidationRuleBuilder<T>.onEach(init: ValidationRuleBuilder<Map.Entry<K, V>>.() -> Unit) {
    val builder = ValidationRuleBuilder<Map.Entry<K, V>>()
    init(builder)
    @Suppress("UNCHECKED_CAST")
    prebuiltValidationRules.add(MapValidationRule(builder.build()) as ValidationRule<T>)
  }

  fun build(): ValidationRule<T> {
    val nestedValidations = subValidations.map { (key, builder) ->
      key.build(builder)
    }
    return ValidationRuleNode(validators, nestedValidations + prebuiltValidationRules)
  }
}
