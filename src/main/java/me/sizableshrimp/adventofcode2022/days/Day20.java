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

import lombok.RequiredArgsConstructor;
import me.sizableshrimp.adventofcode2022.templates.SeparatedDay;

// https://adventofcode.com/2022/day/20 - Grove Positioning System
public class Day20 extends SeparatedDay {
    private int[] numbers;

    public static void main(String[] args) {
        new Day20().run();
    }

    @Override
    protected Object part1() {
        return this.simulate(1, 1);
    }

    @Override
    protected Object part2() {
        return this.simulate(811589153, 10);
    }

    private long simulate(int decryptionKey, int rounds) {
        Node startNode = null;
        Node prevNode = null;
        Node zeroNode = null;
        int modulo = this.lines.size() - 1;
        Node[] nodes = new Node[this.lines.size()];
        int decryptionKeyMod = decryptionKey % modulo;

        for (int i = 0; i < this.lines.size(); i++) {
            int value = this.numbers[i];
            Node node = new Node(value, ((value % modulo) * decryptionKeyMod) % modulo);
            nodes[i] = node;
            if (value == 0)
                zeroNode = node;
            if (startNode == null)
                startNode = node;
            if (prevNode != null) {
                node.prev = prevNode;
                prevNode.next = node;
            }
            prevNode = node;
        }

        startNode.prev = prevNode;
        prevNode.next = startNode;

        for (int round = 1; round <= rounds; round++) {
            for (Node node : nodes) {
                int shift = node.shift;
                if (shift == 0)
                    continue;

                // Remove node
                node.prev.next = node.next;
                node.next.prev = node.prev;

                // Find target
                var targetNode = node.next;
                int shiftAbs = Math.abs(shift);
                for (int i = 1; i <= shiftAbs; i++) {
                    targetNode = shift > 0 ? targetNode.next : targetNode.prev;
                }

                // Insert before target
                targetNode.prev.next = node;
                Node oldPrev = targetNode.prev;
                targetNode.prev = node;
                node.prev = oldPrev;
                node.next = targetNode;
            }
        }

        long result = 0;
        Node node = zeroNode;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 1000; j++) {
                node = node.next;
            }
            result += node.value * (long) decryptionKey;
        }

        return result;
    }

    @Override
    protected void parse() {
        this.numbers = new int[this.lines.size()];

        for (int i = 0; i < this.lines.size(); i++) {
            this.numbers[i] = Integer.parseInt(this.lines.get(i));
        }
    }

    @RequiredArgsConstructor
    private static class Node {
        private final int value;
        private final int shift;
        private Node prev;
        private Node next;

        @Override
        public String toString() {
            return Integer.toString(this.value);
        }
    }
}
