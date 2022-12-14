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

import me.sizableshrimp.adventofcode2022.templates.Coordinate;
import me.sizableshrimp.adventofcode2022.templates.SeparatedDay;

import java.util.HashSet;
import java.util.Set;

// https://adventofcode.com/2022/day/14 - Regolith Reservoir
public class Day14 extends SeparatedDay {
    private static final Coordinate STARTING_SAND_POS = Coordinate.of(500, 0);
    private final Set<Coordinate> startingRocks = new HashSet<>();
    private int maxY;

    public static void main(String[] args) {
        new Day14().run();
    }

    @Override
    protected Object part1() {
        return this.simulate(false);
    }

    @Override
    protected Object part2() {
        return this.simulate(true);
    }

    private int simulate(boolean part2) {
        Set<Coordinate> rocks = new HashSet<>(this.startingRocks);
        int totalResting = 0;
        Coordinate sandPos = STARTING_SAND_POS;
        int maxY = part2 ? this.maxY + 1 : this.maxY;

        while (true) {
            Coordinate below = sandPos.down();

            // For part 1, this is when the abyss starts
            // For part 2, this is when the infinite horizontal line starts
            if (below.y > maxY) {
                if (part2) {
                    rocks.add(sandPos);
                    totalResting++;
                    sandPos = STARTING_SAND_POS;
                    continue;
                } else {
                    break;
                }
            }

            if (!rocks.contains(below)) {
                sandPos = below;
            } else {
                Coordinate left = below.left();
                Coordinate right = below.right();
                if (!rocks.contains(left)) {
                    sandPos = left;
                } else if (!rocks.contains(right)) {
                    sandPos = right;
                } else {
                    rocks.add(sandPos);
                    totalResting++;
                    if (sandPos == STARTING_SAND_POS)
                        break;
                    sandPos = STARTING_SAND_POS;
                }
            }
        }

        return totalResting;
    }

    @Override
    protected void parse() {
        this.startingRocks.clear();

        for (String line : this.lines) {
            int arrowIdx = -4;
            Coordinate coord = null;

            while (arrowIdx < line.length()) {
                int prevIdx = arrowIdx;
                arrowIdx = line.indexOf(" -> ", arrowIdx + 1);
                if (arrowIdx == -1)
                    arrowIdx = line.length();

                Coordinate nextCoord = Coordinate.parse(line.substring(prevIdx + 4, arrowIdx));

                if (coord != null) {
                    int minY = Math.min(coord.y, nextCoord.y);
                    int maxY = Math.max(coord.y, nextCoord.y);
                    int minX = Math.min(coord.x, nextCoord.x);
                    int maxX = Math.max(coord.x, nextCoord.x);

                    for (int y = minY; y <= maxY; y++) {
                        for (int x = minX; x <= maxX; x++) {
                            if (y > this.maxY)
                                this.maxY = y;
                            this.startingRocks.add(Coordinate.of(x, y));
                        }
                    }
                }

                coord = nextCoord;
            }
        }
    }
}
