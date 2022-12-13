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
import me.sizableshrimp.adventofcode2022.templates.SeparatedDay

// https://adventofcode.com/2022/day/13 - Distress Signal
class Day13 : SeparatedDay() {
    private lateinit var packets: List<List<*>>

    override fun part1(): Int {
        var sum = 0
        for ((idx, pair) in this.packets.windowed(2, step = 2).withIndex()) {
            if (comparePackets(pair[0], pair[1]) < 0)
                sum += idx + 1
        }

        return sum
    }

    override fun part2(): Int {
        // A full sort for part 2 is not necessarily.
        // You can just go through each packet once to see if it is before either divider packet.
        var firstKey = 1 // Index shifted by +1
        var secondKey = 2 // Includes first divider packet and +1 index shift

        for (packet in this.packets) {
            if (comparePackets(packet, FIRST_DIVIDER_PACKET) < 0) {
                firstKey++
                secondKey++
            } else if (comparePackets(packet, SECOND_DIVIDER_PACKET) < 0) {
                secondKey++
            }
        }

        return firstKey * secondKey
    }

    // <0 = in order, 0 = continue, >0 = out of order
    private fun comparePackets(first: Any, second: Any): Int {
        val firstIsInt = first is Int
        val secondIsInt = second is Int
        if (firstIsInt && secondIsInt) {
            return (first as Int).compareTo(second as Int)
        } else if (firstIsInt) {
            return comparePackets(listOf(first), second)
        } else if (secondIsInt) {
            return comparePackets(first, listOf(second))
        }

        if (first !is List<*> || second !is List<*>) throw IllegalStateException()

        for ((idx, sub) in first.withIndex()) {
            if (idx >= second.size)
                return 1

            val subCompare = comparePackets(sub!!, second[idx]!!)
            if (subCompare != 0)
                return subCompare
        }

        return if (first.size == second.size) 0 else -1
    }

    override fun parse() {
        val packets = ArrayList<List<*>>()
        this.packets = packets

        for (line in this.lines) {
            if (line.isBlank())
                continue

            packets.add(this.parse(line) as List<*>)
        }
    }

    private fun parse(str: String): Any {
        if (str[0] != '[')
            return str.toInt()
        if (str.length == 2)
            return emptyList<Any>() // []

        val list = ArrayList<Any>()
        var nestedCount = 0
        var lastIdx = 1
        val end = str.length - 1

        for (i in 1 until end) {
            when (str[i]) {
                '[' -> nestedCount++
                ']' -> nestedCount--
                ',' -> {
                    if (nestedCount == 0) {
                        list.add(parse(str.substring(lastIdx, i)))
                        lastIdx = i + 1
                    }
                }
            }
        }
        list.add(parse(str.substring(lastIdx, end)))

        return list
    }

    companion object {
        private val FIRST_DIVIDER_PACKET = listOf(IntList.of(2))
        private val SECOND_DIVIDER_PACKET = listOf(IntList.of(6))

        @JvmStatic
        fun main(args: Array<String>) {
            Day13().run()
        }
    }
}