/*
 * AdventOfCode2022
 * Copyright (C) 2022 SizableShrimp
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.sizableshrimp.adventofcode2022.util

import it.unimi.dsi.fastutil.booleans.BooleanArrayList
import it.unimi.dsi.fastutil.booleans.BooleanList
import it.unimi.dsi.fastutil.bytes.ByteCollection
import it.unimi.dsi.fastutil.chars.CharArrayList
import it.unimi.dsi.fastutil.chars.CharCollection
import it.unimi.dsi.fastutil.chars.CharList
import it.unimi.dsi.fastutil.doubles.DoubleArrayList
import it.unimi.dsi.fastutil.doubles.DoubleCollection
import it.unimi.dsi.fastutil.doubles.DoubleList
import it.unimi.dsi.fastutil.floats.FloatCollection
import it.unimi.dsi.fastutil.ints.IntArrayList
import it.unimi.dsi.fastutil.ints.IntCollection
import it.unimi.dsi.fastutil.ints.IntList
import it.unimi.dsi.fastutil.longs.LongArrayList
import it.unimi.dsi.fastutil.longs.LongCollection
import it.unimi.dsi.fastutil.longs.LongList
import java.lang.IllegalArgumentException

fun <T> Iterable<T>.splitOn(splitter: (T) -> Boolean): List<List<T>> {
    val result = ArrayList<List<T>>()
    var subList = ArrayList<T>()
    result.add(subList)

    this.forEach {
        if (splitter(it)) {
            subList = ArrayList()
            result.add(subList)
        } else {
            subList.add(it)
        }
    }

    return result
}

fun <T> Iterable<T>.splitOnElement(element: T): List<List<T>> = splitOn { element == it }

fun Iterable<String>.splitOnBlankLines(): List<List<String>> = splitOn(String::isBlank)

fun List<String>.toInts(): IntList = this.mapTo(IntArrayList(this.size), String::toInt)

fun List<String>.toLongs(): LongList = this.mapTo(LongArrayList(this.size), String::toLong)

fun List<String>.toDoubles(): DoubleList = this.mapTo(DoubleArrayList(this.size), String::toDouble)

fun List<String>.toBooleans(): BooleanList = this.mapTo(BooleanArrayList(this.size), String::toBoolean)

fun List<String>.toChars(): CharList = this.mapTo(CharArrayList(this.size)) { it[0] }

fun IntCollection.max(): Int {
    val iterator = this.intIterator()
    if (!iterator.hasNext())
        throw IllegalArgumentException()

    var max = iterator.nextInt()

    while (iterator.hasNext()) {
        val num = iterator.nextInt()
        if (num > max)
            max = num
    }

    return max
}

fun LongCollection.max(): Long {
    val iterator = this.longIterator()
    if (!iterator.hasNext())
        throw IllegalArgumentException()

    var max = iterator.nextLong()

    while (iterator.hasNext()) {
        val num = iterator.nextLong()
        if (num > max)
            max = num
    }

    return max
}

fun CharCollection.max(): Char {
    val iterator = this.iterator()
    if (!iterator.hasNext())
        throw IllegalArgumentException()

    var max = iterator.nextChar()

    while (iterator.hasNext()) {
        val num = iterator.nextChar()
        if (num > max)
            max = num
    }

    return max
}

fun ByteCollection.max(): Byte {
    val iterator = this.iterator()
    if (!iterator.hasNext())
        throw IllegalArgumentException()

    var max = iterator.nextByte()

    while (iterator.hasNext()) {
        val num = iterator.nextByte()
        if (num > max)
            max = num
    }

    return max
}

fun DoubleCollection.max(): Double {
    val iterator = this.doubleIterator()
    if (!iterator.hasNext())
        throw IllegalArgumentException()

    var max = iterator.nextDouble()

    while (iterator.hasNext()) {
        val num = iterator.nextDouble()
        if (num > max)
            max = num
    }

    return max
}

fun FloatCollection.max(): Float {
    val iterator = this.iterator()
    if (!iterator.hasNext())
        throw IllegalArgumentException()

    var max = iterator.nextFloat()

    while (iterator.hasNext()) {
        val num = iterator.nextFloat()
        if (num > max)
            max = num
    }

    return max
}

fun IntCollection.min(): Int {
    val iterator = this.intIterator()
    if (!iterator.hasNext())
        throw IllegalArgumentException()

    var min = iterator.nextInt()

    while (iterator.hasNext()) {
        val num = iterator.nextInt()
        if (num < min)
            min = num
    }

    return min
}

fun LongCollection.min(): Long {
    val iterator = this.longIterator()
    if (!iterator.hasNext())
        throw IllegalArgumentException()

    var min = iterator.nextLong()

    while (iterator.hasNext()) {
        val num = iterator.nextLong()
        if (num < min)
            min = num
    }

    return min
}

fun CharCollection.min(): Char {
    val iterator = this.iterator()
    if (!iterator.hasNext())
        throw IllegalArgumentException()

    var min = iterator.nextChar()

    while (iterator.hasNext()) {
        val num = iterator.nextChar()
        if (num < min)
            min = num
    }

    return min
}

fun ByteCollection.min(): Byte {
    val iterator = this.iterator()
    if (!iterator.hasNext())
        throw IllegalArgumentException()

    var min = iterator.nextByte()

    while (iterator.hasNext()) {
        val num = iterator.nextByte()
        if (num < min)
            min = num
    }

    return min
}

fun DoubleCollection.min(): Double {
    val iterator = this.doubleIterator()
    if (!iterator.hasNext())
        throw IllegalArgumentException()

    var min = iterator.nextDouble()

    while (iterator.hasNext()) {
        val num = iterator.nextDouble()
        if (num < min)
            min = num
    }

    return min
}