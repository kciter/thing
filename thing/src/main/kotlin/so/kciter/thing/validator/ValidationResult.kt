package so.kciter.thing.validator

import kotlin.reflect.KProperty1

interface ValidationError {
  val dataPath: String
  val message: String
}

internal data class PropertyValidationError(
  override val dataPath: String,
  override val message: String
): ValidationError {
  override fun toString(): String {
    return "RuleError(dataPath=$dataPath, message=$message)"
  }
}

interface ValidationErrors: List<ValidationError>

internal class DefaultValidationErrors(private val errors: List<ValidationError>): ValidationErrors,
  List<ValidationError> by errors {
  override fun toString(): String {
    return errors.toString()
  }
}

sealed class ValidationResult<out T> {
  abstract operator fun get(vararg propertyPath: Any): List<String>?
  abstract fun <R> map(transform: (T) -> R): ValidationResult<R>
  abstract val errors: ValidationErrors

  data class Invalid<T>(
    internal val internalErrors: Map<String, List<String>>
  ): ValidationResult<T>() {

    override fun get(vararg propertyPath: Any): List<String>? =
      internalErrors[propertyPath.joinToString("", transform = ::toPathSegment)]

    override fun <R> map(transform: (T) -> R): ValidationResult<R> = Invalid(this.internalErrors)

    private fun toPathSegment(it: Any): String {
      return when (it) {
        is KProperty1<*, *> -> ".${it.name}"
        is Int -> "[$it]"
        else -> ".$it"
      }
    }

    override val errors: ValidationErrors by lazy {
      DefaultValidationErrors(
        internalErrors.flatMap { (path, errors) ->
          errors.map { PropertyValidationError(path, it) }
        }
      )
    }

    override fun toString(): String {
      return "Invalid(errors=${errors})"
    }
  }

  data class Valid<T>(val value: T): ValidationResult<T>() {
    override fun get(vararg propertyPath: Any): List<String>? = null
    override fun <R> map(transform: (T) -> R): ValidationResult<R> = Valid(transform(this.value))
    override val errors: ValidationErrors
      get() = DefaultValidationErrors(emptyList())

    override fun toString(): String {
      return "Valid(value=$value)"
    }
  }
}

internal fun <R> ValidationResult<R>.mapError(keyTransform: (String) -> String): ValidationResult<R> {
  return when (this) {
    is ValidationResult.Valid -> this
    is ValidationResult.Invalid -> ValidationResult.Invalid(this.internalErrors.mapKeys { (key, _) ->
      keyTransform(key)
    })
  }
}

internal fun <R> ValidationResult<R>.combineWith(other: ValidationResult<R>): ValidationResult<R> {
  return when (this) {
    is ValidationResult.Valid -> return other
    is ValidationResult.Invalid -> when (other) {
      is ValidationResult.Valid -> this
      is ValidationResult.Invalid -> {
        ValidationResult.Invalid((this.internalErrors.toList() + other.internalErrors.toList())
          .groupBy({ it.first }, { it.second })
          .mapValues { (_, values) -> values.flatten() })
      }
    }
  }
}
