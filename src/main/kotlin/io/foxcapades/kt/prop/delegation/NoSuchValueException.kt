package io.foxcapades.kt.prop.delegation

open class NoSuchValueException(msg: String) : RuntimeException(msg) {
  constructor() : this("attempted to unwrap a value that had not been set")
}
