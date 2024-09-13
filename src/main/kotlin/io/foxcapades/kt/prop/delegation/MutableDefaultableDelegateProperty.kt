package io.foxcapades.kt.prop.delegation

import kotlin.reflect.KProperty

interface MutableDefaultableDelegateProperty<T, A : T?> : MutableDefaultableProperty<T>, MutableDelegateProperty<T, A>
