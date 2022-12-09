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

import me.sizableshrimp.adventofcode2022.templates.Coordinate
import me.sizableshrimp.adventofcode2022.templates.Direction
import me.sizableshrimp.adventofcode2022.templates.SeparatedDay
import kotlin.math.abs

// https://adventofcode.com/2022/day/9 - Rope Bridge
class Day09 : SeparatedDay() {
    private lateinit var moves: List<Move>

    override fun part1() = simulate(1)

    override fun part2()  = simulate(9)

    private fun simulate(numKnots: Int): Int {
        val lastKnotIdx = numKnots - 1
        val visited = HashSet<Coordinate>()
        var head = Coordinate.ORIGIN
        visited.add(Coordinate.ORIGIN)
        val knots = ArrayList<Coordinate>(numKnots)
        for (i in 0 until numKnots) {
            knots.add(Coordinate.ORIGIN)
        }

        for ((dir, moveCount) in this.moves) {
            for (currMove in 0 until moveCount) {
                head = head.resolve(dir)

                for (i in 0 until numKnots) {
                    moveRope(if (i == 0) head else knots[i - 1], knots[i])?.apply {
                        knots[i] = this
                        if (i == lastKnotIdx) visited.add(this)
                    }
                }
            }
        }

        return visited.size
    }

    private fun moveRope(head: Coordinate, tail: Coordinate): Coordinate? {
        if (head == tail || abs(tail.x - head.x) <= 1 && abs(tail.y - head.y) <= 1)
            return null

        return when {
            head.x == tail.x -> tail.resolve(0, if (head.y > tail.y) 1 else -1)
            head.y == tail.y -> tail.resolve(if (head.x > tail.x) 1 else -1, 0)
            else -> tail.resolve(if (head.x > tail.x) 1 else -1, if (head.y > tail.y) 1 else -1)
        }
    }

    override fun parse() {
        val moves = ArrayList<Move>(lines.size)
        this.moves = moves

        for (line in this.lines) {
            val dir = Direction.getCardinalDirection(line[0])
            val count = line.substring(2).toInt()
            moves.add(Move(dir, count))
        }
    }

    private data class Move(val dir: Direction, val count: Int)

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day09().run()
        }
    }
}