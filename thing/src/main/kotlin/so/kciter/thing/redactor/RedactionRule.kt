package so.kciter.thing.redactor

interface RedactionRule<T> {
  companion object {
    operator fun <T> invoke(init: RedactionRuleBuilder<T>.() -> Unit): RedactionRule<T> {
      val builder = RedactionRuleBuilder<T>()
      return builder.apply(init).build()
    }
  }

  fun redact(value: T): T
}
