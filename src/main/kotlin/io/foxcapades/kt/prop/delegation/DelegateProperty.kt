package io.foxcapades.kt.prop.delegation

import kotlin.reflect.KProperty

interface DelegateProperty<T, A : T?> : Property<T> {

  /**
   * Enables the use of [DelegateProperty] instances as read-only class property
   * delegates (`val`).
   *
   * If no value is currently set on this `Property`, this method will return
   * `null`.
   *
   * @param thisRef Reference to the instance containing the delegated property
   * that was accessed.
   *
   * @param property Reference to the class property that delegated access to
   * this `Property` instance.
   *
   * @return The value set for this property.
   */
  operator fun getValue(thisRef: Any?, property: KProperty<*>): A
}
