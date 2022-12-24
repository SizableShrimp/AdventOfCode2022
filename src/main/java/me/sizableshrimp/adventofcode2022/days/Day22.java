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

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.sizableshrimp.adventofcode2022.helper.GridHelper;
import me.sizableshrimp.adventofcode2022.helper.MathUtil;
import me.sizableshrimp.adventofcode2022.templates.Coordinate;
import me.sizableshrimp.adventofcode2022.templates.Direction;
import me.sizableshrimp.adventofcode2022.templates.EnumState;
import me.sizableshrimp.adventofcode2022.templates.SeparatedDay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// https://adventofcode.com/2022/day/22 - Monkey Map
public class Day22 extends SeparatedDay {
    private static final Map<Region, CubeMapping> CUBE_REGION_MAP = new HashMap<>();
    private State[][] grid;
    private int regionSize;
    private Coordinate startingCoord;
    private List<Move> moves;

    public static void main(String[] args) {
        new Day22().run();
    }

    static {
        setupCubeRegions();
    }

    @Override
    protected Object part1() {
        return simulate(false);
    }

    @Override
    protected Object part2() {
        return simulate(true);
    }

    private int simulate(boolean part2) {
        Coordinate coord = this.startingCoord;
        Direction dir = Direction.EAST;

        for (Move move : this.moves) {
            switch (move.dirChar) {
                case 'R' -> dir = dir.clockwise();
                case 'L' -> dir = dir.counterClockwise();
            }

            for (int step = 0; step < move.amount; step++) {
                Coordinate tempCoord = coord.resolve(dir);
                boolean tryWrapAround;
                State state = null;
                if (GridHelper.isValid(this.grid, tempCoord)) {
                    state = this.grid[tempCoord.y][tempCoord.x];
                    tryWrapAround = state == State.EDGE;
                } else {
                    tryWrapAround = true;
                }

                if (state == State.WALL)
                    break;

                if (!tryWrapAround) {
                    coord = tempCoord;
                    continue;
                }

                CubeMapping cubeMapping = null;
                if (part2) {
                    Region oldRegion = getRegion(coord, dir);
                    cubeMapping = CUBE_REGION_MAP.get(oldRegion);
                    tempCoord = this.getCubeWrappedCoord(coord, cubeMapping);
                } else {
                    tempCoord = this.getWrappedCoord(coord, dir);
                }
                if (this.grid[tempCoord.y][tempCoord.x] == State.WALL)
                    break;

                coord = tempCoord;
                if (cubeMapping != null)
                    dir = cubeMapping.region.dir;
            }
        }

        int facing = switch (dir) {
            case EAST -> 0;
            case SOUTH -> 1;
            case WEST -> 2;
            case NORTH -> 3;
            default -> throw new IllegalStateException("Unexpected value: " + dir);
        };
        return 1000 * (coord.y + 1) + 4 * (coord.x + 1) + facing;
    }

    private Coordinate getWrappedCoord(Coordinate originalPos, Direction dir) {
        return switch (dir) {
            case NORTH -> {
                int x = originalPos.x;
                for (int y = this.grid.length - 1; y >= 0; y--) {
                    State newState = this.grid[y][x];
                    if (newState != State.EDGE) {
                        yield Coordinate.of(x, y);
                    }
                }

                throw new IllegalStateException();
            }
            case EAST -> {
                int y = originalPos.y;
                for (int x = 0; x < this.grid[0].length; x++) {
                    State newState = this.grid[y][x];
                    if (newState != State.EDGE) {
                        yield Coordinate.of(x, y);
                    }
                }

                throw new IllegalStateException();
            }
            case SOUTH -> {
                int x = originalPos.x;
                for (int y = 0; y < this.grid.length; y++) {
                    State newState = this.grid[y][x];
                    if (newState != State.EDGE) {
                        yield Coordinate.of(x, y);
                    }
                }

                throw new IllegalStateException();
            }
            case WEST -> {
                int y = originalPos.y;
                for (int x = this.grid[0].length - 1; x >= 0; x--) {
                    State newState = this.grid[y][x];
                    if (newState != State.EDGE) {
                        yield Coordinate.of(x, y);
                    }
                }

                throw new IllegalStateException();
            }
            default -> throw new IllegalStateException("Unexpected value: " + dir);
        };
    }

    private Region getRegion(Coordinate coord, Direction dir) {
        return new Region(coord.x / this.regionSize, coord.y / this.regionSize, dir);
    }

    private Coordinate getCubeWrappedCoord(Coordinate originalPos, CubeMapping cubeMapping) {
        int relativeX = originalPos.x % this.regionSize;
        int relativeY = originalPos.y % this.regionSize;

        return mapCoord(cubeMapping, relativeX, relativeY);
    }

    private Coordinate mapCoord(CubeMapping cubeMapping, int oldRelativeX, int oldRelativeY) {
        Region newRegion = cubeMapping.region;
        boolean reverseNumber = cubeMapping.reverseNumber;
        int x = switch (newRegion.dir) {
            case EAST -> 0;
            case WEST -> this.regionSize - 1;
            default -> {
                int xy = cubeMapping.flipXY ? oldRelativeY : oldRelativeX;
                yield reverseNumber ? this.regionSize - 1 - xy : xy;
            }
        };
        int y = switch (newRegion.dir) {
            case NORTH -> this.regionSize - 1;
            case SOUTH -> 0;
            default -> {
                int xy = cubeMapping.flipXY ? oldRelativeX : oldRelativeY;
                yield reverseNumber ? this.regionSize - 1 - xy : xy;
            }
        };

        return Coordinate.of(newRegion.x * this.regionSize + x, newRegion.y * this.regionSize + y);
    }

    @Override
    protected void parse() {
        this.grid = GridHelper.convertVariableLength((y, x) -> new State[y][x], this.lines.subList(0, this.lines.size() - 2), State.EDGE);
        this.regionSize = MathUtil.gcd(this.grid.length, this.grid[0].length);

        for (int x = 0; x < this.grid[0].length; x++) {
            State state = this.grid[0][x];
            if (state == State.SPACE) {
                this.startingCoord = Coordinate.of(x, 0);
                break;
            }
        }

        String instructions = this.lines.get(this.lines.size() - 1);
        char dirChar = ' ';
        this.moves = new ArrayList<>();
        int numberStartIdx = 0;
        int length = instructions.length();

        for (int i = 0; i < length; i++) {
            char c = instructions.charAt(i);
            if (Character.isLetter(c)) {
                this.moves.add(new Move(dirChar, Integer.parseInt(instructions.substring(numberStartIdx, i))));
                dirChar = c;
                numberStartIdx = i + 1;
            }
        }
        this.moves.add(new Move(dirChar, Integer.parseInt(instructions.substring(numberStartIdx))));
    }

    private static void putRegionPairing(int firstX, int firstY, Direction startingDir, int endX, int endY, Direction finalDir, boolean flipXY, boolean reverseNumber) {
        putRegionPairing(new Region(firstX, firstY, startingDir), new Region(endX, endY, finalDir), flipXY, reverseNumber);
    }

    private static void putRegionPairing(Region first, Region second, boolean flipXY, boolean reverseNumber) {
        CUBE_REGION_MAP.put(first, new CubeMapping(second, flipXY, reverseNumber));
        CUBE_REGION_MAP.put(second.reverse(), new CubeMapping(first.reverse(), flipXY, reverseNumber));
    }

    private static void setupCubeRegions() {
        // EXAMPLE
        // // ..1.
        // // 234.
        // // ..56
        // // Region 1
        // putRegionPairing(2, 0, Direction.NORTH, 0, 1, Direction.SOUTH, false, true);
        // putRegionPairing(2, 0, Direction.WEST, 1, 1, Direction.SOUTH, true, false);
        // putRegionPairing(2, 0, Direction.EAST, 3, 2, Direction.WEST, false, true);
        // // Region 2
        // putRegionPairing(0, 1, Direction.WEST, 3, 2, Direction.NORTH, true, true);
        // putRegionPairing(0, 1, Direction.SOUTH, 2, 2, Direction.NORTH, false, true);
        // // Region 3
        // putRegionPairing(1, 1, Direction.SOUTH, 2, 2, Direction.EAST, true, false);
        // // Region 4
        // putRegionPairing(2, 1, Direction.EAST, 3, 2, Direction.SOUTH, true, true);

        // .12
        // .3.
        // 45.
        // 6..
        // Region 1
        putRegionPairing(1, 0, Direction.NORTH, 0, 3, Direction.EAST, true, false);
        putRegionPairing(1, 0, Direction.WEST, 0, 2, Direction.EAST, false, true);
        // Region 2
        putRegionPairing(2, 0, Direction.NORTH, 0, 3, Direction.NORTH, true, true);
        putRegionPairing(2, 0, Direction.EAST, 1, 2, Direction.WEST, false, true);
        putRegionPairing(2, 0, Direction.SOUTH, 1, 1, Direction.WEST, true, false);
        // Region 3
        putRegionPairing(1, 1, Direction.WEST, 0, 2, Direction.SOUTH, true, false);
        // Region 5
        putRegionPairing(1, 2, Direction.SOUTH, 0, 3, Direction.WEST, true, false);
    }

    private record Move(char dirChar, int amount) {}

    private record Region(int x, int y, Direction dir) {
        private Region reverse() {
            return new Region(this.x, this.y, dir.opposite());
        }
    }

    private record CubeMapping(Region region, boolean flipXY, boolean reverseNumber) {}

    @AllArgsConstructor
    @Getter
    private enum State implements EnumState<State> {
        EDGE(' '), WALL('#'), SPACE('.');

        private final char mappedChar;
    }
}
