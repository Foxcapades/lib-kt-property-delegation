package io.foxcapades.kt.prop.delegation

/**
 * Represents a replaceable/settable generic value which may or may not exist.
 *
 * @param T Type of value this `MutableProperty` may contain.
 *
 * @since 1.0.0
 */
interface MutableProperty<T> : Property<T> {
  /**
   * Sets the value wrapped by this `MutableProperty` to the value provided.
   *
   * If this `MutableProperty` was already set, the provided value will replace
   * the previous one.
   *
   * @param value Value to set for this `MutableProperty`.
   */
  fun set(value: T)

  /**
   * Removes any value previously set on this `MutableProperty` instance.
   *
   * If this `MutableProperty` instance was already unset, this method does
   * nothing.
   *
   * After this method returns, [isSet] will return `false`, and calls to [get]
   * will throw an exception until a new value has been [set].
   */
  fun unset()
}

