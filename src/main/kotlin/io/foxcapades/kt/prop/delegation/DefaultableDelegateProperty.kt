package io.foxcapades.kt.prop.delegation

interface DefaultableDelegateProperty<T, A : T?> : DefaultableProperty<T>, DelegateProperty<T, A>
