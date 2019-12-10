package com.schreibersolutions;
import java.util.Arrays;

public class AsteroidMap {
    private String[][] values;
    private int[][] astroids;


    AsteroidMap(String data) {
        String[] rows = data.split("\n");
        values = new String[rows[0].length()][rows.length];
        int countAsteroids = 0;
        int asteroidIndex = 0;

        for (int y=0;y<rows.length;y++) {
            for (int x = 0;x<rows[y].length();x++) {
                values[x][y] = rows[y].substring(x,x+1);
                if (values[x][y].compareTo("#") == 0) {
                    ++countAsteroids;
                }
            }
        }
        astroids = new int[countAsteroids][2];
        for (int y=0;y<rows.length;y++) {
            for (int x = 0;x<rows[y].length();x++) {
                values[x][y] = rows[y].substring(x,x+1);
                if (values[x][y].compareTo("#") == 0) {
                    astroids[asteroidIndex][0] = x;
                    astroids[asteroidIndex][1] = y;
                    ++asteroidIndex;

                }
            }
        }

//        System.out.println(values);
    }

    public void solver(){
        int[] visible_astroids = new int[astroids.length];
        boolean blocked = false;

        for (int astroid_a = 0; astroid_a < astroids.length; astroid_a++) {
            visible_astroids[astroid_a] = 0;
            for (int astroid_b = 0; astroid_b < astroids.length; astroid_b++) {
                if (astroid_a != astroid_b) {
                    blocked = false;
                    for (int astroid_c = 0; astroid_c < astroids.length; astroid_c++) {
                        if ((astroid_c != astroid_a) && (astroid_c != astroid_b)) {
                            if (findOnLine(astroids[astroid_a][0], astroids[astroid_a][1],
                                    astroids[astroid_b][0], astroids[astroid_b][1],
                                    astroids[astroid_c][0], astroids[astroid_c][1])) {
                                // c blocks a from b
                                blocked = true;
                            }
                        }
                    }
                    if (!blocked) {
                        ++visible_astroids[astroid_a];
                    }
                }
            }
        }

        int maxAstroids = 0;
        int max_x = -1, max_y = -1;

        for (int index = 0; index < visible_astroids.length; index++) {
            System.out.println(String.valueOf(astroids[index][0]) + "," + String.valueOf(astroids[index][1]) + "=" + String.valueOf(visible_astroids[index]));

            if (visible_astroids[index] > maxAstroids) {
                maxAstroids = visible_astroids[index];
                max_x = astroids[index][0];
                max_y = astroids[index][1];
            }
        }

        System.out.println(String.valueOf(max_x) + "," + String.valueOf(max_y) + "=" + String.valueOf(maxAstroids));

    }

    public boolean findOnLine(int x1, int y1, int x2, int y2, int x, int y) {
        float m = 0;
        float b = 0;

        if (x2 == x1) {
            if (x == x1) {
                return ((Math.min(y1,y2) < y) && (y < Math.max(y1,y2)));
            } else {
                return false;
            }
        }

        if (y2 == y1) {
            if (y == y1) {
                return ((Math.min(x1,x2) < x) && (x < Math.max(x1,x2)));
            } else {
                return false;
            }
        }
        m = (float)(y2-y1)/(float) (x2-x1);
        b = y2 - m*x2;

        // y = mx + b
//        if ((x1 == 14) && (y1 == 1) && (Math.abs(y-(m*x+b))<0.05)) {
//            System.out.printf("%d=%f*%d+%f\n", y, m, x, b);
//            System.out.printf("%f\n",m*x+b);
//        }
        if (Math.abs(y-(m*x+b))<0.001) {
            return ((Math.min(y1,y2) < y) && (y < Math.max(y1,y2)));
        }
        else {
            return false;
        }

    }
}
