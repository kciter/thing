package thing.normalizer

interface Normalizable<T> {
  val normalization: Normalization<T>
    get() = Normalization {}

  @Suppress("UNCHECKED_CAST")
  fun normalize(): T = this as T
}