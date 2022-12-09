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

import me.sizableshrimp.adventofcode2022.templates.Coordinate;
import me.sizableshrimp.adventofcode2022.templates.Direction;
import me.sizableshrimp.adventofcode2022.templates.SeparatedDay;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// https://adventofcode.com/2022/day/9 - Rope Bridge
public class Day09 extends SeparatedDay {
    private List<Move> moves;

    public static void main(String[] args) {
        new Day09().run();
    }

    @Override
    protected Object part1() {
        return this.simulate(1);
    }

    @Override
    protected Object part2() {
        return this.simulate(9);
    }

    private int simulate(int numKnots) {
        int lastKnotIdx = numKnots - 1;
        Set<Coordinate> visited = new HashSet<>();
        Coordinate head = Coordinate.ORIGIN;
        visited.add(Coordinate.ORIGIN);
        List<Coordinate> knots = new ArrayList<>(numKnots);
        for (int i = 0; i < numKnots; i++) {
            knots.add(Coordinate.ORIGIN);
        }

        for (Move move : this.moves) {
            Direction dir = move.dir;
            int moveCount = move.count;

            for (int currMove = 0; currMove < moveCount; currMove++) {
                head = head.resolve(dir);

                for (int i = 0; i < numKnots; i++) {
                    Coordinate knot = knots.get(i);
                    Coordinate knotNewPos = this.moveRope(i == 0 ? head : knots.get(i - 1), knot);
                    if (knot == knotNewPos)
                        continue;

                    knots.set(i, knotNewPos);
                    if (i == lastKnotIdx)
                        visited.add(knotNewPos);
                }
            }
        }

        return visited.size();
    }

    private Coordinate moveRope(Coordinate head, Coordinate tail) {
        if (head.equals(tail) || Math.abs(tail.x - head.x) <= 1 && Math.abs(tail.y - head.y) <= 1)
            return tail;

        if (head.x == tail.x) {
            return tail.resolve(0, head.y > tail.y ? 1 : -1);
        } else if (head.y == tail.y) {
            return tail.resolve(head.x > tail.x ? 1 : -1, 0);
        } else {
            return tail.resolve(head.x > tail.x ? 1 : -1, head.y > tail.y ? 1 : -1);
        }
    }

    @Override
    protected void parse() {
        this.moves = new ArrayList<>(this.lines.size());

        for (String line : this.lines) {
            Direction dir = Direction.getCardinalDirection(line.charAt(0));
            int count = Integer.parseInt(line.substring(2));
            this.moves.add(new Move(dir, count));
        }
    }

    private record Move(Direction dir, int count) {}
}
