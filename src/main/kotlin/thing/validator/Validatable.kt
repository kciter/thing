package thing.validator

interface Validatable<T> {
  val validation: Validation<T>
    get() = Validation {}

  @Suppress("UNCHECKED_CAST")
  fun validate(): ValidationResult<T> = validation.validate(this as T)
}