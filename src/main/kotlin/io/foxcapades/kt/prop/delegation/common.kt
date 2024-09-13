@file:JvmName("CommonUtils")
@file:Suppress("NOTHING_TO_INLINE")
package io.foxcapades.kt.prop.delegation

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

// region Base Property

inline fun <T> Property<T>.getOrNull(): T? = if (isSet) get() else null

inline fun <T> Property<T>.getOr(fallback: T): T = getOrNull() ?: fallback

inline val <T> Property<T>.isEmpty get() = !isSet

@OptIn(ExperimentalContracts::class)
inline fun <T> Property<T>.getOrCompute(fn: () -> T): T {
  contract { callsInPlace(fn, InvocationKind.AT_MOST_ONCE) }
  return getOrNull() ?: fn()
}

@OptIn(ExperimentalContracts::class)
inline fun <T> Property<T>.ifPresent(fn: (T) -> Unit) {
  contract { callsInPlace(fn, InvocationKind.AT_MOST_ONCE) }
  if (isSet)
    fn(get())
}

@OptIn(ExperimentalContracts::class)
inline fun Property<*>.ifAbsent(fn: () -> Unit) {
  contract { callsInPlace(fn, InvocationKind.AT_MOST_ONCE) }
  if (isEmpty)
    fn()
}

@OptIn(ExperimentalContracts::class)
fun <T, R> Property<T>.map(fn: (T) -> R): Property<R> {
  contract { callsInPlace(fn, InvocationKind.AT_MOST_ONCE) }
  return when {
    isSet -> BasicProperty(fn(get()))
    else  -> EmptyProperty.cast()
  }
}

@OptIn(ExperimentalContracts::class)
fun <T, R> Property<T>.flatMap(fn: (T) -> Property<R>): Property<R> {
  contract { callsInPlace(fn, InvocationKind.AT_MOST_ONCE) }
  return when {
    isSet -> fn(get())
    else  -> EmptyProperty.cast()
  }
}

inline fun <T> Property<T>.asSequence(): Sequence<T> =
  sequence { if (isSet) yield(get()) }

inline fun <T> Property<T>.asIterator(): Iterator<T> =
  iterator { if (isSet) yield(get()) }

inline fun <T> Property<T>.asIterable(): Iterable<T> =
  Iterable { asIterator() }

// endregion Base Property


/**
 * Returns either the current value of the receiver [DefaultableProperty], if
 * one is set, or the default value, if one is set.
 *
 * If neither a value nor a default value have been set on the receiver
 * `DefaultableProperty`, this method will throw an exception.
 *
 * @return Either the value of this property, or its default fallback value.
 *
 * @throws NoSuchValueException If neither a value nor default value are set
 * on the receiver `DefaultableProperty`.
 */
@Throws(NoSuchValueException::class)
inline fun <T> DefaultableProperty<T>.getOrDefault(): T =
  // if-else is a little funny here to intentionally put get() in the else block
  // so we throw the no-such-value error from the receiver property.
  if (!isSet && hasDefault) getDefault() else get()

inline fun <T> DefaultableProperty<T>.getOrDefaultOrNull(): T? =
  when {
    isSet      -> get()
    hasDefault -> getDefault()
    else       -> null
  }

inline fun <T> DefaultableProperty<T>.getOrDefaultOr(fallback: T): T = getOrDefaultOrNull() ?: fallback

@OptIn(ExperimentalContracts::class)
inline fun <T> DefaultableProperty<T>.getOrDefaultOrCompute(fn: () -> T): T {
  contract { callsInPlace(fn, InvocationKind.AT_MOST_ONCE) }
  return getOrDefaultOrNull() ?: fn()
}
