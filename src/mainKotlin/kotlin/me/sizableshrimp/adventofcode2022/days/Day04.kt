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

import me.sizableshrimp.adventofcode2022.templates.Day

class Day04 : Day() {
    override fun evaluate(): Result {
        var part1Count = 0
        var part2Count = 0

        for (line in this.lines) {
            val pairs = line.split(",")
            val pair1 = pairs[0].split("-")
            val pair2 = pairs[1].split("-")

            val aStart = pair1[0].toInt()
            val aEnd = pair1[1].toInt()
            val bStart = pair2[0].toInt()
            val bEnd = pair2[1].toInt()

            if (aStart <= bStart && bEnd <= aEnd || bStart <= aStart && aEnd <= bEnd) part1Count++
            if (bStart in aStart..aEnd || aStart in bStart..bEnd) part2Count++
        }

        return Result.of(part1Count, part2Count)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day04().run()
        }
    }
}