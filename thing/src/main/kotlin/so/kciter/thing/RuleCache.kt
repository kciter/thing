package so.kciter.thing

import kotlin.reflect.KClass

object RuleCache {
  val rules = mutableMapOf<KClass<*>, Rule<*>>()
}
