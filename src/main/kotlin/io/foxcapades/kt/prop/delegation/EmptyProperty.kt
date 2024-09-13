package io.foxcapades.kt.prop.delegation

internal object EmptyProperty : Property<Any?> {
  @Suppress("UNCHECKED_CAST")
  fun <T> cast() = this as Property<T>

  override val isSet
    get() = false

  override fun get() =
    throw NoSuchValueException()
}
