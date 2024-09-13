package io.foxcapades.kt.prop.delegation

@JvmInline
internal value class BasicProperty<T>(private val value: T) : Property<T> {
  override val isSet
    get() = true

  override fun get() = value
}
