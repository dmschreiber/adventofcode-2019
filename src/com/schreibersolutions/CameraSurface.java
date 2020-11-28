package com.schreibersolutions;

public class CameraSurface extends Surface {

    CameraSurface(int defaultValue) {
        super(defaultValue);
    }

    public void displayCamera() {
        for (int y = upperY+1; y > lowerY-2;y--) {
            System.out.printf("%3d:",y);
            for (int x = lowerX-1; x < upperX+2;x++) {
                char c = (char) getColor(x,y);
                if (c == 0) {
                    System.out.print(" ");
                } else {
                    System.out.print(String.valueOf(c));
                }
            }
            System.out.println();

        }
    }

}
