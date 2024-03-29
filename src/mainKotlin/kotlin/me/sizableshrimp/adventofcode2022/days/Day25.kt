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

// https://adventofcode.com/2022/day/25 - Full of Hot Air
class Day25 : SeparatedDay() {
    override fun part1() = this.lines.sumOf { l -> l.fold(0L) { acc, c -> 5 * acc + ("=-012".indexOf(c) - 2) } }
        .let { d ->
            generateSequence(d) { it / 5 + if (it % 5 >= 3) 1 else 0 }
                .takeWhile { it != 0L }
                .map { "012=-"[(it % 5).toInt()] }
                .joinToString("").reversed()
        }

    // No Part 2 :)
    override fun part2() = null

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day25().run()
        }
    }
}
