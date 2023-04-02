package thing.validator

import kotlin.reflect.KProperty1

interface Validation<T> {
  companion object {
    operator fun <T> invoke(init: ValidationBuilder<T>.() -> Unit): Validation<T> {
      val builder = ValidationBuilder<T>()
      return builder.apply(init).build()
    }
  }

  fun validate(value: T): ValidationResult<T>
}

internal class OptionalValidation<T : Any>(
  private val validation: Validation<T>
) : Validation<T?> {
  override fun validate(value: T?): ValidationResult<T?> {
    val nonNullValue = value ?: return ValidationResult.Valid(null)
    return validation.validate(nonNullValue)
  }
}

internal class RequiredValidation<T : Any>(
  private val validation: Validation<T>
) : Validation<T?> {
  override fun validate(value: T?): ValidationResult<T?> {
    val nonNullValue = value
      ?: return ValidationResult.Invalid(mapOf("" to listOf("is required")))
    return validation.validate(nonNullValue)
  }
}

internal class NonNullPropertyValidation<T, R>(
  private val property: KProperty1<T, R>,
  private val validation: Validation<R>
) : Validation<T> {
  override fun validate(value: T): ValidationResult<T> {
    val propertyValue = property(value)
    return validation.validate(propertyValue).mapError { ".${property.name}$it" }.map { value }
  }
}

internal class OptionalPropertyValidation<T, R>(
  private val property: KProperty1<T, R?>,
  private val validation: Validation<R>
) : Validation<T> {
  override fun validate(value: T): ValidationResult<T> {
    val propertyValue = property(value) ?: return ValidationResult.Valid(value)
    return validation.validate(propertyValue).mapError { ".${property.name}$it" }.map { value }
  }
}

internal class RequiredPropertyValidation<T, R>(
  private val property: KProperty1<T, R?>,
  private val validation: Validation<R>
) : Validation<T> {
  override fun validate(value: T): ValidationResult<T> {
    val propertyValue = property(value)
      ?: return ValidationResult.Invalid<T>(mapOf(".${property.name}" to listOf("is required")))
    return validation.validate(propertyValue).mapError { ".${property.name}${it}" }.map { value }
  }
}

internal class IterableValidation<T>(
  private val validation: Validation<T>
) : Validation<Iterable<T>> {
  override fun validate(value: Iterable<T>): ValidationResult<Iterable<T>> {
    return value.foldIndexed(ValidationResult.Valid(value)) { index, result: ValidationResult<Iterable<T>>, propertyValue ->
      val propertyValidation = validation.validate(propertyValue).mapError { "[$index]$it" }.map { value }
      result.combineWith(propertyValidation)
    }

  }
}

internal class ArrayValidation<T>(
  private val validation: Validation<T>
) : Validation<Array<T>> {
  override fun validate(value: Array<T>): ValidationResult<Array<T>> {
    return value.foldIndexed(ValidationResult.Valid(value)) { index, result: ValidationResult<Array<T>>, propertyValue ->
      val propertyValidation = validation.validate(propertyValue).mapError { "[$index]$it" }.map { value }
      result.combineWith(propertyValidation)
    }

  }
}

internal class MapValidation<K, V>(
  private val validation: Validation<Map.Entry<K, V>>
) : Validation<Map<K, V>> {
  override fun validate(value: Map<K, V>): ValidationResult<Map<K, V>> {
    return value.asSequence().fold(ValidationResult.Valid(value)) { result: ValidationResult<Map<K, V>>, entry ->
      val propertyValidation =
        validation.validate(entry).mapError { ".${entry.key.toString()}${it.removePrefix(".value")}" }.map { value }
      result.combineWith(propertyValidation)
    }
  }
}
