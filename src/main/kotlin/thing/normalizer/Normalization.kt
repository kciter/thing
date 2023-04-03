package thing.normalizer

interface Normalization<T> {
  companion object {
    operator fun <T> invoke(init: NormalizationBuilder<T>.() -> Unit): Normalization<T> {
      val builder = NormalizationBuilder<T>()
      return builder.apply(init).build()
    }
  }

  fun normalize(value: T): T
}

internal class IterableNormalization<T>(
  private val normalization: Normalization<T>
): Normalization<Iterable<T>> {
  override fun normalize(value: Iterable<T>): Iterable<T> {
    return value.map {
      normalization.normalize(it)
    }
  }
}

internal class ArrayNormalization<T>(
  private val normalization: Normalization<T>
): Normalization<Array<T>> {
  override fun normalize(value: Array<T>): Array<T> {
    value.forEach {
      normalization.normalize(it)
    }
    return value
  }
}

internal class MapNormalization<K, V>(
  private val normalization: Normalization<Map.Entry<K, V>>
): Normalization<Map<K, V>> {
  override fun normalize(value: Map<K, V>): Map<K, V> {
    value.asSequence().map {
      normalization.normalize(it)
    }
    return value
  }
}
