package com.schreibersolutions;
import javafx.util.Pair;
import java.util.HashMap;

public class Surface {
    int lowerX = 0, lowerY = 0, upperX = 0, upperY = 0;
    private int myDefault;
    int paintedPanelCount = 0;
    HashMap<Pair<Integer,Integer>,Integer> panels = new HashMap<Pair<Integer,Integer>,Integer>();

    Surface (int defaultValue) {
        myDefault = defaultValue;
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
            for (int x = lowerX-1; x < upperX+2;x++) {
                if ((x == myX) && (y == myY)) {
                    System.out.print("D");
                } else if (getColor(x,y) == 100) {
                    System.out.print("#");
                } else if (getColor(x,y) == 101){
                    System.out.print(".");
                } else if (getColor(x,y) == 102) {
                    System.out.print("@");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();

        }
    }
}
