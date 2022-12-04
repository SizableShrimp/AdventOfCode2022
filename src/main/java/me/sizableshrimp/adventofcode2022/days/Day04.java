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

package me.sizableshrimp.adventofcode2022.days;

import me.sizableshrimp.adventofcode2022.templates.Day;

// https://adventofcode.com/2022/day/4 - Camp Cleanup
public class Day04 extends Day {
    public static void main(String[] args) {
        new Day04().run();
    }

    @Override
    protected Result evaluate() {
        int part1Count = 0;
        int part2Count = 0;

        for (String line : this.lines) {
            String[] pairs = line.split(",");
            String[] pair1Nums = pairs[0].split("-");
            String[] pair2Nums = pairs[1].split("-");
            int aStart = Integer.parseInt(pair1Nums[0]);
            int aEnd = Integer.parseInt(pair1Nums[1]);
            int bStart = Integer.parseInt(pair2Nums[0]);
            int bEnd = Integer.parseInt(pair2Nums[1]);
            if (aStart <= bStart && bEnd <= aEnd || bStart <= aStart && aEnd <= bEnd)
                part1Count++;
            if (aStart <= bEnd && bStart <= aEnd)
                part2Count++;
        }

        return Result.of(part1Count, part2Count);
    }

    @Override
    protected void parse() {

    }
}
