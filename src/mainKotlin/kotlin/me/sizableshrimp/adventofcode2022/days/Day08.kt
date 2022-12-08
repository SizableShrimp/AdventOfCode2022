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

import me.sizableshrimp.adventofcode2022.helper.GridHelper
import me.sizableshrimp.adventofcode2022.templates.Coordinate
import me.sizableshrimp.adventofcode2022.templates.Day
import me.sizableshrimp.adventofcode2022.templates.Direction

// https://adventofcode.com/2022/day/8 - Treetop Tree House
class Day08 : Day() {
    private lateinit var grid: Array<IntArray>

    override fun evaluate(): Result {
        var visibleTrees = 2 * this.grid.size + 2 * this.grid[0].size - 4
        var maxScenicScore = Int.MIN_VALUE

        for (y in 1 until this.grid.size - 1) {
            val row = this.grid[y]

            for (x in 1 until row.size - 1) {
                val tree = row[x]
                var saw = false
                var scenicScore = 1

                for (dir in Direction.cardinalDirections()) {
                    var visibleOnDirection = 0
                    var coord = Coordinate.of(x, y)

                    while (true) {
                        coord = coord.resolve(dir)

                        if (!GridHelper.isValid(this.grid, coord)) {
                            if (!saw) {
                                visibleTrees++
                                saw = true
                            }
                            break
                        }

                        visibleOnDirection++

                        if (this.grid[coord.y][coord.x] >= tree) break
                    }

                    scenicScore *= visibleOnDirection
                }

                if (scenicScore > maxScenicScore) maxScenicScore = scenicScore
            }
        }

        return Result.of(visibleTrees, maxScenicScore)
    }

    override fun parse() {
        this.grid = GridHelper.convertInt(this.lines) { it.code - '0'.code }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day08().run()
        }
    }
}