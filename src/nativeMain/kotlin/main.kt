package com.lorenzoog.nativelibrary

import kotlinx.cinterop.*
import platform.posix.size_t

inline fun Heap(size: size_t): CPointer<heap_t>? {
  return heap_create(size)
}

inline fun <reified T : CVariable> CPointer<heap_t>?.use(f: (CPointer<T>) -> Unit) {
  alloc<T>()
    ?.also(f)
    ?.also(::free)
}

inline fun <reified T : CVariable> CPointer<heap_t>?.alloc(): CPointer<T>? {
  return heap_alloc(this, sizeOf<T>().toULong())?.reinterpret()
}

inline fun CPointer<heap_t>?.free(ptr: CPointer<*>?): Boolean {
  return heap_free(this, ptr)?.pointed?.value ?: false
}

fun main(): Unit = memScoped {
  val heap = Heap(1024u)
  heap.use<IntVar> { ptr ->
    ptr.pointed.value = 40
    println("PTR ${ptr.pointed}")
  }
}