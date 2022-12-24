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

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import me.sizableshrimp.adventofcode2022.helper.GridHelper;
import me.sizableshrimp.adventofcode2022.helper.MathUtil;
import me.sizableshrimp.adventofcode2022.templates.Coordinate;
import me.sizableshrimp.adventofcode2022.templates.Day;
import me.sizableshrimp.adventofcode2022.templates.Direction;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

// https://adventofcode.com/2022/day/24 - Blizzard Basin
public class Day24 extends Day {
    private static final Coordinate START = Coordinate.of(1, 0);
    private boolean[][] grid;
    private boolean[][][] blizzards;
    private int loop;
    private Coordinate goal;

    public static void main(String[] args) {
        new Day24().run();
    }

    @Override
    protected Result evaluate() {
        int part1 = getMinSteps(0, START, this.goal);

        return Result.of(part1, getMinSteps(getMinSteps(part1, this.goal, START), START, this.goal));
    }

    private boolean[][][] getFullBlizzards(int horizModulo, int vertModulo, Direction[][] blizzards) {
        boolean[][][] fullBlizzards = new boolean[this.loop][vertModulo][horizModulo];

        for (int y = 0; y < blizzards.length; y++) {
            for (int x = 0; x < blizzards[0].length; x++) {
                Direction dir = blizzards[y][x];
                if (dir == null)
                    continue;

                fullBlizzards[0][y][x] = true;

                int xMut = x;
                int yMut = y;
                for (int i = 1; i < this.loop; i++) {
                    xMut = Math.floorMod(xMut + dir.x, horizModulo);
                    yMut = Math.floorMod(yMut + dir.y, vertModulo);
                    fullBlizzards[i][yMut][xMut] = true;
                }
            }
        }

        return fullBlizzards;
    }

    private int getMinSteps(int startingSteps, Coordinate start, Coordinate goal) {
        Queue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(n -> n.steps + n.coord.distance(goal)));
        Map<Coordinate, IntSet> steps = new HashMap<>();
        queue.add(new Node(start, startingSteps));

        while (!queue.isEmpty()) {
            Node node = queue.remove();
            int newSteps = node.steps + 1;

            if (steps.containsKey(node.coord)) {
                IntSet curSteps = steps.get(node.coord);
                if (curSteps.contains(node.steps % this.loop))
                    continue;
            }

            steps.computeIfAbsent(node.coord, c -> new IntOpenHashSet()).add(node.steps % this.loop);

            for (Direction dir : Direction.cardinalDirections()) {
                Coordinate neighbor = node.coord.resolve(dir);
                if (!GridHelper.isValid(this.grid, neighbor) || this.grid[neighbor.y][neighbor.x])
                    continue;

                boolean checkBlizzard = true;

                if (neighbor.equals(goal)) {
                    return newSteps;
                } else if (neighbor.equals(start)) {
                    checkBlizzard = false;
                }

                if (checkBlizzard && this.hasBlizzard(neighbor, newSteps))
                    continue;

                if (steps.containsKey(neighbor)) {
                    IntSet curSteps = steps.get(neighbor);
                    if (curSteps.contains(newSteps % this.loop))
                        continue;
                }

                Node newNode = new Node(neighbor, newSteps);
                queue.add(newNode);
            }

            if (node.coord.equals(start) || node.coord.equals(goal) || !this.hasBlizzard(node.coord, newSteps)) {
                if (steps.containsKey(node.coord)) {
                    IntSet curSteps = steps.get(node.coord);
                    if (curSteps.contains(newSteps % this.loop))
                        continue;
                }

                Node newNode = new Node(node.coord, newSteps);
                queue.add(newNode);
            }
        }

        throw new IllegalStateException();
    }

    private boolean hasBlizzard(Coordinate target, int steps) {
        return this.blizzards[steps % this.loop][target.y - 1][target.x - 1];
    }

    @Override
    protected void parse() {
        this.grid = GridHelper.convertBool(this.lines, c -> c == '#');
        Direction[][] blizzardDirs = new Direction[grid.length - 2][grid[0].length - 2];

        for (int y = 0; y < this.lines.size(); y++) {
            String row = this.lines.get(y);
            for (int x = 0; x < row.length(); x++) {
                char c = row.charAt(x);
                Direction dir = switch (c) {
                    case '>' -> Direction.EAST;
                    case '<' -> Direction.WEST;
                    case '^' -> Direction.NORTH;
                    case 'v' -> Direction.SOUTH;
                    default -> null;
                };

                if (dir != null)
                    blizzardDirs[y - 1][x - 1] = dir;
            }
        }

        int horizModulo = this.grid[0].length - 2;
        int vertModulo = this.grid.length - 2;
        this.loop = MathUtil.lcm(horizModulo, vertModulo);
        this.goal = Coordinate.of(this.grid[0].length - 2, this.grid.length - 1);
        this.blizzards = getFullBlizzards(horizModulo, vertModulo, blizzardDirs);
    }

    private record Node(Coordinate coord, int steps) {}
}
