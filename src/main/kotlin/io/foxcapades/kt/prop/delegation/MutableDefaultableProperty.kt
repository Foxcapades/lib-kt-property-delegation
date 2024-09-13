package io.foxcapades.kt.prop.delegation

interface MutableDefaultableProperty<T> : DefaultableProperty<T>, MutableProperty<T>
