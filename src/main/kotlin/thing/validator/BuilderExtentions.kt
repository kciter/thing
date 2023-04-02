package thing.validator

inline fun <reified T> ValidationBuilder<*>.type() =
  addConstraint(
    "must be of the correct type"
  ) { it is T }

fun <T> ValidationBuilder<T>.oneOf(vararg allowed: T) =
  addConstraint(
    "must be one of: {0}",
    allowed.joinToString("', '", "'", "'")
  ) { it in allowed }

inline fun <reified T : Enum<T>> ValidationBuilder<String>.oneOf(): Constraint<String> {
  val enumNames = enumValues<T>().map { it.name }
  return addConstraint(
    "must be one of: {0}",
    enumNames.joinToString("', '", "'", "'")
  ) { it in enumNames }
}

fun <T> ValidationBuilder<T>.equal(expected: T) =
  addConstraint(
    "must be {0}",
    expected?.let { "'$it'" } ?: "null"
  ) { expected == it }

fun <T> ValidationBuilder<T>.notEqual(expected: T) =
  addConstraint(
    "must not be {0}",
    expected?.let { "'$it'" } ?: "null"
  ) { expected != it }

fun <T : Number> ValidationBuilder<T>.maximum(maximumInclusive: Number) =
  addConstraint(
    "must be at most '{0}'",
    maximumInclusive.toString()
  ) { it.toDouble() <= maximumInclusive.toDouble() }

fun <T : Number> ValidationBuilder<T>.exclusiveMaximum(maximumExclusive: Number) =
  addConstraint(
    "must be less than '{0}'",
    maximumExclusive.toString()
  ) { it.toDouble() < maximumExclusive.toDouble() }

fun <T : Number> ValidationBuilder<T>.minimum(minimumInclusive: Number) =
  addConstraint(
    "must be at least '{0}'",
    minimumInclusive.toString()
  ) { it.toDouble() >= minimumInclusive.toDouble() }

fun <T : Number> ValidationBuilder<T>.exclusiveMinimum(minimumExclusive: Number) =
  addConstraint(
    "must be greater than '{0}'",
    minimumExclusive.toString()
  ) { it.toDouble() > minimumExclusive.toDouble() }

fun <T : Number> ValidationBuilder<T>.inRange(min: Number, max: Number) =
  minimum(min) and maximum(max) hint "must be in range '{0}', '{1}'"

fun <T : Number> ValidationBuilder<T>.notInRange(min: Number, max: Number) =
  maximum(min) or minimum(max) hint "must not be in range '{0}', '{1}'"

fun ValidationBuilder<String>.minLength(length: Int): Constraint<String> {
  require(length >= 0) { IllegalArgumentException("minLength requires the length to be >= 0") }
  return addConstraint(
    "must have at least {0} characters",
    length.toString()
  ) { it.length >= length }
}

fun ValidationBuilder<String>.maxLength(length: Int): Constraint<String> {
  require(length >= 0) { IllegalArgumentException("maxLength requires the length to be >= 0") }
  return addConstraint(
    "must have at most {0} characters",
    length.toString()
  ) { it.length <= length }
}

fun ValidationBuilder<String>.pattern(pattern: String) =
  pattern(pattern.toRegex())

fun ValidationBuilder<String>.pattern(pattern: Regex) =
  addConstraint(
    "must match the expected pattern",
    pattern.toString()
  ) { it.matches(pattern) }

fun ValidationBuilder<String>.email() =
  addConstraint("must be a valid email address") {
    "[A-Za-z0-9._%-+]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}".toRegex().matches(it)
  }

inline fun <reified T> ValidationBuilder<T>.minItems(minSize: Int): Constraint<T> =
  addConstraint(
    "must have at least {0} items",
    minSize.toString()
  ) {
    when (it) {
      is Iterable<*> -> it.count() >= minSize
      is Array<*> -> it.count() >= minSize
      is Map<*, *> -> it.count() >= minSize
      else -> throw IllegalStateException("minItems can not be applied to type ${T::class}")
    }
  }

inline fun <reified T> ValidationBuilder<T>.maxItems(maxSize: Int): Constraint<T> =
  addConstraint(
    "must have at most {0} items",
    maxSize.toString()
  ) {
    when (it) {
      is Iterable<*> -> it.count() <= maxSize
      is Array<*> -> it.count() <= maxSize
      is Map<*, *> -> it.count() <= maxSize
      else -> throw IllegalStateException("maxItems can not be applied to type ${T::class}")
    }
  }

inline fun <reified T : Map<*, *>> ValidationBuilder<T>.minProperties(minSize: Int): Constraint<T> =
  minItems(minSize) hint "must have at least {0} properties"

inline fun <reified T : Map<*, *>> ValidationBuilder<T>.maxProperties(maxSize: Int): Constraint<T> =
  maxItems(maxSize) hint "must have at most {0} properties"

inline fun <reified T> ValidationBuilder<T>.uniqueItems(unique: Boolean): Constraint<T> =
  addConstraint(
    "all items must be unique"
  ) {
    !unique || when (it) {
      is Iterable<*> -> it.distinct().count() == it.count()
      is Array<*> -> it.distinct().count() == it.count()
      else -> throw IllegalStateException("uniqueItems can not be applied to type ${T::class}")
    }
  }
