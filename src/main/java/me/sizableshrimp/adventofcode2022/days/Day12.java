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

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import me.sizableshrimp.adventofcode2022.helper.GridHelper;
import me.sizableshrimp.adventofcode2022.templates.Coordinate;
import me.sizableshrimp.adventofcode2022.templates.Day;
import me.sizableshrimp.adventofcode2022.templates.Direction;

import java.util.ArrayDeque;
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
        Object2IntMap<Coordinate> steps = new Object2IntOpenHashMap<>();
        Queue<Coordinate> queue = new ArrayDeque<>();
        queue.add(this.start);

        int part1 = findShortestPath(steps, queue);

        // We can reuse the old steps to slightly speedup locations which are already fastest reached from the original start
        for (int y = 0; y < this.grid.length; y++) {
            for (int x = 0; x < this.grid[0].length; x++) {
                if (this.grid[y][x] == 0) {
                    Coordinate coord = Coordinate.of(x, y);
                    queue.add(coord);
                    steps.put(coord, 0);
                }
            }
        }

        int part2 = findShortestPath(steps, queue);

        return Result.of(part1, part2);
    }

    private int findShortestPath(Object2IntMap<Coordinate> steps, Queue<Coordinate> queue) {
        while (!queue.isEmpty()) {
            Coordinate pos = queue.remove();
            int curHeight = this.grid[pos.y][pos.x];

            for (Direction dir : Direction.cardinalDirections()) {
                Coordinate newPos = pos.resolve(dir);
                if (!GridHelper.isValid(this.grid, newPos))
                    continue;

                if (curHeight >= this.grid[newPos.y][newPos.x] - 1) {
                    int newSteps = steps.getInt(pos) + 1;
                    if (!steps.containsKey(newPos) || steps.getInt(newPos) > newSteps) {
                        queue.add(newPos);
                        steps.put(newPos, newSteps);
                    }
                }
            }
        }

        return steps.getInt(this.end);
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
