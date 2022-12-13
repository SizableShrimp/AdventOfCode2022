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

import it.unimi.dsi.fastutil.ints.IntList;
import me.sizableshrimp.adventofcode2022.helper.Processor;
import me.sizableshrimp.adventofcode2022.templates.SeparatedDay;

import java.util.ArrayList;
import java.util.List;

// https://adventofcode.com/2022/day/13 - Distress Signal
public class Day13 extends SeparatedDay {
    private static final List<IntList> FIRST_DIVIDER_PACKET = List.of(IntList.of(2));
    private static final List<IntList> SECOND_DIVIDER_PACKET = List.of(IntList.of(6));
    private final List<List<?>> packets = new ArrayList<>();

    public static void main(String[] args) {
        new Day13().run();
    }

    @Override
    protected Object part1() {
        int sum = 0;
        List<List<List<?>>> windows = Processor.windowed(this.packets, 2, false);

        for (int i = 0; i < windows.size(); i++) {
            List<List<?>> pair = windows.get(i);
            if (comparePackets(pair.get(0), pair.get(1)) < 0)
                sum += i + 1;
        }

        return sum;
    }

    @Override
    protected Object part2() {
        // A full sort for part 2 is not necessarily.
        // You can just go through each packet once to see if it is before either divider packet.
        int firstKey = 1; // Index shifted by +1
        int secondKey = 2; // Includes first divider packet and +1 index shift

        for (List<?> packet : this.packets) {
            if (this.comparePackets(packet, FIRST_DIVIDER_PACKET) < 0) {
                firstKey++;
                secondKey++;
            } else if (this.comparePackets(packet, SECOND_DIVIDER_PACKET) < 0) {
                secondKey++;
            }
        }

        return firstKey * secondKey;
    }

    // <0 = in order, 0 = continue, >0 = out of order
    private int comparePackets(Object first, Object second) {
        boolean firstIsInt = first instanceof Integer;
        boolean secondIsInt = second instanceof Integer;
        if (firstIsInt && secondIsInt) {
            return ((Integer) first).compareTo((Integer) second);
        } else if (firstIsInt) {
            return comparePackets(List.of(first), second);
        } else if (secondIsInt) {
            return comparePackets(first, List.of(second));
        }

        List<?> firstList = (List<?>) first;
        List<?> secondList = (List<?>) second;

        for (int i = 0; i < firstList.size(); i++) {
            if (i >= secondList.size())
                return 1;

            int subCompare = comparePackets(firstList.get(i), secondList.get(i));
            if (subCompare != 0)
                return subCompare;
        }

        return firstList.size() == secondList.size() ? 0 : -1;
    }


    @Override
    protected void parse() {
        this.packets.clear();

        for (String line : this.lines) {
            if (line.isBlank())
                continue;

            this.packets.add((List<?>) this.parse(line));
        }
    }

    private Object parse(String str) {
        if (str.charAt(0) != '[')
            return Integer.parseInt(str);
        if (str.length() == 2)
            return List.of(); // []

        List<Object> list = new ArrayList<>();
        int nestedCount = 0;
        int lastIdx = 1;
        int end = str.length() - 1;

        for (int i = 1; i < end; i++) {
            switch (str.charAt(i)) {
                case '[' -> nestedCount++;
                case ']' -> nestedCount--;
                case ',' -> {
                    if (nestedCount == 0) {
                        list.add(this.parse(str.substring(lastIdx, i)));
                        lastIdx = i + 1;
                    }
                }
            }
        }
        list.add(this.parse(str.substring(lastIdx, end)));

        return list;
    }
}
