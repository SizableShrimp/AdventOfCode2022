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
import me.sizableshrimp.adventofcode2022.templates.SeparatedDay
import me.sizableshrimp.adventofcode2022.util.betweenCoordsInclusive

// https://adventofcode.com/2022/day/14 - Regolith Reservoir
class Day14 : SeparatedDay() {
    private val startingRocks: MutableSet<Coordinate> = HashSet()
    private var maxY = 0

    override fun part1() = simulate(false)

    override fun part2() = simulate(true)

    private fun simulate(part2: Boolean): Int {
        val rocks: MutableSet<Coordinate> = HashSet(this.startingRocks)
        var totalResting = 0
        var sandPos = STARTING_SAND_POS
        val maxY = if (part2) this.maxY + 1 else this.maxY

        while (true) {
            val below = sandPos.down()

            // For part 1, this is when the abyss starts
            // For part 2, this is when the infinite horizontal line starts
            if (below.y > maxY) {
                if (part2) {
                    rocks.add(sandPos)
                    totalResting++
                    sandPos = STARTING_SAND_POS
                    continue
                } else {
                    break
                }
            }

            sandPos = if (!rocks.contains(below)) {
                below
            } else {
                val left = below.left()
                val right = below.right()
                if (!rocks.contains(left)) {
                    left
                } else if (!rocks.contains(right)) {
                    right
                } else {
                    rocks.add(sandPos)
                    totalResting++
                    if (sandPos === STARTING_SAND_POS) break
                    STARTING_SAND_POS
                }
            }
        }

        return totalResting
    }

    override fun parse() {
        this.startingRocks.clear()

        for (line in this.lines) {
            var coord: Coordinate? = null

            for (coordStr in line.splitToSequence(" -> ")) {
                val nextCoord = Coordinate.parse(coordStr)

                coord?.betweenCoordsInclusive(nextCoord) { x, y ->
                    if (y > this.maxY) this.maxY = y
                    this.startingRocks.add(Coordinate.of(x, y))
                }

                coord = nextCoord
            }
        }
    }

    companion object {
        private val STARTING_SAND_POS: Coordinate = Coordinate.of(500, 0)

        @JvmStatic
        fun main(args: Array<String>) {
            Day14().run()
        }
    }
}