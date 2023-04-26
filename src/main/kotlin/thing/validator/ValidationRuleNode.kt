package thing.validator

internal class ValidationRuleNode<T>(
  private val validators: List<Validator<T>>,
  private val subValidationRules: List<ValidationRule<T>>
): ValidationRule<T> {
  override fun validate(value: T): ValidationResult<T> {
    val subValidationResult = applySubValidations(value) { it }
    val localValidationResult = localValidation(value)
    return localValidationResult.combineWith(subValidationResult)
  }

  private fun localValidation(value: T): ValidationResult<T> {
    return validators
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

  private fun constructHint(value: T, it: Validator<T>): String {
    val replaceValue = it.hint.replace("{value}", value.toString())
    return it.templateValues
      .foldIndexed(replaceValue) { index, hint, templateValue -> hint.replace("{$index}", templateValue) }
  }

  private fun applySubValidations(propertyValue: T, keyTransform: (String) -> String): ValidationResult<T> {
    return subValidationRules.fold(ValidationResult.Valid(propertyValue)) { existingValidation: ValidationResult<T>, validation ->
      val newValidation = validation.validate(propertyValue).mapError(keyTransform)
      existingValidation.combineWith(newValidation)
    }
  }
}
