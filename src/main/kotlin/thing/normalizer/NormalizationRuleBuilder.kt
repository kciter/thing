package thing.normalizer

import kotlin.reflect.KProperty1

@DslMarker
annotation class NormalizationRuleDsl

@NormalizationRuleDsl
open class NormalizationRuleBuilder<T> {
  companion object {
    private interface PropKey<T> {
      fun build(builder: NormalizationRuleBuilder<*>): NormalizationRule<T>
    }

    private data class SingleValuePropKey<T, R>(
      val property: KProperty1<T, R>,
    ): PropKey<T> {
      override fun build(builder: NormalizationRuleBuilder<*>): NormalizationRule<T> {
        @Suppress("UNCHECKED_CAST")
        return (builder as NormalizationRuleBuilder<T>).build(property.name)
      }
    }
  }

  private val normalizers = mutableListOf<Normalizer<T>>()
  private val subNormalizations = mutableMapOf<PropKey<T>, NormalizationRuleBuilder<*>>()

  open fun addNormalizer(transform: (T) -> T): Normalizer<T> {
    return Normalizer(transform).also { normalizers.add(it) }
  }

  private fun <R> KProperty1<T, R?>.getOrCreateBuilder(): NormalizationRuleBuilder<R> {
    val key = SingleValuePropKey(this)
    @Suppress("UNCHECKED_CAST")
    return (subNormalizations.getOrPut(key) { NormalizationRuleBuilder<R>() } as NormalizationRuleBuilder<R>)
  }

  open operator fun <R> KProperty1<T, R>.invoke(init: NormalizationRuleBuilder<R>.() -> Unit) {
    getOrCreateBuilder().also(init)
  }

  fun build(fieldName: String? = null): NormalizationRule<T> {
    val nestedNormalizations = subNormalizations.map { (key, builder) ->
      key.build(builder)
    }
    return NormalizationRuleNode(fieldName, normalizers, nestedNormalizations)
  }
}
