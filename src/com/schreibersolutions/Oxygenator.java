package com.schreibersolutions;

import javafx.util.Pair;

public class Oxygenator {
    private Surface map = null;
    public int minutes = 0;
    private boolean complete = false;

    public Oxygenator(Surface s) {
        map = s;
        for (Pair<Integer,Integer> p : map.getLocations(OxygenRepairRobot.SPACE_OXYGENATOR)) {
            map.setColor(p.getKey(), p.getValue(), OxygenRepairRobot.SPACE_OXYGEN);
        }
    }

    public boolean incomplete() {
        // check if there's any empty space left
        return !complete;
    }

    private boolean oxygenate_point(int x, int y) {
        boolean retval = false;

        if (map.getColor(x-1,y) == OxygenRepairRobot.SPACE_BLANK) {
            map.setColor(x-1,y,OxygenRepairRobot.SPACE_OXYGEN);
            retval = true;
        }
        if (map.getColor(x+1,y) == OxygenRepairRobot.SPACE_BLANK) {
            map.setColor(x+1,y,OxygenRepairRobot.SPACE_OXYGEN);
            retval = true;
        }
        if (map.getColor(x,y-1) == OxygenRepairRobot.SPACE_BLANK) {
            map.setColor(x,y-1,OxygenRepairRobot.SPACE_OXYGEN);
            retval = true;
        }
        if (map.getColor(x,y+1) == OxygenRepairRobot.SPACE_BLANK) {
            map.setColor(x,y+1,OxygenRepairRobot.SPACE_OXYGEN);
            retval = true;
        }
        return retval;
    }

    public void oxygenate() {
        boolean retval = false;

        // find all oxygens and oxygenate neighbors
        for (Pair<Integer,Integer> p : map.getLocations(OxygenRepairRobot.SPACE_OXYGEN)) {
            retval = oxygenate_point(p.getKey(), p.getValue()) || retval;
        }

        if (retval) {
            complete = false;
            ++minutes;
        } else {
            complete = true;
        }
    }
}
