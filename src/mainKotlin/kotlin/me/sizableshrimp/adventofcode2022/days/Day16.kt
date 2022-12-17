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

import it.unimi.dsi.fastutil.ints.Int2IntMap
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2IntMap
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import me.sizableshrimp.adventofcode2022.templates.SeparatedDay
import java.util.ArrayDeque
import java.util.Queue

// https://adventofcode.com/2022/day/16 - Proboscidea Volcanium
class Day16 : SeparatedDay() {
    private lateinit var aaValve: Valve
    private var valvesToOpen = 0

    override fun part1() = simulate(false)

    override fun part2() = simulate(true)

    private fun simulate(part2: Boolean): Int {
        val completed: Int2IntMap = Int2IntOpenHashMap()
        val queue: Queue<Node> = ArrayDeque() // new PriorityQueue<>(Comparator.comparing(n -> n.minutes));
        queue.add(Node(if (part2) 26 else 30, this.aaValve, 0, 0, 0))

        while (!queue.isEmpty()) {
            val node = queue.remove()
            if (node.minutes == 0) continue

            for (pathEntry in node.valve.children.object2IntEntrySet()) {
                val valve = pathEntry.key
                // If the valve is already opened or the AA valve, we can skip it because the children map contains the shortest path to all other nodes
                if (valve.flowRate == 0 || node.openedMask shr valve.id and 1 == 1) continue

                // Simulate moving to the valve and opening it
                val minutes = node.minutes - pathEntry.intValue - 1
                // There's no point to open a valve with zero minutes left, and it's not possible to open any after that
                if (minutes <= 0) continue

                val totalPressure = node.totalPressure + valve.flowRate * minutes
                val openedMask = node.openedMask or (1 shl valve.id)
                val numOpened = node.numOpened + 1

                if (completed[openedMask] < totalPressure) completed.put(openedMask, totalPressure)
                if (numOpened < this.valvesToOpen) queue.add(Node(minutes, valve, openedMask, numOpened, totalPressure))
            }
        }

        var maxPressure = 0

        if (!part2) {
            for (entry in completed.int2IntEntrySet()) {
                val totalPressure = entry.intValue
                if (totalPressure > maxPressure) maxPressure = totalPressure
            }

            return maxPressure
        }

        for (first in completed.int2IntEntrySet()) {
            for (second in completed.int2IntEntrySet()) {
                if (first === second) continue

                val firstOpenedMask = first.intKey
                val secondOpenedMask = second.intKey
                val xor = firstOpenedMask xor secondOpenedMask
                val union = firstOpenedMask or secondOpenedMask

                // If the XOR matches the union then we have no overlap
                if (xor == union) {
                    val totalPressure = first.intValue + second.intValue
                    if (totalPressure > maxPressure) maxPressure = totalPressure
                }
            }
        }

        return maxPressure
    }

    override fun parse() {
        val valves: MutableMap<String, Valve> = HashMap()
        this.valvesToOpen = 0
        var id = 0

        for (line in this.lines) {
            val name = line.substring(6, 8)
            val equalIdx = line.indexOf('=')
            val colonIdx = line.indexOf(';')
            var commaIdx = line.indexOf(',')
            if (commaIdx == -1) commaIdx = line.length

            val valve = valves.computeIfAbsent(name) { Valve() }
            val flowRate = line.substring(equalIdx + 1, colonIdx).toInt()

            if (flowRate > 0) {
                valve.flowRate = flowRate
                valve.id = id++
                valvesToOpen++
            }

            var i = commaIdx - 2
            while (i < line.length) {
                val child = valves.computeIfAbsent(line.substring(i, i + 2)) { Valve() }
                child.parents.add(valve)
                valve.children.put(child, 1)
                i += 4
            }
        }

        // This is a variant of Floyd-Warshall algorithm which I devised before I knew what it was called
        valves.forEach { (k, v) ->
            for (parent in v.parents) {
                val base = if (v.flowRate == 0 && k != "AA") parent.children.removeInt(v) else parent.children.getInt(v)
                for (entry in v.children.object2IntEntrySet()) {
                    if (entry.key === parent) continue
                    entry.key.parents.add(parent)
                    val newDist = entry.intValue + base
                    if (!parent.children.containsKey(entry.key) || parent.children.getInt(entry.key) > newDist) {
                        parent.children.put(entry.key, newDist)
                    }
                }
            }
        }

        this.aaValve = valves["AA"]!!
    }

    // We don't need/want data class here as we just use reference equality for the valves
    private class Valve(var id: Int = 0, var flowRate: Int = 0, val parents: MutableSet<Valve> = HashSet(), val children: Object2IntMap<Valve> = Object2IntOpenHashMap())

    // We never compare nodes so data class is useless
    private class Node(val minutes: Int, val valve: Valve, val openedMask: Int, val numOpened: Int, val totalPressure: Int)

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day16().run()
        }
    }
}