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
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
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
    private static final List<RockFormation> ROCK_FORMATIONS = List.of(
            // ####
            new RockFormation(0, Set.of(Coordinate.of(0, 0), Coordinate.of(1, 0), Coordinate.of(2, 0), Coordinate.of(3, 0))),
            // .#.
            // ###
            // .#.
            new RockFormation(2, Set.of(Coordinate.of(1, 0), Coordinate.of(0, 1), Coordinate.of(1, 1), Coordinate.of(2, 1), Coordinate.of(1, 2))),
            // ..#
            // ..#
            // ###
            new RockFormation(2, Set.of(Coordinate.of(2, 0), Coordinate.of(2, 1), Coordinate.of(0, 2), Coordinate.of(1, 2), Coordinate.of(2, 2))),
            // #
            // #
            // #
            // #
            new RockFormation(3, Set.of(Coordinate.of(0, 0), Coordinate.of(0, 1), Coordinate.of(0, 2), Coordinate.of(0, 3))),
            // ##
            // ##
            new RockFormation(1, Set.of(Coordinate.of(0, 0), Coordinate.of(1, 0), Coordinate.of(0, 1), Coordinate.of(1, 1)))
    );
    private List<Direction> jets;

    public static void main(String[] args) {
        new Day17().run();
    }

    @Override
    protected Result evaluate() {
        Set<Coordinate> rockCoords = new HashSet<>();
        Int2ObjectMap<Set<Coordinate>> cycles = new Int2ObjectOpenHashMap<>();
        Int2IntMap highestYs = new Int2IntOpenHashMap();
        Int2IntMap oldRoundCount = new Int2IntOpenHashMap();
        IntList oldHeights = new IntArrayList();
        int highestY = 0;
        int rockIdx = 0;
        int jetIdx = 0;
        int part1;
        long part2;

        for (int round = 1; ; round++) {
            RockFormation rockFormation = ROCK_FORMATIONS.get(rockIdx);
            Set<Coordinate> coords = new HashSet<>();
            for (Coordinate coord : rockFormation.shape) {
                coords.add(Coordinate.of(coord.x + 2, rockFormation.bottomEdge - coord.y + highestY + 4));
            }
            int key = jetIdx << 3 | rockIdx;
            Set<Coordinate> cycle = new HashSet<>();
            for (Coordinate coord : rockCoords) {
                if (coord.y > highestY - 40)
                    cycle.add(coord.resolve(0, -highestY + 40));
            }
            Set<Coordinate> oldCycle = cycles.get(key);
            if (oldCycle != null && oldCycle.equals(cycle)) {
                int roundsLoop = round - oldRoundCount.get(key);
                long toRun = 1_000_000_000_000L - round;
                int yDiff = highestY - highestYs.get(key);
                part1 = yDiff * ((2022 - round) / roundsLoop) + oldHeights.getInt(oldRoundCount.get(key) + ((2022 - round) % roundsLoop) - 1) + yDiff;
                part2 = yDiff * (toRun / roundsLoop) + oldHeights.getInt(oldRoundCount.get(key) + (int) (toRun % roundsLoop) - 1) + yDiff;
                cycles.clear();
                highestYs.clear();
                oldRoundCount.clear();
                break;
            } else if (oldCycle == null) {
                cycles.put(key, cycle);
                highestYs.put(key, highestY);
                oldRoundCount.put(key, round);
            }
            boolean jetPushing = true;
            while (true) {
                Set<Coordinate> newCoords = new HashSet<>(coords.size());
                Direction moveDir = jetPushing ? this.jets.get(jetIdx) : Direction.NORTH;
                boolean failMove = false;
                for (Coordinate coord : coords) {
                    Coordinate resolved = coord.resolve(moveDir);
                    if (resolved.x < 0 || resolved.x > 6 || resolved.y <= 0 || rockCoords.contains(resolved)) {
                        failMove = true;
                        break;
                    }
                    newCoords.add(resolved);
                }
                if (failMove && !jetPushing)
                    break;

                if (!failMove)
                    coords = newCoords;
                if (jetPushing) {
                    jetIdx++;
                    if (jetIdx == this.jets.size())
                        jetIdx = 0;
                }
                jetPushing = !jetPushing;
            }
            for (Coordinate coord : coords) {
                if (coord.y > highestY)
                    highestY = coord.y;
            }
            oldHeights.add(highestY);
            rockCoords.addAll(coords);
            rockIdx++;
            if (rockIdx == ROCK_FORMATIONS.size())
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

    private record RockFormation(int bottomEdge, Set<Coordinate> shape) {}
}
