package so.kciter.thing.normalizer

interface NormalizationRule<T> {
  companion object {
    operator fun <T> invoke(init: NormalizationRuleBuilder<T>.() -> Unit): NormalizationRule<T> {
      val builder = NormalizationRuleBuilder<T>()
      return builder.apply(init).build()
    }
  }

  fun normalize(value: T): T
}
