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

package me.sizableshrimp.adventofcode2022.days

import me.sizableshrimp.adventofcode2022.templates.SeparatedDay

class Day03 : SeparatedDay() {
    override fun part1() = this.lines.sumOf {
        val middle = it.length / 2
        getPriority(it.toCharArray(endIndex = middle).intersect(it.toCharArray(startIndex = middle).toSet()).first())
    }

    override fun part2() = this.lines.windowed(3, 3, false).sumOf {
        val charSet = it[0].toCharArray().toMutableSet()
        charSet.retainAll(it[1].toCharArray().toSet())
        charSet.retainAll(it[2].toCharArray().toSet())
        getPriority(charSet.first())
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day03().run()
        }

        private fun getPriority(c: Char) = if (Character.isLowerCase(c)) c.code - 'a'.code + 1 else c.code - 'A'.code + 27
    }
}