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

import it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import me.sizableshrimp.adventofcode2022.helper.ListConvert;
import me.sizableshrimp.adventofcode2022.helper.Processor;
import me.sizableshrimp.adventofcode2022.templates.Day;

import java.util.ArrayList;
import java.util.List;

// https://adventofcode.com/2022/day/1 - Calorie Counting
public class Day01 extends Day {
    private final IntAVLTreeSet maxCalories = new IntAVLTreeSet(IntComparators.NATURAL_COMPARATOR.reversed());
    private List<IntList> inventories;

    public static void main(String[] args) {
        new Day01().run();
    }

    @Override
    protected Result evaluate() {
        this.maxCalories.clear();

        for (IntList inventory : this.inventories) {
            int totalCalories = inventory.intStream().sum();
            this.maxCalories.add(totalCalories);
        }

        return Result.of(this.maxCalories.firstInt(), this.maxCalories.intStream().limit(3).sum());
    }

    @Override
    protected void parse() {
        this.inventories = new ArrayList<>();

        for (List<String> inventory : Processor.split(this.lines, String::isBlank)) {
            this.inventories.add(ListConvert.ints(inventory));
        }
    }
}
