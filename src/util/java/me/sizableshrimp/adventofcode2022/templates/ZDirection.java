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

package me.sizableshrimp.adventofcode2022.templates;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Nullable;

/**
 * The 6 directions that only vary on a single axis of x, y, or z
 */
@AllArgsConstructor
public enum ZDirection {
    NORTH(0, -1, 0, Axis.Y),
    EAST(1, 0, 0, Axis.X),
    SOUTH(0, 1, 0, Axis.Y),
    WEST(-1, 0, 0, Axis.X),
    UP(0, 0, 1, Axis.Z),
    DOWN(0, 0, -1, Axis.Z);

    private static final ZDirection[] ONE_AXIS = {NORTH, EAST, SOUTH, WEST, UP, DOWN};

    public final int x;
    public final int y;
    public final int z;
    @Nullable
    public final Axis axis;

    /**
     * Returns the 6 directions that vary on a single axis of x, y, or z
     */
    public static ZDirection[] oneAxisDirections() {
        return ONE_AXIS;
    }

    public ZCoordinate asCoords() {
        return ZCoordinate.of(this.x, this.y, this.z);
    }

    public static ZDirection fromDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> NORTH;
            case EAST -> EAST;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            default -> throw new IllegalArgumentException("Invalid direction: " + direction);
        };
    }

    public Direction toDirection() {
        return switch (this) {
            case NORTH -> Direction.NORTH;
            case EAST -> Direction.EAST;
            case SOUTH -> Direction.SOUTH;
            case WEST -> Direction.WEST;
            default -> throw new IllegalArgumentException("Invalid z direction: " + this);
        };
    }

    public enum Axis {
        X, Y, Z
    }
}
