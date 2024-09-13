package io.foxcapades.kt.prop.delegation

import kotlin.reflect.KProperty

interface MutableDelegateProperty<T, A : T?> : MutableProperty<T>, DelegateProperty<T, A> {
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
  operator fun setValue(thisRef: Any?, property: KProperty<*>, value: A)
}
