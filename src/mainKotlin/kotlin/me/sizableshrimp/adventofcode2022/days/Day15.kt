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

import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import me.sizableshrimp.adventofcode2022.templates.Coordinate
import me.sizableshrimp.adventofcode2022.templates.SeparatedDay
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

// https://adventofcode.com/2022/day/15 - Beacon Exclusion Zone
class Day15 : SeparatedDay() {
    private lateinit var sensors: List<Sensor>

    override fun part1(): Int {
        // We assume the answer to part 2 is not on the row y=2,000,000, so we can have a continuous line segment
        var minX = 0
        var maxX = 0
        val extraBeacons = HashSet<Coordinate>()

        for ((pos, beacon, maxDist) in this.sensors) {
            val yDist = abs(pos.y - 2000000)
            if (yDist > maxDist) continue // Does not contribute to the total

            val xDist = maxDist - yDist // The # of squares in either direction from the sensor X pos that cannot have a beacon
            minX = min(minX, pos.x - xDist)
            maxX = max(maxX, pos.x + xDist)

            if (beacon.y == 2000000) extraBeacons.add(beacon)
        }

        return maxX - minX + 1 - extraBeacons.size
    }

    override fun part2(): Long {
        var x = 0
        var y = 0
        val maxXY = 4000000
        val recalculateRows = IntOpenHashSet()
        recalculateRows.add(0)

        for ((pos, _, beaconDist) in this.sensors) {
            // We recalculate the sensors visible to a y-row whenever a sensor becomes in or out of range
            recalculateRows.add(pos.y - beaconDist)
            recalculateRows.add(pos.y + beaconDist)
        }

        val possibleSensors = ArrayList<Sensor>()

        while (true) {
            if (recalculateRows.contains(y)) {
                possibleSensors.clear()
                for (sensor in this.sensors) {
                    val yDist = abs(sensor.pos.y - y)
                    if (yDist <= sensor.beaconDist) possibleSensors.add(sensor)
                }
            }

            var found = true

            for ((pos, _, maxDist) in possibleSensors) {
                val yDist = abs(pos.y - y) // We guarantee that yDist is always lower than or equal to maxDist here
                val xDist = abs(pos.x - x)

                if (yDist + xDist <= maxDist) {
                    x = pos.x + maxDist - yDist + 1
                    if (x > maxXY) {
                        found = false
                        break
                    }
                }
            }

            if (found) return x * maxXY.toLong() + y

            x = 0
            y++
        }
    }

    override fun parse() {
        val sensors = ArrayList<Sensor>(this.lines.size)
        this.sensors = sensors

        for (line in this.lines) {
            val (sensor, beacon) = line.split(':').map(::getCoord)
            sensors.add(Sensor(sensor, beacon))
        }

        sensors.sortWith(Comparator.comparingInt { it.pos.x })
    }

    private fun getCoord(segment: String): Coordinate {
        val xEqualIdx = segment.indexOf('=')
        val commaIdx = segment.indexOf(',')

        return Coordinate.of(segment.substring(xEqualIdx + 1, commaIdx).toInt(), segment.substring(commaIdx + 4).toInt())
    }

    private data class Sensor(val pos: Coordinate, val beacon: Coordinate, val beaconDist: Int = pos.distance(beacon))

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day15().run()
        }
    }
}