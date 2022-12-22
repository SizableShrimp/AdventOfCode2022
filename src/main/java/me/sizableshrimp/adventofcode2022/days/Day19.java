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
import me.sizableshrimp.adventofcode2022.templates.SeparatedDay;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

// https://adventofcode.com/2022/day/19 - Not Enough Minerals
public class Day19 extends SeparatedDay {
    private List<Blueprint> blueprints;

    public static void main(String[] args) {
        new Day19().run();
    }

    @Override
    protected Object part1() {
        int result = 0;

        for (Blueprint blueprint : this.blueprints) {
            result += blueprint.id * this.getMaxGeodes(blueprint, 24);
        }

        return result;
    }

    @Override
    protected Object part2() {
        int result = 1;

        for (int i = 0; i < 3; i++) {
            Blueprint blueprint = this.blueprints.get(i);
            result *= this.getMaxGeodes(blueprint, 32);
        }

        return result;
    }

    private int getMaxGeodes(Blueprint blueprint, int startingMinutes) {
        Queue<Node> queue = new PriorityQueue<>(Comparator.<Node>comparingInt(n -> n.geodes).reversed());
        int skipMinutes = Math.min(blueprint.oreRobotCostOre, blueprint.clayRobotCostOre);
        queue.add(new Node(startingMinutes - skipMinutes, skipMinutes, 0, 0, 0, 1, 0, 0, 0, null));
        int maxGeodes = 0;

        while (!queue.isEmpty()) {
            Node node = queue.remove();
            int newMinutes = node.minutes - 1;

            // If we can't make back the max geodes in time, we should just skip this node.
            // We can calculate the upper bound by taking the arithmetic series from 1+geodeRobots, 2+geodeRobots, ..., minutes-1+geodeRobots.
            // This is because it calculates the max geodes we can make if we make 1 geode robot every minute and sum the resources from those robots
            // using an arithmetic series, shifting the series based on the current number of robots and adding the current amount of geodes.
            // The number of terms in the series would be the number of minutes we have left to build/mine.
            // We can slightly reorganize the arithmetic series to pull the geode robots out of the numerator as there are 2 terms of it divided by 2.
            if (maxGeodes != 0 && maxGeodes > node.geodes + node.minutes * ((1 + newMinutes) / 2 + node.geodeRobots))
                continue;

            if (newMinutes == 0) {
                int finalGeodes = node.geodes + node.geodeRobots;
                if (finalGeodes > maxGeodes)
                    maxGeodes = finalGeodes;
                continue;
            }

            int newOre = node.ore + node.oreRobots;
            int newClay = node.clay + node.clayRobots;
            int newObsidian = node.obsidian + node.obsidianRobots;
            int newGeodes = node.geodes + node.geodeRobots;

            if (node.targetToBuild != null) {
                Node newNode;
                if (blueprint.canBuildRobot(node.targetToBuild, node)) {
                    newNode = new Node(newMinutes, newOre, newClay, newObsidian, newGeodes, node, node.targetToBuild, blueprint, null);
                } else {
                    newNode = new Node(newMinutes, newOre, newClay, newObsidian, newGeodes, node, null, blueprint, node.targetToBuild);
                }
                queue.add(newNode);
                continue;
            }

            for (ResourceType type : ResourceType.RESOURCES) {
                if (blueprint.shouldEverBuildRobot(type, node)) {
                    Node newNode;
                    if (blueprint.canBuildRobot(type, node)) {
                        newNode = new Node(newMinutes, newOre, newClay, newObsidian, newGeodes, node, type, blueprint, null);
                    } else {
                        newNode = new Node(newMinutes, newOre, newClay, newObsidian, newGeodes, node, null, blueprint, type);
                    }
                    queue.add(newNode);
                }
            }
        }

        return maxGeodes;
    }

    @Override
    protected void parse() {
        this.blueprints = new ArrayList<>(this.lines.size());

        for (String line : this.lines) {
            String[] split = line.split(" ");

            int id = Integer.parseInt(split[1].substring(0, split[1].length() - 1));
            int oreRobotCost = Integer.parseInt(split[6]);
            int clayRobotCost = Integer.parseInt(split[12]);
            int obsidianRobotCostOre = Integer.parseInt(split[18]);
            int obsidianRobotCostClay = Integer.parseInt(split[21]);
            int geodeRobotCostOre = Integer.parseInt(split[27]);
            int geodeRobotCostObsidian = Integer.parseInt(split[30]);

            this.blueprints.add(new Blueprint(id, oreRobotCost, clayRobotCost, obsidianRobotCostOre, obsidianRobotCostClay, geodeRobotCostOre, geodeRobotCostObsidian));
        }
    }

    private record Blueprint(int id, int oreRobotCostOre, int clayRobotCostOre, int obsidianRobotCostOre, int obsidianRobotCostClay,
                             int geodeRobotCostOre, int geodeRobotCostObsidian) {
        boolean shouldEverBuildRobot(ResourceType type, Day19.Node node) {
            return switch (type) {
                case ORE -> node.oreRobots < Math.max(this.oreRobotCostOre, Math.max(this.clayRobotCostOre, Math.max(this.obsidianRobotCostOre, this.geodeRobotCostOre)));
                case CLAY -> node.clayRobots < this.obsidianRobotCostClay;
                case OBSIDIAN -> node.clayRobots > 0 && node.obsidianRobots < this.geodeRobotCostObsidian;
                case GEODE -> node.clayRobots > 0 && node.obsidianRobots > 0;
            };
        }

        boolean canBuildRobot(ResourceType type, Day19.Node node) {
            return switch (type) {
                case ORE -> node.ore >= this.oreRobotCostOre;
                case CLAY -> node.ore >= this.clayRobotCostOre;
                case OBSIDIAN -> node.ore >= this.obsidianRobotCostOre && node.clay >= this.obsidianRobotCostClay;
                case GEODE -> node.ore >= this.geodeRobotCostOre && node.obsidian >= this.geodeRobotCostObsidian;
            };
        }
    }

    @AllArgsConstructor
    private static class Node {
        final int minutes;
        int ore;
        int clay;
        int obsidian;
        int geodes;
        int oreRobots;
        int clayRobots;
        int obsidianRobots;
        int geodeRobots;
        final ResourceType targetToBuild;

        Node(int minutes, int ore, int clay, int obsidian, int geodes, Node old, ResourceType robotToBuild, Blueprint blueprint, ResourceType targetToBuild) {
            this.minutes = minutes;
            this.targetToBuild = targetToBuild;
            this.ore = ore;
            this.clay = clay;
            this.obsidian = obsidian;
            this.geodes = geodes;
            this.oreRobots = old.oreRobots;
            this.clayRobots = old.clayRobots;
            this.obsidianRobots = old.obsidianRobots;
            this.geodeRobots = old.geodeRobots;
            if (robotToBuild != null) {
                switch (robotToBuild) {
                    case ORE -> {
                        this.oreRobots++;
                        this.ore -= blueprint.oreRobotCostOre;
                    }
                    case CLAY -> {
                        this.clayRobots++;
                        this.ore -= blueprint.clayRobotCostOre;
                    }
                    case OBSIDIAN -> {
                        this.obsidianRobots++;
                        this.ore -= blueprint.obsidianRobotCostOre;
                        this.clay -= blueprint.obsidianRobotCostClay;
                    }
                    case GEODE -> {
                        this.geodeRobots++;
                        this.ore -= blueprint.geodeRobotCostOre;
                        this.obsidian -= blueprint.geodeRobotCostObsidian;
                    }
                }
            }
        }
    }

    private enum ResourceType {
        ORE, CLAY, OBSIDIAN, GEODE;

        private static final ResourceType[] RESOURCES = new ResourceType[]{GEODE, OBSIDIAN, CLAY, ORE};
    }
}
