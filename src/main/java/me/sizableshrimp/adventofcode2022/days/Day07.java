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

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// https://adventofcode.com/2022/day/7 - No Space Left On Device
public class Day07 extends Day {
    private static final Path BASE_PATH = Path.of(".");

    public static void main(String[] args) {
        new Day07().run();
    }

    @Override
    protected Result evaluate() {
        Path currPath = BASE_PATH;
        Map<Path, Directory> map = new HashMap<>();

        for (String line : this.lines) {
            if (line.charAt(0) == '$') {
                if (line.charAt(2) == 'c') { // cd
                    if (line.endsWith("..")) {
                        currPath = currPath.getParent();
                    } else {
                        String newPath = line.split(" ")[2];
                        currPath = newPath.equals("/") ? BASE_PATH : currPath.resolve(newPath);
                    }
                }

                continue;
            }

            // We assume any line not starting with $ is a response to ls
            String[] split = line.split(" ");
            Directory directory = map.computeIfAbsent(currPath, k -> new Directory());
            if ("dir".equals(split[0])) {
                directory.children.add(map.computeIfAbsent(currPath.resolve(split[1]), k -> new Directory()));
            } else {
                directory.size += Integer.parseInt(split[0]);
            }
        }

        int part1Size = 0;
        for (Directory value : map.values()) {
            int dirTotal = value.getTotalSize();
            if (dirTotal <= 100000)
                part1Size += dirTotal;
        }

        int currUnused = 70000000 - map.get(BASE_PATH).getTotalSize();
        int part2MinRemovalSize = Integer.MAX_VALUE;

        for (Directory value : map.values()) {
            int dirTotal = value.getTotalSize();
            if (dirTotal <= part2MinRemovalSize && currUnused + dirTotal >= 30000000)
                part2MinRemovalSize = dirTotal;
        }

        return Result.of(part1Size, part2MinRemovalSize);
    }

    private static class Directory {
        final List<Directory> children = new ArrayList<>();
        int size;
        int cachedSize = -1;

        public int getTotalSize() {
            if (this.cachedSize != -1)
                return this.cachedSize;

            int totalSize = this.size;

            for (Directory childDir : children) {
                totalSize += childDir.getTotalSize();
            }

            this.cachedSize = totalSize;

            return totalSize;
        }
    }
}
