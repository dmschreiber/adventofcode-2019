package com.schreibersolutions;

import java.util.HashMap;

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

    public int countOrbits(String start) {

        String orbits = map_hash.get(start);

        if (orbits.compareTo("COM") == 0) {
            return 1;
        } else {
            return countOrbits(map_hash.get(start))+1;
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
