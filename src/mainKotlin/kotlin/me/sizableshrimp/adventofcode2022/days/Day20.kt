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

import me.sizableshrimp.adventofcode2022.templates.SeparatedDay
import kotlin.math.abs

// https://adventofcode.com/2022/day/20 - Grove Positioning System
class Day20 : SeparatedDay() {
    private lateinit var numbers: IntArray

    override fun part1() = simulate(1, 1)

    override fun part2() = simulate(811589153, 10)

    private fun simulate(decryptionKey: Int, rounds: Int): Long {
        var startNode: Node? = null
        var prevNode: Node? = null
        var zeroNode: Node? = null
        val modulo = this.numbers.size - 1
        val nodes = Array<Node?>(this.numbers.size) { null }
        val decryptionKeyMod = decryptionKey % modulo

        for (i in this.numbers.indices) {
            val value = this.numbers[i]
            val node = Node(value, ((value % modulo) * decryptionKeyMod) % modulo)
            nodes[i] = node
            if (value == 0)
                zeroNode = node
            if (startNode == null)
                startNode = node
            prevNode?.apply { node.prev = this }
            prevNode?.next = node
            prevNode = node
        }

        prevNode!!.next = startNode!!
        startNode.prev = prevNode

        for (round in 1..rounds) {
            for (node in nodes) {
                val shift = node!!.shift
                if (shift == 0) continue

                // Remove node
                node.prev.next = node.next
                node.next.prev = node.prev

                // Find target
                var targetNode = node.next
                for (i in 1..abs(shift)) {
                    targetNode = if (shift > 0) targetNode.next else targetNode.prev
                }

                // Insert before target
                targetNode.prev.next = node
                val oldPrev = targetNode.prev
                targetNode.prev = node
                node.prev = oldPrev
                node.next = targetNode
            }
        }

        var result = 0L
        var node = zeroNode!!

        // var string = ""
        // while (true) {
        //     string += node.value.toString() + ", "
        //     node = node.next
        //     if (node == zeroNode) break
        // }

        for (i in 1..3) {
            for (j in 1..1000) {
                node = node.next
            }
            result += node.value * decryptionKey.toLong()
        }

        return result
    }

    override fun parse() {
        this.numbers = IntArray(this.lines.size) { this.lines[it].toInt() }
    }

    private class Node(val value: Int, val shift: Int) {
        lateinit var prev: Node
        lateinit var next: Node

        override fun toString() = this.value.toString()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day20().run()
        }
    }
}