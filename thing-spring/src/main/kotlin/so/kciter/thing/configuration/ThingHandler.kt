package so.kciter.thing.configuration

import kotlin.reflect.KClass

@Target(
  AnnotationTarget.CLASS,
  AnnotationTarget.FUNCTION
)
@Retention(AnnotationRetention.RUNTIME)
annotation class ThingHandler(
  vararg val value: KClass<*> = []
)
