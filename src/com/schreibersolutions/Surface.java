package com.schreibersolutions;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.ArrayList;

public class Surface {
    int lowerX = 0, lowerY = 0, upperX = 0, upperY = 0;
    private int myDefault;
    int paintedPanelCount = 0;
    HashMap<Pair<Integer,Integer>,Integer> panels = new HashMap<Pair<Integer,Integer>,Integer>();
    ArrayList<ArrayList<Integer>> solutions = new ArrayList<>();

    Surface (int defaultValue) {
        myDefault = defaultValue;
    }

    public void readFromFile(String filename) throws IOException {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String         line = null;
            StringBuilder  stringBuilder = new StringBuilder();
            String         ls = System.getProperty("line.separator");
            int x = 0;
            int y = 0;

            try {
                while((line = reader.readLine()) != null) {
                    System.out.println(line);
                    for (int i = 0; i< line.length(); i++) {
                        setColor(x,y,line.charAt(i));
                        ++x;
                    }
                    x=0;
                    --y;
                }

            } finally {
                reader.close();
            }
    }

    public ArrayList<Pair<Integer,Integer>> getLocations(int color) {
        ArrayList<Pair<Integer,Integer>> retval = new ArrayList<>();

        for (Pair p: panels.keySet()) {
            if (panels.get(p) == color) {
                retval.add(p);
            }
        }
        return retval;
    }

    public int getColor(int x, int y) {
        int color = myDefault;

        Pair<Integer,Integer> loc = new Pair(x,y);

        if (panels.containsKey(loc)) {
            color = panels.get(loc);
        }
        return color;
    }
    public static void clearScreen() {
        System.out.print("\033[H");
        System.out.flush();
    }

    public void setColor(int x, int y, int color) {
        Pair<Integer,Integer> loc = new Pair(x,y);
        lowerX = Math.min(lowerX,x);
        upperX = Math.max(upperX,x);
        lowerY = Math.min(lowerY,y);
        upperY = Math.max(upperY,y);

        if (panels.containsKey(loc)) {
            panels.replace(loc,color);
            color = panels.get(loc);
        } else {
            ++paintedPanelCount;
            panels.put(loc,color);
        }

    }

    public void displaySurface() {
        System.out.printf("%d to %d, %d to %d\n", lowerY, upperY, lowerX, upperX);
        for (int y = upperY+1; y > lowerY-2;y--) {
            for (int x = lowerX-1; x < upperX+2;x++) {
                if (getColor(x,y) == 0) {
                    System.out.print(" ");
                } else {
                    System.out.print("\u2588");
                }
            }
            System.out.println();

        }
    }

    public void displaySpace(int myX, int myY) {
        System.out.printf("%d to %d, %d to %d\n", lowerY, upperY, lowerX, upperX);
        for (int y = upperY+1; y > lowerY-2;y--) {
            System.out.printf("%3d:",y);
            for (int x = lowerX-1; x < upperX+2;x++) {
                if ((x == myX) && (y == myY)) {
                    System.out.print("D");
                } else if (getColor(x,y) == OxygenRepairRobot.SPACE_WALL) {
                    System.out.print("#");
                } else if (getColor(x,y) == OxygenRepairRobot.SPACE_BLANK){
                    System.out.print(".");
                } else if (getColor(x,y) == OxygenRepairRobot.SPACE_OXYGENATOR) {
                    System.out.print("@");
                } else if (getColor(x,y) == OxygenRepairRobot.SPACE_OXYGEN) {
                    System.out.print("O");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();

        }
    }
}
