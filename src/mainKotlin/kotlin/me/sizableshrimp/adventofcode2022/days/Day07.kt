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

import me.sizableshrimp.adventofcode2022.templates.Day
import java.nio.file.Path

// https://adventofcode.com/2022/day/7 - No Space Left On Device
class Day07 : Day() {
    override fun evaluate(): Result {
        var currPath = BASE_PATH
        val map = HashMap<Path, Directory>()

        for (line in this.lines) {
            if (line[0] == '$') {
                if (line[2] == 'c') { // cd
                    currPath = when {
                        line.endsWith("..") -> currPath.parent
                        else -> {
                            val newPath = line.split(" ")[2]
                            if (newPath == "/") BASE_PATH else currPath.resolve(newPath)
                        }
                    }
                }

                continue
            }

            // We assume any line not starting with $ is a response to ls
            val split = line.split(" ")
            val directory = map.computeIfAbsent(currPath) { _ -> Directory() }

            if ("dir" == split[0]) {
                directory.children += map.computeIfAbsent(currPath.resolve(split[1])) { _ -> Directory() }
            } else {
                directory.size += split[0].toInt()
            }
        }

        var part1Size = 0L
        for (value in map.values) {
            val dirTotal = value.totalSize
            if (dirTotal <= 100000)
                part1Size += dirTotal
        }

        val currUnused = 70000000L - map[BASE_PATH]!!.totalSize
        var part2MinRemovalSize = Int.MAX_VALUE

        for (value in map.values) {
            val dirTotal = value.totalSize
            if (dirTotal <= part2MinRemovalSize && currUnused + dirTotal >= 30000000)
                part2MinRemovalSize = dirTotal
        }

        return Result.of(part1Size, part2MinRemovalSize)
    }

    private class Directory {
        val children = ArrayList<Directory>()
        var size = 0
        val totalSize: Int by lazy {
            var totalSize = this.size

            for (childDir in this.children) {
                totalSize += childDir.totalSize
            }

            totalSize
        }
    }

    companion object {
        private val BASE_PATH = Path.of(".")

        @JvmStatic
        fun main(args: Array<String>) {
            Day07().run()
        }
    }
}