package thing.redactor

internal class RedactionRuleNode<T>(
  private val fieldName: String?,
  private val redactors: List<Redactor>,
  private val subRedactionRule: List<RedactionRule<T>>
): RedactionRule<T> {
  override fun redact(value: T): T {
    val subValidationResult = applySubRedactions(value)
    return localRedaction(subValidationResult)
  }

  private fun localRedaction(value: T): T {
    @Suppress("UNCHECKED_CAST")
    return redactors
      .fold(value) { acc, alteration ->
        val f = acc!!::class.java.getDeclaredField(fieldName)
        f.isAccessible = true
        if (alteration.test(f.get(acc) as String)) {
          f.set(acc, alteration.redactedString)
        }
        acc
      }
  }

  private fun applySubRedactions(propertyValue: T): T {
    return subRedactionRule.fold(propertyValue) { acc, normalization ->
      normalization.redact(acc)
    }
  }
}
