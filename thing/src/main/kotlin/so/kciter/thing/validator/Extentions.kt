package so.kciter.thing.validator

inline fun <reified T> ValidationRuleBuilder<*>.type() =
  addValidator(
    "must be of the correct type"
  ) { it is T }

fun <T> ValidationRuleBuilder<T>.oneOf(vararg allowed: T) =
  addValidator(
    "must be one of: {0}",
    allowed.joinToString("', '", "'", "'")
  ) { it in allowed }

inline fun <reified T: Enum<T>> ValidationRuleBuilder<String>.oneOf(): Validator<String> {
  val enumNames = enumValues<T>().map { it.name }
  return addValidator(
    "must be one of: {0}",
    enumNames.joinToString("', '", "'", "'")
  ) { it in enumNames }
}

fun <T> ValidationRuleBuilder<T>.equal(expected: T) =
  addValidator(
    "must be {0}",
    expected?.let { "'$it'" } ?: "null"
  ) { expected == it }

fun <T> ValidationRuleBuilder<T>.notEqual(expected: T) =
  addValidator(
    "must not be {0}",
    expected?.let { "'$it'" } ?: "null"
  ) { expected != it }

fun <T: Number> ValidationRuleBuilder<T>.maximum(maximumInclusive: Number) =
  addValidator(
    "must be at most '{0}'",
    maximumInclusive.toString()
  ) { it.toDouble() <= maximumInclusive.toDouble() }

fun <T: Number> ValidationRuleBuilder<T>.exclusiveMaximum(maximumExclusive: Number) =
  addValidator(
    "must be less than '{0}'",
    maximumExclusive.toString()
  ) { it.toDouble() < maximumExclusive.toDouble() }

fun <T: Number> ValidationRuleBuilder<T>.minimum(minimumInclusive: Number) =
  addValidator(
    "must be at least '{0}'",
    minimumInclusive.toString()
  ) { it.toDouble() >= minimumInclusive.toDouble() }

fun <T: Number> ValidationRuleBuilder<T>.exclusiveMinimum(minimumExclusive: Number) =
  addValidator(
    "must be greater than '{0}'",
    minimumExclusive.toString()
  ) { it.toDouble() > minimumExclusive.toDouble() }

fun <T: Number> ValidationRuleBuilder<T>.inRange(min: Number, max: Number) =
  minimum(min) and maximum(max) hint "must be in range '{0}', '{1}'"

fun <T: Number> ValidationRuleBuilder<T>.notInRange(min: Number, max: Number) =
  maximum(min) or minimum(max) hint "must not be in range '{0}', '{1}'"

fun ValidationRuleBuilder<String>.minLength(length: Int): Validator<String> {
  require(length >= 0) { IllegalArgumentException("minLength requires the length to be >= 0") }
  return addValidator(
    "must have at least {0} characters",
    length.toString()
  ) { it.length >= length }
}

fun ValidationRuleBuilder<String>.maxLength(length: Int): Validator<String> {
  require(length >= 0) { IllegalArgumentException("maxLength requires the length to be >= 0") }
  return addValidator(
    "must have at most {0} characters",
    length.toString()
  ) { it.length <= length }
}

fun ValidationRuleBuilder<String>.pattern(pattern: String) =
  pattern(pattern.toRegex())

fun ValidationRuleBuilder<String>.pattern(pattern: Regex) =
  addValidator(
    "must match the expected pattern",
    pattern.toString()
  ) { it.matches(pattern) }

fun ValidationRuleBuilder<String>.email() =
  addValidator("must be a valid email address") {
    "[A-Za-z0-9._%-+]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}".toRegex().matches(it)
  }

inline fun <reified T> ValidationRuleBuilder<T>.minItems(minSize: Int): Validator<T> =
  addValidator(
    "must have at least {0} items",
    minSize.toString()
  ) {
    when (it) {
      is Iterable<*> -> it.count() >= minSize
      is Array<*>    -> it.count() >= minSize
      is Map<*, *>   -> it.count() >= minSize
      else           -> throw IllegalStateException("minItems can not be applied to type ${T::class}")
    }
  }

inline fun <reified T> ValidationRuleBuilder<T>.maxItems(maxSize: Int): Validator<T> =
  addValidator(
    "must have at most {0} items",
    maxSize.toString()
  ) {
    when (it) {
      is Iterable<*> -> it.count() <= maxSize
      is Array<*>    -> it.count() <= maxSize
      is Map<*, *>   -> it.count() <= maxSize
      else           -> throw IllegalStateException("maxItems can not be applied to type ${T::class}")
    }
  }

inline fun <reified T> ValidationRuleBuilder<T>.uniqueItems(): Validator<T> =
  addValidator(
    "all items must be unique"
  ) {
    when (it) {
      is Iterable<*> -> it.distinct().count() == it.count()
      is Array<*>    -> it.distinct().count() == it.count()
      else           -> throw IllegalStateException("uniqueItems can not be applied to type ${T::class}")
    }
  }

inline fun <reified T> ValidationRuleBuilder<T>.notEmpty(): Validator<T> =
  addValidator(
    "must not be empty"
  ) {
    when (it) {
      is Iterable<*> -> it.count() != 0
      is Array<*>    -> it.isNotEmpty()
      is Map<*, *>   -> it.isNotEmpty()
      else           -> throw IllegalStateException("notEmpty can not be applied to type ${T::class}")
    }
  }

fun ValidationRuleBuilder<String>.notEmpty(): Validator<String> =
  addValidator(
    "must not be empty"
  ) { it.isNotEmpty() }
