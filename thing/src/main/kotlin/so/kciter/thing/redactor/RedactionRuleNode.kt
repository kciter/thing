package so.kciter.thing.redactor

import kotlin.reflect.KProperty1
import kotlin.reflect.full.instanceParameter

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
        val copy = acc!!::class.members.first { it.name == "copy" }
        val instanceParameter = copy.instanceParameter!!
        val parameterToBeUpdated = copy.parameters.first { it.name == fieldName }
        val property = acc!!::class.members.first { it.name == fieldName } as KProperty1<Any, *>
        if (alteration.test(property.get(acc) as String)) {
          copy.callBy(mapOf(instanceParameter to acc, parameterToBeUpdated to alteration.redactedString)) as T
        } else {
          acc
        }
//        val f = acc!!::class.java.getDeclaredField(fieldName)
//        f.isAccessible = true
//        if (alteration.test(f.get(acc) as String)) {
//          f.set(acc, alteration.redactedString)
//        }
//        acc
      }
  }

  private fun applySubRedactions(propertyValue: T): T {
    return subRedactionRule.fold(propertyValue) { acc, normalization ->
      normalization.redact(acc)
    }
  }
}
