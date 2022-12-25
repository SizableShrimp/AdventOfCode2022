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
    override fun part1() = convertToBalancedQuinary(this.lines.sumOf(::convertFromBalancedQuinary))

    // No Part 2 :)
    override fun part2() = null

    private fun convertFromBalancedQuinary(numStr: String): Long {
        var num = 0L
        var power = 1L

        for (i in numStr.length - 1 downTo 0) {
            num += power * when (numStr[i]) {
                '2' -> 2
                '1' -> 1
                '0' -> 0
                '-' -> -1
                '=' -> -2
                else -> throw IllegalStateException()
            }
            power *= 5
        }

        return num
    }

    private fun convertToBalancedQuinary(inputNum: Long): String {
        var num = inputNum
        val result = StringBuilder()

        while (num > 0) {
            val remainder = (num % 5).toInt()
            num /= 5
            if (remainder > 2) num++

            result.insert(
                0, when (remainder) {
                    0 -> '0'
                    1 -> '1'
                    2 -> '2'
                    3 -> '='
                    4 -> '-'
                    else -> throw IllegalStateException()
                }
            )
        }

        return result.toString()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day25().run()
        }
    }
}
