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
import me.sizableshrimp.adventofcode2022.templates.SeparatedDay;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// https://adventofcode.com/2022/day/15 - Beacon Exclusion Zone
public class Day15 extends SeparatedDay {
    private List<Sensor> sensors;

    public static void main(String[] args) {
        new Day15().run();
    }

    @Override
    protected Object part1() {
        // We assume the answer to part 2 is not on the row y=2,000,000, so we can have a continuous line segment
        int minX = 0;
        int maxX = 0;
        Set<Coordinate> extraBeacons = new HashSet<>();

        for (Sensor sensor : this.sensors) {
            int yDist = Math.abs(sensor.pos.y - 2_000_000);
            int maxDist = sensor.beaconDist;
            if (yDist > maxDist)
                continue; // Does not contribute to the total

            int xDist = maxDist - yDist; // The # of squares in either direction from the sensor X pos that cannot have a beacon
            minX = Math.min(minX, sensor.pos.x - xDist);
            maxX = Math.max(maxX, sensor.pos.x + xDist);

            if (sensor.beacon.y == 2_000_000)
                extraBeacons.add(sensor.beacon);
        }

        return maxX - minX + 1 - extraBeacons.size();
    }

    @Override
    protected Object part2() {
        int x = 0;
        int y = 0;
        int maxXY = 4_000_000;

        while (true) {
            boolean found = true;
            for (Sensor sensor : this.sensors) {
                int yDist = Math.abs(sensor.pos.y - y);
                int maxDist = sensor.beaconDist;
                if (yDist > maxDist)
                    continue; // We are already outside the range of the sensor, so it can't help us

                int xDist = Math.abs(sensor.pos.x - x);
                if (yDist + xDist <= maxDist) {
                    x = sensor.pos.x + maxDist - yDist + 1;
                    if (x > maxXY) {
                        found = false;
                        break;
                    }
                }
            }

            if (found)
                break;

            x = 0;
            y++;
        }

        return x * (long) maxXY + y;
    }

    @Override
    protected void parse() {
        this.sensors = new ArrayList<>(this.lines.size());

        for (String line : this.lines) {
            int commaIdx = line.indexOf(',');
            int colonIdx = line.indexOf(':');
            int sensorX = Integer.parseInt(line.substring(12, commaIdx));
            int sensorY = Integer.parseInt(line.substring(commaIdx + 4, colonIdx));

            int beaconEqualIdx = line.indexOf('=', colonIdx);
            commaIdx = line.indexOf(',', beaconEqualIdx);
            int beaconX = Integer.parseInt(line.substring(beaconEqualIdx + 1, commaIdx));
            int beaconY = Integer.parseInt(line.substring(commaIdx + 4));

            this.sensors.add(new Sensor(Coordinate.of(sensorX, sensorY), Coordinate.of(beaconX, beaconY)));
        }

        this.sensors.sort(Comparator.comparingInt(s -> s.pos.x));
    }

    private record Sensor(Coordinate pos, Coordinate beacon, int beaconDist) {
        private Sensor(Coordinate pos, Coordinate beacon) {
            this(pos, beacon, pos.distance(beacon));
        }
    }
}
