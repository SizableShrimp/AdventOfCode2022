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
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import me.sizableshrimp.adventofcode2022.templates.SeparatedDay;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

// https://adventofcode.com/2022/day/16 - Proboscidea Volcanium
public class Day16 extends SeparatedDay {
    private Valve aaValve;
    private int valvesToOpen;

    public static void main(String[] args) {
        new Day16().run();
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
        Int2IntMap completed = new Int2IntOpenHashMap();
        Queue<Node> queue = new ArrayDeque<>(); // new PriorityQueue<>(Comparator.comparing(n -> n.minutes));
        queue.add(new Node(part2 ? 26 : 30, this.aaValve, 0, 0, 0));

        while (!queue.isEmpty()) {
            Node node = queue.remove();

            if (node.minutes == 0)
                continue;

            for (Object2IntMap.Entry<Valve> pathEntry : node.valve.children.object2IntEntrySet()) {
                Valve valve = pathEntry.getKey();
                // If the valve is already opened or the AA valve, we can skip it because the children map contains the shortest path to all other nodes
                if (valve.flowRate == 0 || (node.openedMask >> valve.id & 1) == 1)
                    continue;

                // Simulate moving to the valve and opening it
                int minutes = node.minutes - pathEntry.getIntValue() - 1;
                if (minutes <= 0) // There's no point to open a valve with zero minutes left, and it's not possible to open any after that
                    continue;

                int totalPressure = node.totalPressure + valve.flowRate * minutes;
                int openedMask = node.openedMask | (1 << valve.id);
                int numOpened = node.numOpened + 1;
                if (completed.get(openedMask) < totalPressure)
                    completed.put(openedMask, totalPressure);
                if (numOpened < this.valvesToOpen)
                    queue.add(new Node(minutes, valve, openedMask, numOpened, totalPressure));
            }
        }

        int maxPressure = 0;

        if (!part2) {
            for (Int2IntMap.Entry entry : completed.int2IntEntrySet()) {
                int totalPressure = entry.getIntValue();
                if (totalPressure > maxPressure)
                    maxPressure = totalPressure;
            }

            return maxPressure;
        }

        for (Int2IntMap.Entry first : completed.int2IntEntrySet()) {
            for (Int2IntMap.Entry second : completed.int2IntEntrySet()) {
                if (first == second)
                    continue;

                int firstOpenedMask = first.getIntKey();
                int secondOpenedMask = second.getIntKey();
                int xor = firstOpenedMask ^ secondOpenedMask;
                int union = firstOpenedMask | secondOpenedMask;

                // If the XOR matches the union then we have no overlap
                if (xor == union) {
                    int totalPressure = first.getIntValue() + second.getIntValue();
                    if (totalPressure > maxPressure)
                        maxPressure = totalPressure;
                }
            }
        }

        return maxPressure;
    }

    @Override
    protected void parse() {
        Map<String, Valve> valves = new HashMap<>();
        this.valvesToOpen = 0;
        int id = 0;

        for (String line : this.lines) {
            String name = line.substring(6, 8);
            int equalIdx = line.indexOf('=');
            int colonIdx = line.indexOf(';');
            int commaIdx = line.indexOf(',');
            if (commaIdx == -1)
                commaIdx = line.length();

            Valve valve = valves.computeIfAbsent(name, k -> new Valve());
            int flowRate = Integer.parseInt(line.substring(equalIdx + 1, colonIdx));
            if (flowRate > 0) {
                valve.flowRate = flowRate;
                valve.id = id++;
                this.valvesToOpen++;
            }

            for (int i = commaIdx - 2; i < line.length(); i += 4) {
                Valve child = valves.computeIfAbsent(line.substring(i, i + 2), k -> new Valve());
                child.parents.add(valve);
                valve.children.put(child, 1);
            }
        }

        // This is a variant of Floyd-Warshall algorithm which I devised before I knew what it was called
        valves.forEach((k, v) -> {
            for (Valve parent : v.parents) {
                int base = v.flowRate == 0 && !k.equals("AA") ? parent.children.removeInt(v) : parent.children.getInt(v);
                for (Object2IntMap.Entry<Valve> entry : v.children.object2IntEntrySet()) {
                    if (entry.getKey() == parent)
                        continue;
                    entry.getKey().parents.add(parent);
                    int newDist = entry.getIntValue() + base;
                    if (!parent.children.containsKey(entry.getKey()) || parent.children.getInt((entry.getKey())) > newDist) {
                        parent.children.put(entry.getKey(), newDist);
                    }
                }
            }
        });

        this.aaValve = valves.get("AA");
    }

    private static class Valve {
        int id;
        int flowRate;
        final Set<Valve> parents = new HashSet<>();
        final Object2IntMap<Valve> children = new Object2IntOpenHashMap<>();
    }

    private record Node(int minutes, Valve valve, int openedMask, int numOpened, int totalPressure) {}
}
