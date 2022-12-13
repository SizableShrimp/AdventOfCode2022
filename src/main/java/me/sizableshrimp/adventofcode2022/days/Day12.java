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

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

// https://adventofcode.com/2022/day/12 - Hill Climbing Algorithm
public class Day12 extends Day {
    private Coordinate start;
    private Coordinate end;
    private int[][] grid;

    public static void main(String[] args) {
        new Day12().run();
    }

    @Override
    protected Result evaluate() {
        int[][] stepsGrid = new int[this.grid.length][this.grid[0].length];
        for (int[] row : stepsGrid) {
            Arrays.fill(row, -1);
        }
        Queue<Coordinate> queue = new ArrayDeque<>();
        queue.add(this.start);
        stepsGrid[this.start.y][this.start.x] = 0;

        int part1 = findShortestPath(stepsGrid, queue);

        // We can reuse the old steps to slightly speedup locations which are already fastest reached from the original start
        for (int y = 0; y < this.grid.length; y++) {
            for (int x = 0; x < this.grid[0].length; x++) {
                if (this.grid[y][x] == 0) {
                    Coordinate coord = Coordinate.of(x, y);
                    queue.add(coord);
                    stepsGrid[y][x] = 0;
                }
            }
        }

        int part2 = findShortestPath(stepsGrid, queue);

        return Result.of(part1, part2);
    }

    private int findShortestPath(int[][] stepsGrid, Queue<Coordinate> queue) {
        int minTargetSteps = Integer.MAX_VALUE;

        while (!queue.isEmpty()) {
            Coordinate pos = queue.remove();
            int steps = stepsGrid[pos.y][pos.x];
            if (steps >= minTargetSteps)
                continue;

            int curHeight = this.grid[pos.y][pos.x];
            int newSteps = steps + 1;

            for (Direction dir : Direction.cardinalDirections()) {
                if (!GridHelper.isValid(this.grid, pos, dir))
                    continue;
                int newPosX = pos.x + dir.x;
                int newPosY = pos.y + dir.y;

                if (curHeight >= this.grid[newPosY][newPosX] - 1) {
                    int currSteps = stepsGrid[newPosY][newPosX];
                    if (currSteps == -1 || currSteps > newSteps) {
                        if (this.end.x == newPosX && this.end.y == newPosY) {
                            if (newSteps < minTargetSteps)
                                minTargetSteps = newSteps;
                            continue;
                        }

                        Coordinate newPos = Coordinate.of(newPosX, newPosY);
                        queue.add(newPos);
                        stepsGrid[newPos.y][newPos.x] = newSteps;
                    }
                }
            }
        }

        return minTargetSteps;
    }

    @Override
    protected void parse() {
        this.start = GridHelper.findCoordinate(this.lines, 'S');
        this.end = GridHelper.findCoordinate(this.lines, 'E');

        this.grid = GridHelper.convertInt(this.lines, c -> switch (c) {
            case 'S' -> 0;
            case 'E' -> 25;
            default -> c - 'a';
        });
    }
}
