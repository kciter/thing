package thing.normalizer

internal class NormalizationNode<T>(
  private val alterations: List<Alteration<T>>,
  private val subNormalization: List<Normalization<T>>
) : Normalization<T> {
  override fun normalize(value: T): T {
    val subValidationResult = applySubNormalizations(value)
    return localNormalization(subValidationResult)
  }

  private fun localNormalization(value: T): T {
    @Suppress("UNCHECKED_CAST")
    return alterations
      .fold(value) { acc, alteration ->
        val f = acc!!::class.java.getDeclaredField(alteration.fieldName)
        f.isAccessible = true
        f.set(acc, alteration.transform(f.get(acc) as T))
        acc
      }
  }

  private fun applySubNormalizations(propertyValue: T): T {
    return subNormalization.fold(propertyValue) { acc, normalization ->
      normalization.normalize(acc)
    }
  }
}
