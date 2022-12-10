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
import me.sizableshrimp.adventofcode2022.helper.LetterParser;
import me.sizableshrimp.adventofcode2022.templates.Day;

// https://adventofcode.com/2022/day/10 - Cathode-Ray Tube
public class Day10 extends Day {
    private final IntList instructions = new IntArrayList(this.lines.size());

    public static void main(String[] args) {
        new Day10().run();
    }

    @Override
    protected Result evaluate() {
        int register = 1;
        int i = 0;
        int cycle = 1;
        int max = this.instructions.size();
        int toAdd = 0;
        boolean adding = false;
        int signalStrength = 0;
        boolean[][] grid = new boolean[6][40];

        while (i < max) {
            if (cycle == 20 || (cycle > 20 && (cycle - 20) % 40 == 0))
                signalStrength += cycle * register;

            int row = cycle / 40;
            int column = cycle % 40 - 1;
            if (Math.abs(register - column) <= 1)
                grid[row][column] = true;

            cycle++;

            if (adding) {
                adding = false;
                register += toAdd;
                continue;
            }

            int insn = this.instructions.getInt(i);
            i++;

            if (insn == 0)
                continue; // noop

            // addx
            toAdd = insn;
            adding = true;
        }

        return Result.of(signalStrength, LetterParser.getLettersOrGrid(grid).trim());
    }

    @Override
    protected void parse() {
        this.instructions.clear();

        for (String line : this.lines) {
            this.instructions.add(line.charAt(0) == 'a' ? /* addx */ Integer.parseInt(line.substring(5)) : /* noop */ 0);
        }
    }
}
