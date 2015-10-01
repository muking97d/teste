/*
 * Copyright (C) 2015 porokoro
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.github.porokoro.paperboy.extensions

import android.util.SparseArray

internal operator fun <T> SparseArray<T>.iterator() = SparseArrayIterator(this)

internal inline fun <T> SparseArray<T>.forEach(operation: (Pair<Int, T>) -> Unit) {
    for (element in this) operation(element)
}

internal inline fun <T> SparseArray<T>.firstOrNull(predicate: (Pair<Int, T>) -> Boolean): Pair<Int, T>? {
    for (element in this) if (predicate(element)) return element
    return null
}

internal class SparseArrayIterator<T>(private val array: SparseArray<T>) : Iterator<Pair<Int, T>> {
    private var index = 0

    override fun hasNext(): Boolean = index < array.size()

    override fun next(): Pair<Int, T> {
        val value = array.keyAt(index) to array.valueAt(index)
        index++
        return value
    }
}
