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

import it.unimi.dsi.fastutil.chars.CharArrayList;
import it.unimi.dsi.fastutil.chars.CharList;
import me.sizableshrimp.adventofcode2022.helper.Processor;
import me.sizableshrimp.adventofcode2022.templates.SeparatedDay;

import java.util.ArrayList;
import java.util.List;

// https://adventofcode.com/2022/day/5 - Supply Stacks
public class Day05 extends SeparatedDay {
    private List<CharList> initialState;
    private List<Move> moves;

    public static void main(String[] args) {
        new Day05().run();
    }

    @Override
    protected Object part1() {
        return simulate(false);
    }

    @Override
    protected Object part2() {
        return simulate(true);
    }

    private String simulate(boolean moveExact) {
        List<CharList> state = new ArrayList<>(this.initialState.size());
        for (CharList list : this.initialState) {
            state.add(new CharArrayList(list));
        }

        for (Move move : this.moves) {
            CharList from = state.get(move.fromIdx);
            CharList to = state.get(move.toIdx);
            int count = move.count;
            int size = from.size();

            if (moveExact) {
                int idx = size - count;
                for (int i = 0; i < count; i++) {
                    to.add(from.removeChar(idx));
                }
            } else {
                for (int i = 0; i < count; i++) {
                    to.add(from.removeChar(size - i - 1));
                }
            }
        }

        StringBuilder result = new StringBuilder();
        for (CharList list : state) {
            result.append(list.getChar(list.size() - 1));
        }
        return result.toString();
    }

    @Override
    protected void parse() {
        List<List<String>> linesSplit = Processor.splitOnBlankLines(this.lines);
        int numStacks = (lines.get(0).length() + 1) / 4;

        this.initialState = new ArrayList<>(numStacks);
        for (int i = 0; i < numStacks; i++) {
            this.initialState.add(new CharArrayList());
        }

        for (String line : linesSplit.get(0)) {
            if (line.indexOf('[') == -1)
                continue;

            for (int idx = 0; idx < numStacks; idx++) {
                char id = line.charAt(idx * 4 + 1);
                if (!Character.isLetter(id))
                    continue;

                this.initialState.get(idx).add(0, id);
            }
        }

        this.moves = new ArrayList<>(linesSplit.get(1).size());
        for (String line : linesSplit.get(1)) {
            String[] split = line.split(" ");
            this.moves.add(new Move(Integer.parseInt(split[3]) - 1, Integer.parseInt(split[5]) - 1, Integer.parseInt(split[1])));
        }
    }

    private record Move(int fromIdx, int toIdx, int count) {}
}
