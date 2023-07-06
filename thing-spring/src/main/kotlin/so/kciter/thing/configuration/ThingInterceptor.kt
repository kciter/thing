package so.kciter.thing.configuration

import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import org.springframework.beans.factory.FactoryBean
import org.springframework.beans.factory.SmartFactoryBean
import org.springframework.http.HttpStatus
import org.springframework.util.ClassUtils
import org.springframework.web.server.ResponseStatusException
import so.kciter.thing.Thing
import so.kciter.thing.validator.ValidationResult
import java.lang.reflect.Method

class ThingInterceptor: MethodInterceptor {
  override fun invoke(invocation: MethodInvocation): Any? {
    if (isFactoryBeanMetadataMethod(invocation.method)) {
      return invocation.proceed()
    }

    val target = invocation.`this`
    require(target != null) {
      "Target must not be null"
    }

    val result = validateParameters(invocation.arguments)
    if (result.isNotEmpty()) {
      throw ResponseStatusException(HttpStatus.BAD_REQUEST, result.joinToString { it.errors.toString() })
    }

    val returnValue = invocation.proceed()
    if (returnValue is Thing<*>) {
      return (returnValue.normalize() as Thing<*>).redact()
    }

    return returnValue
  }

  private fun validateParameters(arguments: Array<Any>): List<ValidationResult<*>> =
    arguments
      .filterIsInstance<Thing<*>>()
      .map { (it.normalize() as Thing<*>).validate() }
      .filterIsInstance<ValidationResult.Invalid<*>>()

  private fun isFactoryBeanMetadataMethod(method: Method): Boolean {
    val clazz: Class<*> = method.declaringClass

    if (clazz.isInterface) {
      return (clazz == FactoryBean::class.java || clazz == SmartFactoryBean::class.java) &&
        !method.name.equals("getObject")
    }

    var factoryBeanType: Class<*>? = null
    if (SmartFactoryBean::class.java.isAssignableFrom(clazz)) {
      factoryBeanType = SmartFactoryBean::class.java
    } else if (FactoryBean::class.java.isAssignableFrom(clazz)) {
      factoryBeanType = FactoryBean::class.java
    }
    return factoryBeanType != null && !method.name.equals("getObject") &&
      ClassUtils.hasMethod(factoryBeanType, method)
  }
}
