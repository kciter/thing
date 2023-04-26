package thing.normalizer

interface NormalizationRule<T> {
  companion object {
    operator fun <T> invoke(init: NormalizationRuleBuilder<T>.() -> Unit): NormalizationRule<T> {
      val builder = NormalizationRuleBuilder<T>()
      builder.apply { init.invoke(this) }
      return builder.apply(init).build()
    }
  }

  fun normalize(value: T): T
}
