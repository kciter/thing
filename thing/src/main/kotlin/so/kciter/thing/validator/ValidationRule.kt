package so.kciter.thing.validator

import kotlin.reflect.KProperty1

interface ValidationRule<A> {
  companion object {
    operator fun <A> invoke(init: ValidationRuleBuilder<A>.() -> Unit): ValidationRule<A> =
      ValidationRuleBuilder<A>().apply(init).build()
  }

  fun validate(value: A): ValidationResult<A>
}

internal class NonNullValidationRule<T, R>(
  private val property: KProperty1<T, R>,
  private val validationRule: ValidationRule<R>
): ValidationRule<T> {
  override fun validate(value: T): ValidationResult<T> {
    val propertyValue = property(value)
    return validationRule.validate(propertyValue).mapError { ".${property.name}$it" }.map { value }
  }
}

internal class OptionalValidationRule<T, R>(
  private val property: KProperty1<T, R?>,
  private val validationRule: ValidationRule<R>
): ValidationRule<T> {
  override fun validate(value: T): ValidationResult<T> {
    val propertyValue = property(value) ?: return ValidationResult.Valid(value)
    return validationRule.validate(propertyValue).mapError { ".${property.name}$it" }.map { value }
  }
}

internal class RequiredValidationRule<T, R>(
  private val property: KProperty1<T, R?>,
  private val validationRule: ValidationRule<R>
): ValidationRule<T> {
  override fun validate(value: T): ValidationResult<T> {
    val propertyValue = property(value)
      ?: return ValidationResult.Invalid<T>(mapOf(".${property.name}" to listOf("is required")))
    return validationRule.validate(propertyValue).mapError { ".${property.name}${it}" }.map { value }
  }
}

internal class IterableValidationRule<T>(
  private val validationRule: ValidationRule<T>
): ValidationRule<Iterable<T>> {
  override fun validate(value: Iterable<T>): ValidationResult<Iterable<T>> {
    return value.foldIndexed(ValidationResult.Valid(value)) { index, result: ValidationResult<Iterable<T>>, propertyValue ->
      val propertyValidation = validationRule.validate(propertyValue).mapError { "[$index]$it" }.map { value }
      result.combineWith(propertyValidation)
    }

  }
}

internal class ArrayValidationRule<T>(
  private val validationRule: ValidationRule<T>
): ValidationRule<Array<T>> {
  override fun validate(value: Array<T>): ValidationResult<Array<T>> {
    return value.foldIndexed(ValidationResult.Valid(value)) { index, result: ValidationResult<Array<T>>, propertyValue ->
      val propertyValidation = validationRule.validate(propertyValue).mapError { "[$index]$it" }.map { value }
      result.combineWith(propertyValidation)
    }

  }
}

internal class MapValidationRule<K, V>(
  private val validationRule: ValidationRule<Map.Entry<K, V>>
): ValidationRule<Map<K, V>> {
  override fun validate(value: Map<K, V>): ValidationResult<Map<K, V>> {
    return value.asSequence().fold(ValidationResult.Valid(value)) { result: ValidationResult<Map<K, V>>, entry ->
      val propertyValidation =
        validationRule.validate(entry).mapError { ".${entry.key.toString()}${it.removePrefix(".value")}" }.map { value }
      result.combineWith(propertyValidation)
    }
  }
}
