package thing.normalizer

import kotlin.reflect.KProperty1

@DslMarker
annotation class NormalizationDsl

@NormalizationDsl
open class NormalizationBuilder<T>(val fieldName: String? = null) {
  companion object {
    private interface PropKey<T> {
      fun build(builder: NormalizationBuilder<*>): Normalization<T>
    }

    private data class SingleValuePropKey<T, R>(
      val property: KProperty1<T, R>,
    ) : PropKey<T> {
      override fun build(builder: NormalizationBuilder<*>): Normalization<T> {
        @Suppress("UNCHECKED_CAST")
        return (builder as NormalizationBuilder<T>).build()
      }
    }

    private data class IterablePropKey<T, R>(
      val property: KProperty1<T, Iterable<R>>,
    ) : PropKey<T> {
      override fun build(builder: NormalizationBuilder<*>): Normalization<T> {
        @Suppress("UNCHECKED_CAST")
        return (builder as NormalizationBuilder<T>).build()
      }
    }

    private data class ArrayPropKey<T, R>(
      val property: KProperty1<T, Array<R>>
    ) : PropKey<T> {
      override fun build(builder: NormalizationBuilder<*>): Normalization<T> {
        @Suppress("UNCHECKED_CAST")
        return (builder as NormalizationBuilder<T>).build()
      }
    }

    private data class MapPropKey<T, K, V>(
      val property: KProperty1<T, Map<K, V>>
    ) : PropKey<T> {
      override fun build(builder: NormalizationBuilder<*>): Normalization<T> {
        @Suppress("UNCHECKED_CAST")
        return (builder as NormalizationBuilder<T>).build()
      }
    }
  }

  private val alterations = mutableListOf<Alteration<T>>()
  private val subNormalizations = mutableMapOf<PropKey<T>, NormalizationBuilder<*>>()
  private val prebuiltNormalizations = mutableListOf<Normalization<T>>()

  open fun addAlteration(transform: (T) -> T): Alteration<T> {
    return Alteration(fieldName!!, transform).also { alterations.add(it) }
  }

  private fun <R> KProperty1<T, R?>.getOrCreateBuilder(): NormalizationBuilder<R> {
    val key = SingleValuePropKey(this)
    @Suppress("UNCHECKED_CAST")
    return (subNormalizations.getOrPut(key) { NormalizationBuilder<R>(key.property.name) } as NormalizationBuilder<R>)
  }

  private fun <R> PropKey<T>.getOrCreateBuilder(): NormalizationBuilder<R> {
    @Suppress("UNCHECKED_CAST")
    return (subNormalizations.getOrPut(this) { NormalizationBuilder<R>() } as NormalizationBuilder<R>)
  }

  open operator fun <R> KProperty1<T, R>.invoke(init: NormalizationBuilder<R>.() -> Unit) {
    getOrCreateBuilder().also(init)
  }

  private fun <R> KProperty1<T, Iterable<R>>.getOrCreateIterablePropertyBuilder(): NormalizationBuilder<R> {
    val key = IterablePropKey(this)
    @Suppress("UNCHECKED_CAST")
    return (subNormalizations.getOrPut(key) { NormalizationBuilder<R>() } as NormalizationBuilder<R>)
  }

  private fun <R> onEachIterable(prop: KProperty1<T, Iterable<R>>, init: NormalizationBuilder<R>.() -> Unit) {
    prop.getOrCreateIterablePropertyBuilder().also(init)
  }

  private fun <R> onEachArray(prop: KProperty1<T, Array<R>>, init: NormalizationBuilder<R>.() -> Unit) {
    ArrayPropKey(prop).getOrCreateBuilder<R>().also(init)
  }

  private fun <K, V> onEachMap(prop: KProperty1<T, Map<K, V>>, init: NormalizationBuilder<Map.Entry<K, V>>.() -> Unit) {
    MapPropKey(prop).getOrCreateBuilder<Map.Entry<K, V>>().also(init)
  }

  fun run(validation: Normalization<T>) {
    prebuiltNormalizations.add(validation)
  }

  fun build(): Normalization<T> {
    val nestedNormalizations = subNormalizations.map { (key, builder) ->
      key.build(builder)
    }
    return NormalizationNode(alterations, nestedNormalizations + prebuiltNormalizations)
  }
}

@JvmName("onEachIterable")
infix fun <S, T : Iterable<S>> NormalizationBuilder<T>.onEach(init: NormalizationBuilder<S>.() -> Unit) {
  val builder = NormalizationBuilder<S>(this.fieldName)
  init(builder)
  @Suppress("UNCHECKED_CAST")
  run(IterableNormalization(builder.build()) as Normalization<T>)
}

@JvmName("onEachArray")
infix fun <T> NormalizationBuilder<Array<T>>.onEach(init: NormalizationBuilder<T>.() -> Unit) {
  val builder = NormalizationBuilder<T>(this.fieldName)
  init(builder)
  @Suppress("UNCHECKED_CAST")
  run(ArrayNormalization(builder.build()) as Normalization<Array<T>>)
}

@JvmName("onEachMap")
infix fun <K, V, T : Map<K, V>> NormalizationBuilder<T>.onEach(init: NormalizationBuilder<Map.Entry<K, V>>.() -> Unit) {
  val builder = NormalizationBuilder<Map.Entry<K, V>>(this.fieldName)
  init(builder)
  @Suppress("UNCHECKED_CAST")
  run(MapNormalization(builder.build()) as Normalization<T>)
}