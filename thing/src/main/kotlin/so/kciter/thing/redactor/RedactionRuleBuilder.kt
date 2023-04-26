package so.kciter.thing.redactor

import kotlin.reflect.KProperty1

@DslMarker
annotation class RedactionRuleDsl

@RedactionRuleDsl
open class RedactionRuleBuilder<T> {
  companion object {
    private interface PropKey<T> {
      fun build(builder: RedactionRuleBuilder<*>): RedactionRule<T>
    }

    private data class StringValuePropKey<T, R>(
      val property: KProperty1<T, R>,
    ): PropKey<T> {
      override fun build(builder: RedactionRuleBuilder<*>): RedactionRule<T> {
        @Suppress("UNCHECKED_CAST")
        return (builder as RedactionRuleBuilder<T>).build(property.name)
      }
    }
  }

  private val redactors = mutableListOf<Redactor>()
  private val subRedactions = mutableMapOf<PropKey<T>, RedactionRuleBuilder<*>>()

  open fun addRedactor(redactedString: String, test: (String) -> Boolean): Redactor {
    return Redactor(redactedString, test).also { redactors.add(it) }
  }

  open infix fun Redactor.replace(redactedString: String): Redactor =
    Redactor(redactedString, this.test).also {
      redactors.remove(this)
      redactors.add(it)
    }

  private fun <R> KProperty1<T, R?>.getOrCreateBuilder(): RedactionRuleBuilder<R> {
    val key = StringValuePropKey(this)
    @Suppress("UNCHECKED_CAST")
    return (subRedactions.getOrPut(key) { RedactionRuleBuilder<R>() } as RedactionRuleBuilder<R>)
  }

  open operator fun <R> KProperty1<T, R>.invoke(init: RedactionRuleBuilder<R>.() -> Unit) {
    getOrCreateBuilder().also(init)
  }

  fun build(fieldName: String? = null): RedactionRule<T> {
    val nestedNormalizations = subRedactions.map { (key, builder) ->
      key.build(builder)
    }
    return RedactionRuleNode(fieldName, redactors, nestedNormalizations)
  }
}
