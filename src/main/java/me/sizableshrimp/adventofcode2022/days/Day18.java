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

import me.sizableshrimp.adventofcode2022.templates.Day;
import me.sizableshrimp.adventofcode2022.templates.ZCoordinate;
import me.sizableshrimp.adventofcode2022.templates.ZDirection;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

// https://adventofcode.com/2022/day/18 - Boiling Boulders
public class Day18 extends Day {
    private Set<ZCoordinate> lavaCoords;

    public static void main(String[] args) {
        new Day18().run();
    }

    @Override
    protected Result evaluate() {
        int minX = 0;
        int maxX = 0;
        int minY = 0;
        int maxY = 0;
        int minZ = 0;
        int maxZ = 0;

        for (ZCoordinate coord : this.lavaCoords) {
            minX = Math.min(minX, coord.x);
            maxX = Math.max(maxX, coord.x);
            minY = Math.min(minY, coord.y);
            maxY = Math.max(maxY, coord.y);
            minZ = Math.min(minZ, coord.z);
            maxZ = Math.max(maxZ, coord.z);
        }

        Set<ZCoordinate> steamCoords = new HashSet<>();
        Set<ZCoordinate> airPockets = new HashSet<>();

        int part1Total = 0;
        int part2Total = 0;

        for (ZCoordinate coord : this.lavaCoords) {
            for (ZDirection dir : ZDirection.oneAxisDirections()) {
                ZCoordinate neighbor = coord.resolve(dir);
                if (this.lavaCoords.contains(neighbor))
                    continue;

                part1Total++;

                if (airPockets.contains(neighbor))
                    continue;

                if (steamCoords.contains(neighbor)) {
                    part2Total++;
                    continue;
                }

                Set<ZCoordinate> seen = new HashSet<>();
                Queue<ZCoordinate> queue = new ArrayDeque<>();
                queue.add(neighbor);
                boolean isSteam = false;
                while (!queue.isEmpty()) {
                    ZCoordinate steam = queue.remove();
                    for (ZDirection steamDir : ZDirection.oneAxisDirections()) {
                        ZCoordinate steamNeighbor = steam.resolve(steamDir);
                        if (steamCoords.contains(steamNeighbor) || steamNeighbor.x < minX || steamNeighbor.x > maxX || steamNeighbor.y < minY || steamNeighbor.y > maxY || steamNeighbor.z < minZ || steamNeighbor.z > maxZ) {
                            isSteam = true;
                            break;
                        }
                        if (!this.lavaCoords.contains(steamNeighbor) && seen.add(steamNeighbor)) {
                            queue.add(steamNeighbor);
                        }
                    }
                    if (isSteam)
                        break;
                }

                if (isSteam) {
                    steamCoords.addAll(seen);
                    part2Total++;
                } else {
                    airPockets.addAll(seen);
                }
            }
        }

        return Result.of(part1Total, part2Total);
    }

    @Override
    protected void parse() {
        this.lavaCoords = new HashSet<>();

        for (String line : this.lines) {
            this.lavaCoords.add(ZCoordinate.parse(line));
        }
    }
}
