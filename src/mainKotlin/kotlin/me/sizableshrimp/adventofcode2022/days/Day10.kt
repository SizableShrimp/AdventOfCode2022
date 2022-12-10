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

import it.unimi.dsi.fastutil.ints.IntArrayList
import it.unimi.dsi.fastutil.ints.IntList
import me.sizableshrimp.adventofcode2022.helper.LetterParser
import me.sizableshrimp.adventofcode2022.templates.Day
import kotlin.math.abs

// https://adventofcode.com/2022/day/10 - Cathode-Ray Tube
class Day10 : Day() {
    private val instructions: IntList = IntArrayList(this.lines.size)

    override fun evaluate(): Result {
        var register = 1
        var i = 0
        var cycle = 1
        val max = this.instructions.size
        var toAdd = 0
        var adding = false
        var signalStrength = 0
        val grid = Array(6) { BooleanArray(40) }

        while (i < max) {
            if (cycle == 20 || (cycle > 20 && (cycle - 20) % 40 == 0))
                signalStrength += cycle * register

            val row = cycle / 40
            val column = cycle % 40 - 1
            if (abs(register - column) <= 1)
                grid[row][column] = true

            cycle++

            if (adding) {
                adding = false
                register += toAdd
                continue
            }

            val insn = this.instructions.getInt(i)
            i++

            if (insn == 0)
                continue // noop

            // addx
            toAdd = insn
            adding = true
        }

        return Result.of(signalStrength, LetterParser.getLettersOrGrid(grid).trim())
    }

    override fun parse() {
        this.instructions.clear()

        for (line in this.lines) {
            this.instructions.add(if (line[0] == 'a') /* addx */ line.substring(5).toInt() else /* noop */ 0)
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day10().run()
        }
    }
}