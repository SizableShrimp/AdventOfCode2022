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

// https://adventofcode.com/2022/day/21 - Monkey Math
class Day21 : SeparatedDay() {
    private lateinit var rootMonkey: Monkey

    override fun part1() = this.rootMonkey.value

    override fun part2() = this.rootMonkey.calculateRequiredValue(0)

    override fun parse() {
        val monkeys = HashMap<String, Monkey>()

        for (line in this.lines) {
            val split = line.split(": ")
            val id = split[0]
            val monkey = monkeys.computeIfAbsent(id, ::Monkey)
            if (split[1].indexOf(' ') == -1) { // Number monkey
                monkey.num = split[1].toLong()
                monkey.hasNum = true
            } else { // Operation monkey
                val (left, op, right) = split[1].split(" ")
                monkey.leftMonkey = monkeys.computeIfAbsent(left, ::Monkey)
                monkey.leftMonkey!!.parent = monkey
                monkey.rightMonkey = monkeys.computeIfAbsent(right, ::Monkey)
                monkey.rightMonkey!!.parent = monkey
                monkey.operation = when (op) {
                    "+" -> Operation.ADD
                    "-" -> Operation.SUBTRACT
                    "*" -> Operation.MULTIPLY
                    "/" -> Operation.DIVIDE
                    else -> throw IllegalStateException()
                }
            }
        }

        this.rootMonkey = monkeys["root"]!!

        var dependentHuman = monkeys["humn"]
        while (dependentHuman != null) {
            dependentHuman.requiresHuman = true
            dependentHuman = dependentHuman.parent
        }
    }

    private class Monkey(val id: String) {
        var parent: Monkey? = null
        var num: Long = 0
        var hasNum: Boolean = false
        var leftMonkey: Monkey? = null
        var rightMonkey: Monkey? = null
        var operation: Operation? = null
        private var cachedValue: Long = 0
        private var cached: Boolean = false
        var requiresHuman: Boolean = false
        val value: Long
            get() {
                if (this.cached) return this.cachedValue
                if (this.hasNum) return this.num

                this.cachedValue = this.operation!!.apply(this.leftMonkey!!.value, this.rightMonkey!!.value)
                this.cached = true
                return this.cachedValue
            }

        fun calculateRequiredValue(target: Long): Long {
            if (this.hasNum)
                return target // humn

            val leftMonkey = this.leftMonkey!!
            val rightMonkey = this.rightMonkey!!

            if (leftMonkey.requiresHuman) {
                val right = rightMonkey.value
                return leftMonkey.calculateRequiredValue(
                    if (target == 0L) right else when (this.operation!!) {
                        Operation.ADD -> target - right
                        Operation.SUBTRACT -> target + right
                        Operation.MULTIPLY -> target / right
                        Operation.DIVIDE -> target * right
                    }
                )
            } else {
                val left = leftMonkey.value
                return rightMonkey.calculateRequiredValue(
                    if (target == 0L) left else when (this.operation!!) {
                        Operation.ADD -> target - left
                        Operation.SUBTRACT -> left - target
                        Operation.MULTIPLY -> target / left
                        Operation.DIVIDE -> left / target
                    }
                )
            }
        }
    }

    private enum class Operation {
        ADD, MULTIPLY, DIVIDE, SUBTRACT;

        fun apply(left: Long, right: Long): Long = when (this) {
            ADD -> left + right
            MULTIPLY -> left * right
            DIVIDE -> left / right
            SUBTRACT -> left - right
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day21().run()
        }
    }
}