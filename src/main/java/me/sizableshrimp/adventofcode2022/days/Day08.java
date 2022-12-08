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

import me.sizableshrimp.adventofcode2022.helper.GridHelper;
import me.sizableshrimp.adventofcode2022.templates.Coordinate;
import me.sizableshrimp.adventofcode2022.templates.Day;
import me.sizableshrimp.adventofcode2022.templates.Direction;

// https://adventofcode.com/2022/day/8 - Treetop Tree House
public class Day08 extends Day {
    private int[][] grid;

    public static void main(String[] args) {
        new Day08().run();
    }

    @Override
    protected Result evaluate() {
        int visibleTrees = 2 * (this.grid.length) + 2 * (this.grid[0].length) - 4;
        int maxScenicScore = Integer.MIN_VALUE;

        for (int y = 1; y < this.grid.length - 1; y++) {
            int[] row = this.grid[y];

            for (int x = 1; x < row.length - 1; x++) {
                int tree = row[x];
                boolean saw = false;
                int scenicScore = 1;

                for (Direction dir : Direction.cardinalDirections()) {
                    int visibleOnDirection = 0;
                    Coordinate coord = Coordinate.of(x, y);

                    while (true) {
                        coord = coord.resolve(dir);

                        if (!GridHelper.isValid(this.grid, coord)) {
                            if (!saw) {
                                visibleTrees++;
                                saw = true;
                            }
                            break;
                        }

                        visibleOnDirection++;

                        if (this.grid[coord.y][coord.x] >= tree)
                            break;
                    }

                    scenicScore *= visibleOnDirection;
                }

                if (scenicScore > maxScenicScore)
                    maxScenicScore = scenicScore;
            }
        }

        return Result.of(visibleTrees, maxScenicScore);
    }

    @Override
    protected void parse() {
        this.grid = GridHelper.convertInt(this.lines, c -> c - '0');
    }
}
