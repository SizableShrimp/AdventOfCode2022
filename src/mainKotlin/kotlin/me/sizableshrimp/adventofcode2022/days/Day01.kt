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

import it.unimi.dsi.fastutil.ints.IntList
import me.sizableshrimp.adventofcode2022.util.splitOnBlankLines
import me.sizableshrimp.adventofcode2022.templates.Day
import me.sizableshrimp.adventofcode2022.util.toInts

// https://adventofcode.com/2022/day/1 - Calorie Counting
class Day01 : Day() {
    private val inventories = ArrayList<IntList>()

    override fun evaluate(): Result {
        val maxCalories = this.inventories.map { it.sum() }.sortedDescending().take(3)

        return Result.of(maxCalories[0], maxCalories.sum())
    }

    override fun parse() {
        this.inventories.clear()

        this.lines.splitOnBlankLines().forEach { this.inventories.add(it.toInts()) }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day01().run()
        }
    }
}