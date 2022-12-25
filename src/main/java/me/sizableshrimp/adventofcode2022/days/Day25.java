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

import me.sizableshrimp.adventofcode2022.templates.SeparatedDay;

// https://adventofcode.com/2022/day/25 - Full of Hot Air
public class Day25 extends SeparatedDay {
    public static void main(String[] args) {
        new Day25().run();
    }

    @Override
    protected Object part1() {
        long sum = 0L;

        for (String line : this.lines) {
            sum += this.convertFromBalancedQuinary(line);
        }

        return convertToBalancedQuinary(sum);
    }

    @Override
    protected Object part2() {
        // No Part 2 :)
        return null;
    }

    private long convertFromBalancedQuinary(String numStr) {
        long num = 0;
        long power = 1;

        for (int i = numStr.length() - 1; i >= 0; i--) {
            num += power * switch (numStr.charAt(i)) {
                case '2' -> 2;
                case '1' -> 1;
                case '0' -> 0;
                case '-' -> -1;
                case '=' -> -2;
                default -> throw new IllegalStateException();
            };
            power *= 5;
        }

        return num;
    }

    private static String convertToBalancedQuinary(long num) {
        StringBuilder result = new StringBuilder();

        while (num > 0) {
            int remainder = (int) (num % 5);
            num /= 5;
            if (remainder > 2) {
                num++;
            }
            result.insert(0, switch (remainder) {
                case 0 -> '0';
                case 1 -> '1';
                case 2 -> '2';
                case 3 -> '='; // 5 - 2
                case 4 -> '-'; // 5 - 1
                default -> throw new IllegalStateException();
            });
        }

        return result.toString();
    }
}
