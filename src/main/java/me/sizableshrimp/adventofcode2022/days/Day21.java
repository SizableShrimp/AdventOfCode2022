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

import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.sizableshrimp.adventofcode2022.helper.Parser;
import me.sizableshrimp.adventofcode2022.templates.EnumState;
import me.sizableshrimp.adventofcode2022.templates.SeparatedDay;

import java.util.HashMap;
import java.util.Map;

// https://adventofcode.com/2022/day/21 - Monkey Math
public class Day21 extends SeparatedDay {
    private Monkey rootMonkey;

    public static void main(String[] args) {
        new Day21().run();
    }

    @Override
    protected Object part1() {
        return this.rootMonkey.getValue();
    }

    @Override
    protected Object part2() {
        return this.rootMonkey.getRequiredValue(0);
    }

    @Override
    protected void parse() {
        Map<String, Monkey> monkeys = new HashMap<>();

        for (String line : this.lines) {
            int colonIdx = line.indexOf(':');
            String id = line.substring(0, colonIdx);
            String eval = line.substring(colonIdx + 2);
            int spaceIdx = eval.indexOf(' ');
            Monkey monkey = monkeys.computeIfAbsent(id, Monkey::new);

            if (spaceIdx == -1) { // Number monkey
                monkey.num = Long.parseLong(eval);
                monkey.hasNum = true;
            } else { // Operation monkey
                String leftId = eval.substring(0, spaceIdx);
                char op = eval.charAt(spaceIdx + 1);
                String rightId = eval.substring(spaceIdx + 3);
                monkey.leftMonkey = monkeys.computeIfAbsent(leftId, Monkey::new);
                monkey.leftMonkey.parent = monkey;
                monkey.rightMonkey = monkeys.computeIfAbsent(rightId, Monkey::new);
                monkey.rightMonkey.parent = monkey;
                monkey.operation = Operation.ID_MAP.get(op);
            }
        }

        this.rootMonkey = monkeys.get("root");

        Monkey dependentMonkey = monkeys.get("humn");
        while (dependentMonkey != null) {
            dependentMonkey.requiresHuman = true;
            dependentMonkey = dependentMonkey.parent;
        }
    }

    @RequiredArgsConstructor
    private static class Monkey {
        private final String id;
        private Monkey parent;
        private long cachedValue;
        private boolean requiresHuman;
        // Number monkey
        private long num;
        private boolean hasNum;
        // Operation monkey
        private Monkey leftMonkey;
        private Monkey rightMonkey;
        private Operation operation;

        private long getValue() {
            if (this.hasNum)
                return this.num;

            if (this.cachedValue != 0L)
                return this.cachedValue;

            this.cachedValue = this.operation.apply(this.leftMonkey.getValue(), this.rightMonkey.getValue());
            return this.cachedValue;
        }

        private long getRequiredValue(long target) {
            if (this.hasNum)
                return target; // humn

            if (this.leftMonkey.requiresHuman) {
                long right = this.rightMonkey.getValue();
                return this.leftMonkey.getRequiredValue(target == 0 ? right : switch (this.operation) {
                    case ADD -> target - right;
                    case SUBTRACT -> target + right;
                    case MULTIPLY -> target / right;
                    case DIVIDE -> target * right;
                });
            } else {
                long left = this.leftMonkey.getValue();
                return this.rightMonkey.getRequiredValue(target == 0 ? left : switch (this.operation) {
                    case ADD -> target - left;
                    case SUBTRACT -> left - target;
                    case MULTIPLY -> target / left;
                    case DIVIDE -> left / target;
                });
            }
        }
    }

    @Getter
    @RequiredArgsConstructor
    private enum Operation implements EnumState<Operation> {
        ADD('+'), SUBTRACT('-'), MULTIPLY('*'), DIVIDE('/');

        private static final Char2ObjectMap<Operation> ID_MAP = Parser.getEnumCharMap(values());
        private final char mappedChar;

        private long apply(long left, long right) {
            return switch (this) {
                case ADD -> left + right;
                case SUBTRACT -> left - right;
                case MULTIPLY -> left * right;
                case DIVIDE -> left / right;
            };
        }
    }
}
