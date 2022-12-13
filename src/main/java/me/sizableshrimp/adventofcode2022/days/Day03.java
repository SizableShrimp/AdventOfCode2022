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
import me.sizableshrimp.adventofcode2022.helper.Processor;
import me.sizableshrimp.adventofcode2022.templates.SeparatedDay;

import java.util.List;

// https://adventofcode.com/2022/day/3 - Rucksack Reorganization
public class Day03 extends SeparatedDay {
    public static void main(String[] args) {
        new Day03().run();
    }

    @Override
    protected Object part1() {
        int sum = 0;

        for (String line : this.lines) {
            int middle = line.length() / 2;
            CharOpenHashSet charSet = new CharOpenHashSet(line.substring(middle).toCharArray());
            for (int i = 0; i < middle; i++) {
                char c = line.charAt(i);
                if (charSet.contains(c)) {
                    sum += getPriority(c);
                    break;
                }
            }
        }

        return sum;
    }

    @Override
    protected Object part2() {
        int sum = 0;

        for (List<String> window : Processor.windowed(this.lines, 3, false)) {
            CharOpenHashSet charSet = new CharOpenHashSet(window.get(0).toCharArray());
            charSet.retainAll(new CharOpenHashSet(window.get(1).toCharArray()));
            charSet.retainAll(new CharOpenHashSet(window.get(2).toCharArray()));
            sum += getPriority(charSet.iterator().nextChar());
        }

        return sum;
    }

    private static int getPriority(char c) {
        if (Character.isLowerCase(c)) {
            return c - 'a' + 1;
        } else {
            return c - 'A' + 27;
        }
    }
}
