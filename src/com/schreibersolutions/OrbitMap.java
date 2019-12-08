package com.schreibersolutions;

import java.util.HashMap;
import java.util.Stack;


/**
 * Created by dschreiber on 12/7/19.
 */
public class OrbitMap {
    String map_array[];
    HashMap<String,String> map_hash = new HashMap();

     OrbitMap(String input) {

         map_array = input.split("\n");

        for (int i=0;i < map_array.length; i++) {
            String orbit[] = map_array[i].split("\\)");

            map_hash.put(orbit[1],orbit[0]);
        }

    }

    public Stack<String> getOrbitalPath(Stack<String> path, String start) {
        String orbits = map_hash.get(start);

        path.push(orbits);

        if (orbits.compareTo("COM") == 0) {
            return path;
        } else {
            return getOrbitalPath(path, orbits);
        }
    }

    public void myOrbtalPath() {
        Stack<String> s = getOrbitalPath(new Stack<String>(), "YOU");
        System.out.println(s.toString());
    }

    public void santaOrbitalPath() {
        Stack<String> s = getOrbitalPath(new Stack<String>(), "SAN");
        System.out.println(s.toString());
    }

    public int findCommonPlace() {
        Stack<String> you = getOrbitalPath(new Stack<String>(), "YOU");
        Stack<String> santa = getOrbitalPath(new Stack<String>(), "SAN");
        int minDistance = 9999999;
        int jumps;

        String commonPlace = "COM";

        for (int i_you = 0; i_you < you.size(); i_you++) {
            for (int i_santa = 0; i_santa < santa.size(); i_santa++) {
                if (you.get(i_you).compareTo(santa.get(i_santa)) == 0) {
                    commonPlace = you.get(i_you);
                    jumps = countOrbits("YOU", commonPlace) + countOrbits("SAN", commonPlace) - 2;
                    minDistance = Math.min(minDistance,jumps);
                }
            }
        }

        return minDistance;

    }

    public int countOrbits(String start) {
        return countOrbits(start, "COM");
    }

    public int countOrbits(String start, String end) {

        String orbits = map_hash.get(start);

        if (orbits.compareTo(end) == 0) {
            return 1;
        } else {
            return countOrbits(map_hash.get(start),end)+1;
        }
    }

    public int countOrbits() {
        int orbits = 0;

        for (String satellite: map_hash.keySet()) {
            orbits += countOrbits(satellite);
        }
        return orbits;
    }
}
