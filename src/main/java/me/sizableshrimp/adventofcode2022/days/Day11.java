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

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import me.sizableshrimp.adventofcode2022.helper.Processor;
import me.sizableshrimp.adventofcode2022.templates.SeparatedDay;

import java.util.ArrayList;
import java.util.List;

// https://adventofcode.com/2022/day/11 - Monkey in the Middle
public class Day11 extends SeparatedDay {
    private List<Monkey> monkeys;
    private int numMonkeys;

    public static void main(String[] args) {
        new Day11().run();
    }

    @Override
    protected Object part1() {
        return simulate(false);
    }

    @Override
    protected Object part2() {
        return simulate(true);
    }

    private long simulate(boolean part2) {
        List<IntList> itemLists = new ArrayList<>(this.numMonkeys);
        int maxWorryLevel = 1;
        int[] monkeyBusiness = new int[this.numMonkeys];

        for (Monkey monkey : this.monkeys) {
            if (part2)
                maxWorryLevel *= monkey.divisibleBy;
            itemLists.add(new IntArrayList(monkey.startingItems));
        }

        int rounds = part2 ? 10_000 : 20;
        for (int round = 0; round < rounds; round++) {
            for (int monkeyIdx = 0; monkeyIdx < this.numMonkeys; monkeyIdx++) {
                Monkey monkey = this.monkeys.get(monkeyIdx);
                IntList items = itemLists.get(monkeyIdx);
                if (items.isEmpty())
                    continue;

                for (int i = 0; i < items.size(); i++) {
                    int worryLevel = items.getInt(i);
                    long newWorryLevel = monkey.operation.apply(worryLevel);
                    worryLevel = part2 ? (int) (newWorryLevel % maxWorryLevel) : ((int) newWorryLevel) / 3;
                    monkeyBusiness[monkeyIdx]++;
                    itemLists.get(worryLevel % monkey.divisibleBy == 0 ? monkey.throwTrue : monkey.throwFalse).add(worryLevel);
                }

                items.clear();
            }
        }

        int firstMax = Integer.MIN_VALUE;
        int secondMax = Integer.MIN_VALUE;

        for (int itemsInspected : monkeyBusiness) {
            if (itemsInspected > firstMax) {
                secondMax = firstMax;
                firstMax = itemsInspected;
            } else if (itemsInspected > secondMax) {
                secondMax = itemsInspected;
            }
        }

        return firstMax * (long) secondMax;
    }

    @Override
    protected void parse() {
        List<List<String>> splitMonkeys = Processor.splitOnBlankLines(this.lines);
        this.numMonkeys = splitMonkeys.size();
        this.monkeys = new ArrayList<>(this.numMonkeys);

        for (List<String> monkeyData : splitMonkeys) {
            String[] startingItemStrings = monkeyData.get(1).substring(18).split(",");
            IntList startingItems = new IntArrayList(startingItemStrings.length);
            for (String num : startingItemStrings) {
                // Using a single character for split() and trimming here should mean a fastpath regex
                startingItems.add(Integer.parseInt(num.trim()));
            }

            String operationStr = monkeyData.get(2);
            String mathPart = operationStr.substring(25);
            Operation operation = mathPart.equals("old") ? new Operation(true, false, 0) : new Operation(false, operationStr.indexOf('+') != -1, Integer.parseInt(mathPart));
            int divisibleBy = Integer.parseInt(monkeyData.get(3).substring(21));
            int throwTrue = Integer.parseInt(monkeyData.get(4).substring(29));
            int throwFalse = Integer.parseInt(monkeyData.get(5).substring(30));

            this.monkeys.add(new Monkey(startingItems, operation, divisibleBy, throwTrue, throwFalse));
        }
    }

    private record Monkey(IntList startingItems, Operation operation, int divisibleBy, int throwTrue, int throwFalse) {}

    private record Operation(boolean squared, boolean add, int amount) {
        // These operations can and do produce a value higher than Integer.MAX_VALUE before the modulo is taken and the number is brought back down to an int
        public long apply(int worryLevel) {
            if (this.squared)
                return worryLevel * (long) worryLevel;

            return this.add ? worryLevel + (long) this.amount : worryLevel * (long) this.amount;
        }
    }
}
