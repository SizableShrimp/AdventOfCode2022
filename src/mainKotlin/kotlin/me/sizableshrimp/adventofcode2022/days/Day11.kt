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
import me.sizableshrimp.adventofcode2022.templates.SeparatedDay
import me.sizableshrimp.adventofcode2022.util.splitOnBlankLines

// https://adventofcode.com/2022/day/11 - Monkey in the Middle
class Day11 : SeparatedDay() {
    private lateinit var monkeys: List<Monkey>
    private var numMonkeys = 0

    override fun part1() = simulate(false)

    override fun part2() = simulate(true)

    private fun simulate(part2: Boolean): Long {
        val itemLists: MutableList<IntList> = ArrayList(this.numMonkeys)
        var maxWorryLevel = 1
        val monkeyBusiness = IntArray(this.numMonkeys)

        for ((startingItems, _, divisibleBy) in this.monkeys) {
            if (part2) maxWorryLevel *= divisibleBy
            itemLists.add(IntArrayList(startingItems))
        }

        val rounds = if (part2) 10000 else 20
        for (round in 0 until rounds) {
            for (monkeyIdx in 0 until numMonkeys) {
                val monkey = this.monkeys[monkeyIdx]
                val items = itemLists[monkeyIdx]
                if (items.isEmpty())
                    continue

                for (i in items.indices) {
                    var worryLevel = items.getInt(i)
                    val newWorryLevel = monkey.operation.apply(worryLevel)
                    worryLevel = if (part2) (newWorryLevel % maxWorryLevel).toInt() else newWorryLevel.toInt() / 3
                    monkeyBusiness[monkeyIdx]++
                    itemLists[if (worryLevel % monkey.divisibleBy == 0) monkey.throwTrue else monkey.throwFalse].add(worryLevel)
                }

                items.clear()
            }
        }

        var firstMax = Int.MIN_VALUE
        var secondMax = Int.MIN_VALUE

        for (itemsInspected in monkeyBusiness) {
            if (itemsInspected > firstMax) {
                secondMax = firstMax
                firstMax = itemsInspected
            } else if (itemsInspected > secondMax) {
                secondMax = itemsInspected
            }
        }

        return firstMax * secondMax.toLong()
    }

    override fun parse() {
        val splitMonkeys = this.lines.splitOnBlankLines()
        this.numMonkeys = splitMonkeys.size
        val monkeys = ArrayList<Monkey>(numMonkeys)
        this.monkeys = monkeys

        for (monkeyData in splitMonkeys) {
            val startingItemStrings = monkeyData[1].substring(18).split(", ")
            val startingItems = IntArrayList(startingItemStrings.size)
            for (num in startingItemStrings) {
                startingItems.add(num.toInt())
            }

            val operationStr = monkeyData[2]
            val mathPart = operationStr.substring(25)
            val operation = if (mathPart == "old") Operation(true, false, 0) else Operation(false, operationStr.contains('+'), mathPart.toInt())
            val divisibleBy = monkeyData[3].substring(21).toInt()
            val throwTrue = monkeyData[4].substring(29).toInt()
            val throwFalse = monkeyData[5].substring(30).toInt()

            monkeys.add(Monkey(startingItems, operation, divisibleBy, throwTrue, throwFalse))
        }
    }

    private data class Monkey(val startingItems: IntList, val operation: Operation, val divisibleBy: Int, val throwTrue: Int, val throwFalse: Int)

    private class Operation(val squared: Boolean, val add: Boolean, val amount: Int) {
        // These operations can and do produce a value higher than Int.MAX_VALUE before the modulo is taken and the number is brought back down to an int
        fun apply(worryLevel: Int): Long {
            if (this.squared)
                return worryLevel * worryLevel.toLong()

            return if (this.add) worryLevel + this.amount.toLong() else worryLevel * this.amount.toLong()
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day11().run()
        }
    }
}