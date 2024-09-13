package io.foxcapades.kt.prop.delegation

import kotlin.reflect.KProperty

interface MutableDefaultableDelegateProperty<T> : MutableDefaultableProperty<T>, MutableDelegateProperty<T, T> {
  /**
   * Overrides the parent implementation [DelegateProperty.getValue] and
   * replaces type `T?` with `T`.
   *
   * @param thisRef Reference to the instance containing the delegated property
   * that was accessed.
   *
   * @param property Reference to the class property that delegated access to
   * this `Property` instance.
   *
   * @return Either the value of this property, or its default fallback value.
   *
   * @throws NoSuchValueException If neither a value nor default value are set
   * on this `DefaultableProperty`.
   */
  @Throws(NoSuchValueException::class)
  override operator fun getValue(thisRef: Any?, property: KProperty<*>): T

  /**
   * Enables the use of [MutableProperty] instances as mutable class property
   * delegates (`var`).
   *
   * @param thisRef Reference to the instance containing the delegated property
   * that was accessed.
   *
   * @param property Reference to the class property that delegated access to
   * this `Property` instance.
   *
   * @param value Value to set for this `MutableProperty`.
   */
  override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T)
}

