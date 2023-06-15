package so.kciter.thing.normalizer

import kotlin.reflect.KProperty1
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.jvm.isAccessible

internal class NormalizationRuleNode<T>(
  private val fieldName: String?,
  private val normalizers: List<Normalizer<T>>,
  private val subNormalizationRule: List<NormalizationRule<T>>
): NormalizationRule<T> {
  override fun normalize(value: T): T {
    val subValidationResult = applySubNormalizations(value)
    return localNormalization(subValidationResult)
  }

  private fun localNormalization(value: T): T {
    @Suppress("UNCHECKED_CAST")
    return normalizers
      .fold(value) { acc, normalizer ->
        val copy = acc!!::class.members.first { it.name == "copy" }
        val instanceParameter = copy.instanceParameter!!
        val parameterToBeUpdated = copy.parameters.first { it.name == fieldName }
        val property = acc!!::class.members.first { it.name == fieldName } as KProperty1<Any, *>
        copy.callBy(mapOf(instanceParameter to acc, parameterToBeUpdated to normalizer.transform(property.get(acc) as T))) as T

//        val f = acc!!::class.java.getDeclaredField(fieldName)
//        f.isAccessible = true
//        f.set(acc, normalizer.transform(f.get(acc) as T))
//        acc
      }
  }

  private fun applySubNormalizations(propertyValue: T): T {
    return subNormalizationRule.fold(propertyValue) { acc, normalization ->
      normalization.normalize(acc)
    }
  }
}
