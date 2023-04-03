package thing.validator

import kotlin.reflect.KProperty1

@DslMarker
annotation class ValidationDsl

@ValidationDsl
open class ValidationBuilder<T> {
  companion object {
    private enum class PropModifier {
      Required, Optional, OptionalRequired
    }

    private interface PropKey<T> {
      fun build(builder: ValidationBuilder<*>): Validation<T>
    }

    private data class SingleValuePropKey<T, R>(
      val property: KProperty1<T, R>,
      val modifier: PropModifier
    ): PropKey<T> {
      override fun build(builder: ValidationBuilder<*>): Validation<T> {
        @Suppress("UNCHECKED_CAST")
        val validations = (builder as ValidationBuilder<R>).build()
        return when (modifier) {
          PropModifier.Required -> NonNullPropertyValidation(property, validations)
          PropModifier.Optional -> OptionalPropertyValidation(property, validations)
          PropModifier.OptionalRequired -> RequiredPropertyValidation(property, validations)
        }
      }
    }

    private data class IterablePropKey<T, R>(
      val property: KProperty1<T, Iterable<R>>,
      val modifier: PropModifier
    ): PropKey<T> {
      override fun build(builder: ValidationBuilder<*>): Validation<T> {
        @Suppress("UNCHECKED_CAST")
        val validations = (builder as ValidationBuilder<R>).build()
        @Suppress("UNCHECKED_CAST")
        return when (modifier) {
          PropModifier.Required -> NonNullPropertyValidation(property, IterableValidation(validations))
          PropModifier.Optional -> OptionalPropertyValidation(property, IterableValidation(validations))
          PropModifier.OptionalRequired -> RequiredPropertyValidation(property, IterableValidation(validations))
        }
      }
    }

    private data class ArrayPropKey<T, R>(
      val property: KProperty1<T, Array<R>>,
      val modifier: PropModifier
    ): PropKey<T> {
      override fun build(builder: ValidationBuilder<*>): Validation<T> {
        @Suppress("UNCHECKED_CAST")
        val validations = (builder as ValidationBuilder<R>).build()
        @Suppress("UNCHECKED_CAST")
        return when (modifier) {
          PropModifier.Required -> NonNullPropertyValidation(property, ArrayValidation(validations))
          PropModifier.Optional -> OptionalPropertyValidation(property, ArrayValidation(validations))
          PropModifier.OptionalRequired -> RequiredPropertyValidation(property, ArrayValidation(validations))
        }
      }
    }

    private data class MapPropKey<T, K, V>(
      val property: KProperty1<T, Map<K, V>>,
      val modifier: PropModifier
    ): PropKey<T> {
      override fun build(builder: ValidationBuilder<*>): Validation<T> {
        @Suppress("UNCHECKED_CAST")
        val validations = (builder as ValidationBuilder<Map.Entry<K, V>>).build()
        return when (modifier) {
          PropModifier.Required -> NonNullPropertyValidation(property, MapValidation(validations))
          PropModifier.Optional -> OptionalPropertyValidation(property, MapValidation(validations))
          PropModifier.OptionalRequired -> RequiredPropertyValidation(property, MapValidation(validations))
        }
      }
    }
  }

  private val constraints = mutableListOf<Constraint<T>>()
  private val subValidations = mutableMapOf<PropKey<T>, ValidationBuilder<*>>()
  private val prebuiltValidations = mutableListOf<Validation<T>>()

  open infix fun Constraint<T>.hint(hint: String): Constraint<T> =
    Constraint(hint, this.templateValues, this.test).also { constraints.remove(this); constraints.add(it) }

  open fun addConstraint(errorMessage: String, vararg templateValues: String, test: (T) -> Boolean): Constraint<T> {
    return Constraint(errorMessage, templateValues.toList(), test).also { constraints.add(it) }
  }

  private fun <R> KProperty1<T, R?>.getOrCreateBuilder(modifier: PropModifier): ValidationBuilder<R> {
    val key = SingleValuePropKey(this, modifier)
    @Suppress("UNCHECKED_CAST")
    return (subValidations.getOrPut(key) { ValidationBuilder<R>() } as ValidationBuilder<R>)
  }

  private fun <R> KProperty1<T, Iterable<R>>.getOrCreateIterablePropertyBuilder(modifier: PropModifier): ValidationBuilder<R> {
    val key = IterablePropKey(this, modifier)
    @Suppress("UNCHECKED_CAST")
    return (subValidations.getOrPut(key) { ValidationBuilder<R>() } as ValidationBuilder<R>)
  }

  private fun <R> PropKey<T>.getOrCreateBuilder(): ValidationBuilder<R> {
    @Suppress("UNCHECKED_CAST")
    return (subValidations.getOrPut(this) { ValidationBuilder<R>() } as ValidationBuilder<R>)
  }

  open operator fun <R> KProperty1<T, R>.invoke(init: ValidationBuilder<R>.() -> Unit) {
    getOrCreateBuilder(PropModifier.Required).also(init)
  }

  private fun <R> onEachIterable(prop: KProperty1<T, Iterable<R>>, init: ValidationBuilder<R>.() -> Unit) {
    prop.getOrCreateIterablePropertyBuilder(PropModifier.Required).also(init)
  }

  private fun <R> onEachArray(prop: KProperty1<T, Array<R>>, init: ValidationBuilder<R>.() -> Unit) {
    ArrayPropKey(prop, PropModifier.Required).getOrCreateBuilder<R>().also(init)
  }

  private fun <K, V> onEachMap(prop: KProperty1<T, Map<K, V>>, init: ValidationBuilder<Map.Entry<K, V>>.() -> Unit) {
    MapPropKey(prop, PropModifier.Required).getOrCreateBuilder<Map.Entry<K, V>>().also(init)
  }

  open infix fun <R> KProperty1<T, R?>.ifPresent(init: ValidationBuilder<R>.() -> Unit) {
    getOrCreateBuilder(PropModifier.Optional).also(init)
  }

  open infix fun <R> KProperty1<T, R?>.required(init: ValidationBuilder<R>.() -> Unit) {
    getOrCreateBuilder(PropModifier.OptionalRequired).also(init)
  }

  val <R> KProperty1<T, R>.has: ValidationBuilder<R>
    get() = getOrCreateBuilder(PropModifier.Required)

  fun run(validation: Validation<T>) {
    prebuiltValidations.add(validation)
  }

  fun build(): Validation<T> {
    val nestedValidations = subValidations.map { (key, builder) ->
      key.build(builder)
    }
    return ValidationNode(constraints, nestedValidations + prebuiltValidations)
  }
}

fun <T: Any> ValidationBuilder<T?>.ifPresent(init: ValidationBuilder<T>.() -> Unit) {
  val builder = ValidationBuilder<T>()
  init(builder)
  run(OptionalValidation(builder.build()))
}

fun <T: Any> ValidationBuilder<T?>.required(init: ValidationBuilder<T>.() -> Unit) {
  val builder = ValidationBuilder<T>()
  init(builder)
  run(RequiredValidation(builder.build()))
}

@JvmName("onEachIterable")
infix fun <S, T: Iterable<S>> ValidationBuilder<T>.onEach(init: ValidationBuilder<S>.() -> Unit) {
  val builder = ValidationBuilder<S>()
  init(builder)
  @Suppress("UNCHECKED_CAST")
  run(IterableValidation(builder.build()) as Validation<T>)
}

@JvmName("onEachArray")
infix fun <T> ValidationBuilder<Array<T>>.onEach(init: ValidationBuilder<T>.() -> Unit) {
  val builder = ValidationBuilder<T>()
  init(builder)
  @Suppress("UNCHECKED_CAST")
  run(ArrayValidation(builder.build()) as Validation<Array<T>>)
}

@JvmName("onEachMap")
infix fun <K, V, T: Map<K, V>> ValidationBuilder<T>.onEach(init: ValidationBuilder<Map.Entry<K, V>>.() -> Unit) {
  val builder = ValidationBuilder<Map.Entry<K, V>>()
  init(builder)
  @Suppress("UNCHECKED_CAST")
  run(MapValidation(builder.build()) as Validation<T>)
}
