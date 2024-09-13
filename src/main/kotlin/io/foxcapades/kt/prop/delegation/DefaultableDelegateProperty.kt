package io.foxcapades.kt.prop.delegation

import kotlin.reflect.KProperty

interface DefaultableDelegateProperty<T> : DefaultableProperty<T>, DelegateProperty<T, T> {
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
}

