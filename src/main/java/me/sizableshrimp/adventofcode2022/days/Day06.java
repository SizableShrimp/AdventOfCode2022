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

import it.unimi.dsi.fastutil.chars.CharOpenHashSet;
import me.sizableshrimp.adventofcode2022.templates.SeparatedDay;

// https://adventofcode.com/2022/day/6 - Tuning Trouble
public class Day06 extends SeparatedDay {
    private final String packet = this.lines.get(0);

    public static void main(String[] args) {
        new Day06().run();
    }

    @Override
    protected Object part1() {
        return simulate(4);
    }

    @Override
    protected Object part2() {
        return simulate(14);
    }

    private int simulate(int markerLength) {
        int packetLength = this.packet.length();

        for (int i = markerLength; i <= packetLength; i++) {
            char[] chars = new char[markerLength];
            this.packet.getChars(i - markerLength, i, chars, 0);
            if (new CharOpenHashSet(chars).size() == markerLength)
                return i;
        }

        throw new IllegalStateException();
    }
}
