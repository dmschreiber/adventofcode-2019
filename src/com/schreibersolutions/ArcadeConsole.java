package com.schreibersolutions;

public class ArcadeConsole extends Surface {

    ArcadeConsole(int defaultValue) {
        super(defaultValue);
    }

    @Override
    public void displaySurface() {
            for (int y = upperY+1; y > lowerY-2;y--) {
                for (int x = lowerX-1; x < upperX+2;x++) {
                    if (getColor(x,y) == 0) {
                        System.out.print(" ");
                    } else if (getColor(x,y) == 1) {
                        System.out.print("\u2588");
                    } else if (getColor(x,y) == 2) {
                        System.out.print("#");
                    } else if (getColor(x,y) == 3) {
                        System.out.print("=");
                    } else if (getColor(x,y) == 4) {
                        System.out.print("O");
                    }
                }
                System.out.println();

            }
    }

}
