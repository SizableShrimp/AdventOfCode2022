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
import it.unimi.dsi.fastutil.ints.Int2LongMap;
import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import me.sizableshrimp.adventofcode2022.templates.Coordinate;
import me.sizableshrimp.adventofcode2022.templates.Day;
import me.sizableshrimp.adventofcode2022.templates.Direction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// https://adventofcode.com/2022/day/17 - Pyroclastic Flow
public class Day17 extends Day {
    private static final List<Set<Coordinate>> ROCK_FORMATIONS = List.of(
            // ####
            Set.of(Coordinate.of(0, 0), Coordinate.of(1, 0), Coordinate.of(2, 0), Coordinate.of(3, 0)),
            // .#.
            // ###
            // .#.
            Set.of(Coordinate.of(1, 2), Coordinate.of(0, 1), Coordinate.of(1, 1), Coordinate.of(2, 1), Coordinate.of(1, 0)),
            // ..#
            // ..#
            // ###
            Set.of(Coordinate.of(2, 2), Coordinate.of(2, 1), Coordinate.of(0, 0), Coordinate.of(1, 0), Coordinate.of(2, 0)),
            // #
            // #
            // #
            // #
            Set.of(Coordinate.of(0, 3), Coordinate.of(0, 2), Coordinate.of(0, 1), Coordinate.of(0, 0)),
            // ##
            // ##
            Set.of(Coordinate.of(0, 1), Coordinate.of(1, 1), Coordinate.of(0, 0), Coordinate.of(1, 0))
    );
    private static final IntList ROCK_HEIGHTS = IntList.of(1, 3, 3, 4, 2);
    private List<Direction> jets;

    public static void main(String[] args) {
        new Day17().run();
    }

    @Override
    protected Result evaluate() {
        List<boolean[]> grid = new ArrayList<>();
        Int2LongMap cycles = new Int2LongOpenHashMap();
        Int2IntMap highestYs = new Int2IntOpenHashMap();
        Int2IntMap oldRoundCount = new Int2IntOpenHashMap();
        IntList oldHeights = new IntArrayList();
        int highestY = 0;
        int rockIdx = 0;
        int rocksSize = ROCK_FORMATIONS.size();
        int jetIdx = 0;
        int jetsSize = this.jets.size();
        int part1;
        long part2;
        int cycleStreak = 0;
        long cycle = 0;

        for (int round = 1; ; round++) {
            Set<Coordinate> rockCoords = new HashSet<>();
            for (Coordinate coord : ROCK_FORMATIONS.get(rockIdx)) {
                Coordinate rockCoord = coord.resolve(2, highestY + 4);
                rockCoords.add(rockCoord);

                while (rockCoord.y > grid.size()) {
                    grid.add(new boolean[7]);
                }
            }
            int key = jetIdx << 3 | rockIdx;
            // System.out.println(toString(highestY, rockIdx, cycle, rockCoords));
            long oldCycle = cycles.get(key);
            if (oldCycle != 0 && oldCycle == cycle) {
                cycleStreak++;
                if (cycleStreak > 10) {
                    int roundsLoop = round - oldRoundCount.get(key);
                    long toRun = 1_000_000_000_000L - round;
                    int yDiff = highestY - highestYs.get(key);
                    part1 = yDiff * ((2022 - round) / roundsLoop) + oldHeights.getInt(oldRoundCount.get(key) + ((2022 - round) % roundsLoop) - 1) + yDiff;
                    part2 = yDiff * (toRun / roundsLoop) + oldHeights.getInt(oldRoundCount.get(key) + (int) (toRun % roundsLoop) - 1) + yDiff;
                    break;
                }
            } else if (oldCycle != 0) {
                cycleStreak = 0;
            } else {
                cycles.put(key, cycle);
                highestYs.put(key, highestY);
                oldRoundCount.put(key, round);
            }
            boolean jetPushing = true;
            while (true) {
                Set<Coordinate> newCoords = new HashSet<>(rockCoords.size());
                Direction moveDir = jetPushing ? this.jets.get(jetIdx) : Direction.NORTH;
                boolean failMove = false;
                for (Coordinate rockCoord : rockCoords) {
                    Coordinate resolved = rockCoord.resolve(moveDir);
                    if (resolved.x < 0 || resolved.x > 6 || resolved.y <= 0 || grid.get(resolved.y - 1)[resolved.x]) {
                        failMove = true;
                        break;
                    }
                    newCoords.add(resolved);
                }
                if (failMove && !jetPushing)
                    break;

                if (!failMove)
                    rockCoords = newCoords;
                if (jetPushing) {
                    jetIdx++;
                    if (jetIdx == jetsSize)
                        jetIdx = 0;
                }
                jetPushing = !jetPushing;
            }
            int oldHighestY = highestY;
            for (Coordinate rockCoord : rockCoords) {
                if (rockCoord.y > highestY)
                    highestY = rockCoord.y;
            }
            if (highestY > 9 && highestY != oldHighestY) {
                int yChange = highestY - Math.max(9, oldHighestY);
                cycle >>= yChange * 7; // Delete however many rows got pushed out by new max Y
            }
            for (Coordinate rockCoord : rockCoords) {
                // 9 rows is the max we can pack into a long (9x7 = 63)
                // Highest Y includes a row, so we need >= highestY - 8 to get 9
                int min = Math.max(1, highestY - 8);
                if (rockCoord.y >= min) {
                    cycle |= 1L << ((rockCoord.y - min) * 7 + rockCoord.x);
                }

                grid.get(rockCoord.y - 1)[rockCoord.x] = true;
            }
            oldHeights.add(highestY);
            rockIdx++;
            if (rockIdx == rocksSize)
                rockIdx = 0;
        }

        return Result.of(part1, part2);
    }

    @Override
    protected void parse() {
        String jetString = this.lines.get(0);
        this.jets = new ArrayList<>();

        for (int i = 0; i < jetString.length(); i++) {
            this.jets.add(jetString.charAt(i) == '>' ? Direction.EAST : Direction.WEST);
        }
    }

    private static String toString(int highestY, int rockIdx, long cycle, Set<Coordinate> rockCoords) {
        StringBuilder result = new StringBuilder();
        int height = Math.min(9, highestY) + 3 + ROCK_HEIGHTS.getInt(rockIdx);
        int yOffset = Math.max(1, highestY - 8);

        for (int y = height - 1; y >= 0; y--) {
            result.append('|');
            for (int x = 0; x < 7; x++) {
                boolean filled = y <= 8 && ((cycle >> (y * 7 + x)) & 1) == 1;
                if (filled) {
                    result.append('#');
                } else if (rockCoords.contains(Coordinate.of(x, y + yOffset))) {
                    result.append('@');
                } else {
                    result.append('.');
                }
            }
            result.append("|\n");
        }
        result.append(highestY <= 9 ? "+-------+\n" : "   ...\n");

        return result.toString();
    }
}
