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

import me.sizableshrimp.adventofcode2022.templates.Coordinate
import kotlin.math.max
import kotlin.math.min

/**
 * Calls the consumer for all (x, y) coordinates between this coordinate and the other coordinate, inclusive.
 */
inline fun Coordinate.betweenCoordsInclusive(other: Coordinate, consumer: (Int, Int) -> Unit) {
    val minY = min(this.y, other.y)
    val maxY = max(this.y, other.y)
    val minX = min(this.x, other.x)
    val maxX = max(this.x, other.x)

    for (y in minY..maxY) {
        for (x in minX..maxX) {
            consumer(x, y)
        }
    }
}