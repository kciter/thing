package thing.validator

internal class ValidationNode<T>(
  private val constraints: List<Constraint<T>>,
  private val subValidations: List<Validation<T>>
): Validation<T> {
  override fun validate(value: T): ValidationResult<T> {
    val subValidationResult = applySubValidations(value, keyTransform = { it })
    val localValidationResult = localValidation(value)
    return localValidationResult.combineWith(subValidationResult)
  }

  private fun localValidation(value: T): ValidationResult<T> {
    return constraints
      .filter { !it.test(value) }
      .map { constructHint(value, it) }
      .let { errors ->
        if (errors.isEmpty()) {
          ValidationResult.Valid(value)
        } else {
          ValidationResult.Invalid(mapOf("" to errors))
        }
      }
  }

  private fun constructHint(value: T, it: Constraint<T>): String {
    val replaceValue = it.hint.replace("{value}", value.toString())
    return it.templateValues
      .foldIndexed(replaceValue) { index, hint, templateValue -> hint.replace("{$index}", templateValue) }
  }

  private fun applySubValidations(propertyValue: T, keyTransform: (String) -> String): ValidationResult<T> {
    return subValidations.fold(ValidationResult.Valid(propertyValue)) { existingValidation: ValidationResult<T>, validation ->
      val newValidation = validation.validate(propertyValue).mapError(keyTransform)
      existingValidation.combineWith(newValidation)
    }
  }
}
