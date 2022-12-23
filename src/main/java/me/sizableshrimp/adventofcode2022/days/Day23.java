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

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import me.sizableshrimp.adventofcode2022.helper.GridHelper;
import me.sizableshrimp.adventofcode2022.templates.Coordinate;
import me.sizableshrimp.adventofcode2022.templates.Day;
import me.sizableshrimp.adventofcode2022.templates.Direction;

import java.util.Set;

// https://adventofcode.com/2022/day/23 - Unstable Diffusion
public class Day23 extends Day {
    private static final Direction[] PROPOSITION_DIRS = {Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};
    private static final Direction[][] PROPOSITION_CHECKS = {
            new Direction[]{Direction.NORTH, Direction.NORTHEAST, Direction.NORTHWEST},
            new Direction[]{Direction.SOUTH, Direction.SOUTHEAST, Direction.SOUTHWEST},
            new Direction[]{Direction.WEST, Direction.NORTHWEST, Direction.SOUTHWEST},
            new Direction[]{Direction.EAST, Direction.NORTHEAST, Direction.SOUTHEAST}
    };
    private IntSet startingCoords;

    public static void main(String[] args) {
        new Day23().run();
    }

    @Override
    protected Result evaluate() {
        IntSet coords = new IntOpenHashSet(this.startingCoords);
        int round = 1;
        int part1 = 0;

        Int2IntMap newCoords = new Int2IntOpenHashMap();
        IntSet chosen = new IntOpenHashSet();
        IntSet dead = new IntOpenHashSet();
        IntSet a = new IntOpenHashSet();
        IntSet b = new IntOpenHashSet();
        while (true) {
            newCoords.clear();
            chosen.clear();
            dead.clear();

            IntIterator coordsIter = coords.iterator();
            while (coordsIter.hasNext()) {
                int elf = coordsIter.nextInt();
                boolean nearbyElf = false;
                for (Direction dir : Direction.cardinalOrdinalDirections()) {
                    int x = getX(elf) + dir.x;
                    int y = getY(elf) + dir.y;
                    int coord = getCoord(x, y);
                    if (coords.contains(coord)) {
                        nearbyElf = true;
                        break;
                    }
                }
                if (!nearbyElf)
                    continue;

                for (int i = 0; i < 4; i++) {
                    int propIdx = (i + round - 1) % 4;
                    boolean success = true;
                    for (Direction dir : PROPOSITION_CHECKS[propIdx]) {
                        int x = getX(elf) + dir.x;
                        int y = getY(elf) + dir.y;
                        int coord = getCoord(x, y);
                        if (coords.contains(coord)) {
                            success = false;
                            break;
                        }
                    }
                    if (success) {
                        Direction dir = PROPOSITION_DIRS[propIdx];
                        int x = getX(elf) + dir.x;
                        int y = getY(elf) + dir.y;
                        int coord = getCoord(x, y);
                        if (!chosen.add(coord)) {
                            dead.add(coord);
                        } else {
                            newCoords.put(elf, coord);
                        }
                        break;
                    }
                }
            }

            IntSet next = coords == a ? b : a;
            next.clear();
            boolean same = true;
            coordsIter = coords.iterator();
            while (coordsIter.hasNext()) {
                int elf = coordsIter.nextInt();
                int newCoord = newCoords.getOrDefault(elf, Integer.MAX_VALUE);
                if (newCoord == Integer.MAX_VALUE || dead.contains(newCoord)) {
                    next.add(elf);
                } else {
                    next.add(newCoord);
                    same = false;
                }
            }

            if (same)
                break;

            coords = next;

            if (round == 10)
                part1 = calculatePart1(coords);

            round++;
        }

        return Result.of(part1, round);
    }

    private static int calculatePart1(IntSet coords) {
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;

        IntIterator iter = coords.iterator();
        while (iter.hasNext()) {
            int elf = iter.nextInt();
            int x = getX(elf);
            int y = getY(elf);
            if (x < minX)
                minX = x;
            if (x > maxX)
                maxX = x;
            if (y < minY)
                minY = y;
            if (y > maxY)
                maxY = y;
        }

        return (maxX - minX + 1) * (maxY - minY + 1) - coords.size();
    }

    private static int getCoord(int x, int y) {
        // Turn positive for easier packing
        return x << 16 | y;
    }

    private static int getX(int coord) {
        return (coord >> 16);
    }

    private static int getY(int coord) {
        return (coord & 0xFFFF);
    }

    @Override
    protected void parse() {
        Set<Coordinate> startingCoordsFull = GridHelper.convertToSet(this.lines, c -> c == '#');
        this.startingCoords = new IntOpenHashSet();

        for (Coordinate coord : startingCoordsFull) {
            // We assume that the coord will not go larger than 1024 in any direction and therefore become negative
            this.startingCoords.add(getCoord(coord.x + 1024, coord.y + 1024));
        }
    }
}
