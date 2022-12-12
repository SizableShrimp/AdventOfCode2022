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

import it.unimi.dsi.fastutil.objects.Object2IntMap
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import me.sizableshrimp.adventofcode2022.helper.GridHelper
import me.sizableshrimp.adventofcode2022.templates.Coordinate
import me.sizableshrimp.adventofcode2022.templates.Day
import me.sizableshrimp.adventofcode2022.templates.Direction
import java.util.ArrayDeque
import java.util.Queue

// https://adventofcode.com/2022/day/12 - Hill Climbing Algorithm
class Day12 : Day() {
    private lateinit var start: Coordinate
    private lateinit var end: Coordinate
    private lateinit var grid: Array<IntArray>

    override fun evaluate(): Result? {
        val steps = Object2IntOpenHashMap<Coordinate>()
        val queue = ArrayDeque<Coordinate>()
        queue.add(this.start)

        val part1 = findShortestPath(steps, queue)

        // We can reuse the old steps to slightly speedup locations which are already fastest reached from the original start
        for (y in this.grid.indices) {
            for (x in this.grid[0].indices) {
                if (this.grid[y][x] == 0) {
                    val coord = Coordinate.of(x, y)
                    queue.add(coord)
                    steps.put(coord, 0)
                }
            }
        }

        val part2 = findShortestPath(steps, queue)

        return Result.of(part1, part2)
    }

    private fun findShortestPath(steps: Object2IntMap<Coordinate>, queue: Queue<Coordinate>): Int {
        while (!queue.isEmpty()) {
            val pos = queue.remove()
            val curHeight = this.grid[pos.y][pos.x]

            for (dir in Direction.cardinalDirections()) {
                val newPos = pos.resolve(dir)
                if (!GridHelper.isValid(grid, newPos)) continue

                if (curHeight >= this.grid[newPos.y][newPos.x] - 1) {
                    val newSteps = steps.getInt(pos) + 1
                    if (!steps.containsKey(newPos) || steps.getInt(newPos) > newSteps) {
                        queue.add(newPos)
                        steps.put(newPos, newSteps)
                    }
                }
            }
        }

        return steps.getInt(this.end)
    }

    override fun parse() {
        this.start = GridHelper.findCoordinate(this.lines, 'S')
        this.end = GridHelper.findCoordinate(this.lines, 'E')
        this.grid = GridHelper.convertInt(this.lines) {
            when (it) {
                'S' -> 0
                'E' -> 25
                else -> it.code - 'a'.code
            }
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day12().run()
        }
    }
}