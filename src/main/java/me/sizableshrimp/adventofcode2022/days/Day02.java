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

// https://adventofcode.com/2022/day/2 - Rock Paper Scissors
public class Day02 extends Day {
    public static void main(String[] args) {
        new Day02().run();
    }

    @Override
    protected Result evaluate() {
        int totalPart1 = 0;
        int totalPart2 = 0;

        for (String line : lines) {
            // "ABC"
            int opponent = line.charAt(0) - 'A';
            // "XYZ"
            int me = line.charAt(2) - 'X';
            totalPart1 += me + 1 + Math.floorMod(me - opponent + 1, 3) * 3;
            totalPart2 += me * 3 + Math.floorMod(opponent + me - 1, 3) + 1;
        }

        return Result.of(totalPart1, totalPart2);
    }
}
