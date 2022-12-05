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

import it.unimi.dsi.fastutil.chars.CharArrayList
import it.unimi.dsi.fastutil.chars.CharList
import me.sizableshrimp.adventofcode2022.templates.SeparatedDay
import me.sizableshrimp.adventofcode2022.util.splitOnBlankLines

// https://adventofcode.com/2022/day/5 - Supply Stacks
class Day05 : SeparatedDay() {
    private lateinit var initialState: List<CharList>
    private lateinit var moves: List<Move>

    override fun part1() = simulate(false)

    override fun part2() = simulate(true)

    private fun simulate(moveExact: Boolean): String {
        val state = ArrayList<CharList>(this.initialState.size)
        for (list in this.initialState) {
            state.add(CharArrayList(list))
        }

        for (move in this.moves) {
            val from = state[move.fromIdx]
            val to = state[move.toIdx]
            val count = move.count
            val size = from.size

            for (i in 0 until count) {
                val subtract = if (moveExact) count else i + 1
                to.add(from.removeChar(size - subtract))
            }
        }

        val result = StringBuilder()
        for (list in state) {
            result.append(list.getChar(list.size - 1))
        }
        return result.toString()
    }

    override fun parse() {
        val linesSplit = this.lines.splitOnBlankLines()
        val numStacks = (this.lines[0].length + 1) / 4

        val initialState = ArrayList<CharList>(numStacks)
        this.initialState = initialState
        for (i in 0 until numStacks) {
            initialState.add(CharArrayList())
        }

        for (line in linesSplit[0]) {
            if (line.indexOf('[') == -1) continue

            for (idx in 0 until numStacks) {
                val crateId = line[idx * 4 + 1]
                if (Character.isLetter(crateId))
                    initialState[idx].add(0, crateId)
            }
        }

        val moves = ArrayList<Move>(linesSplit[1].size)
        this.moves = moves
        for (line in linesSplit[1]) {
            val split = line.split(" ")
            moves.add(Move(split[3].toInt() - 1, split[5].toInt() - 1, split[1].toInt()))
        }
    }

    data class Move(val fromIdx: Int, val toIdx: Int, val count: Int)

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day05().run()
        }
    }
}