package io.foxcapades.kt.prop.delegation

/**
 * Represents a generic value which may or may not exist.
 *
 * Similar to Java's [java.util.Optional] type, but allowing for class property
 * delegation.
 *
 * @param T Type of value this `Property` may contain.
 *
 * @since 1.0.0
 */
interface Property<T> {
  /**
   * Indicates whether this [Property] currently contains a value.
   *
   * If `isSet` is `false`, then calling [get] will result in an exception being
   * thrown.
   */
  val isSet: Boolean

  /**
   * Returns the value contained in this [Property] instance, if one is set.
   *
   * If no value is currently set on this `Property`, this method will throw an
   * exception.
   *
   * @return The value set for this property.
   *
   * @throws NoSuchValueException If this method is called when the property has
   * no value set.
   */
  @Throws(NoSuchValueException::class)
  fun get(): T
}
