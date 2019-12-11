package com.schreibersolutions;
import java.util.Arrays;
import java.util.Collections;

public class AsteroidMap {
    private String[][] values;
    private int[][] astroids;
    private Astroid[] astroid_objects;
    private Astroid bestAstroid;

    public class Astroid implements Comparable<Astroid> {
        public int myX, myY;
        public double angle;
        public boolean isVaporized = false;
        public int visibleAsteroids = 0;

        private int baseX, baseY;

        Astroid (int x, int y) {
            myX = x;
            myY = y;
            setBase(0,0);

        }

        @Override
        public int compareTo(Astroid o) {
            int retval = 0;
            if ((this.angle - o.angle) < 0) {
                retval = -1;
            } else if ((this.angle - o.angle) > 0) {
                retval = 1;
            }


            return retval;
        }

        void setBase(int x, int y) {
            baseX = x;
            baseY = y;

//            if ((myX==baseX) && (myY > baseY)) {
//                angle = -1 * Math.PI/2;
//            } else if ((myX==baseX) && (myY < baseY)) {
//                angle = Math.PI/2;
//            }
            double delta_x = (double) myX - baseX;
            delta_x = Math.abs(delta_x);
            double delta_y = (double) myY - baseY;
            delta_y = Math.abs(delta_y);
            double theta = Math.atan(delta_x/delta_y);

            if ((myX > baseX) && (myY < baseY)) {
                angle = theta;

            } else if ((myX > baseX) && (myY > baseY)) {
                angle = Math.PI - theta;
            } else if ((myX < baseX) && (myY > baseY)) {
                angle = Math.PI + theta;
            } else if ((myX < baseX) && (myY < baseY)) {
                angle = 2 * Math.PI - theta;
            } else if ((myY == baseY) && (myX > baseX)) {
                angle = Math.PI / 2;
            } else if ((myX == baseX) && (myY > baseY)) {
                angle = Math.PI;
            } else if ((myY == baseY) && (myX < baseX)) {
                angle = Math.PI * (3/2);
            } else if ((myX == baseX) && (myY < baseY)) {
                angle = 0;
            }


            if (Double.isNaN((angle))) {
                System.out.printf("Nan because my %d,%d vs base of %d,%d\n", myX, myY, baseX, baseY);
                angle = 0;
            }
        }
    }

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
        astroid_objects = new Astroid[countAsteroids];

        for (int y=0;y<rows.length;y++) {
            for (int x = 0;x<rows[y].length();x++) {
                values[x][y] = rows[y].substring(x,x+1);
                if (values[x][y].compareTo("#") == 0) {
                    astroid_objects[asteroidIndex] = new Astroid(x,y);
                    astroids[asteroidIndex][0] = x;
                    astroids[asteroidIndex][1] = y;
                    ++asteroidIndex;

                }
            }
        }

//        System.out.println(values);
    }

    public void bestLocationSolver(){
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
                        ++astroid_objects[astroid_a].visibleAsteroids;
                    }
                }
            }
        }

        int maxAstroids = 0;
        int max_x = -1, max_y = -1;

        for (int index = 0; index < astroid_objects.length; index++) {
            System.out.println(String.valueOf(astroids[index][0]) + "," + String.valueOf(astroids[index][1]) + "=" + String.valueOf(visible_astroids[index]));

            if (astroid_objects[index].visibleAsteroids > maxAstroids) {
                maxAstroids = astroid_objects[index].visibleAsteroids;
                max_x = astroids[index][0];
                max_y = astroids[index][1];
            }
        }

        for (int index = 0; index < visible_astroids.length; index++) {
            astroid_objects[index].setBase(max_x,max_y);
        }
        bestAstroid = new Astroid(max_x, max_y);

        System.out.println(String.valueOf(max_x) + "," + String.valueOf(max_y) + "=" + String.valueOf(maxAstroids));

    }

    private boolean isDirectLineOfSight(int x1, int y1, int x2, int y2) {
        boolean blocked = false;
        for (int astroid_c = 0; astroid_c < astroid_objects.length; astroid_c++) {
            Astroid astroid_c_object = astroid_objects[astroid_c];
            if ((astroid_c_object.myX != x1) || (astroid_c_object.myY != y1) &&
                    (astroid_c_object.myX != x2) || (astroid_c_object.myY != y2)) {
                if (findOnLine(x1, y1,
                        x2,y2,
                        astroid_c_object.myX, astroid_c_object.myY)) {
                    // c blocks a from b
                    blocked = true;
                }
            }
        }
        return (!blocked);
    }

    public void laserSweep() {

        int vaporizedOrder = 1;

        while ((astroid_objects.length > 1)) {
            System.out.println("Sweep");
            Arrays.sort(astroid_objects);
            for (int index = 0; index < astroid_objects.length; index++) {
                if ((astroid_objects[index].myX == bestAstroid.myX) && (astroid_objects[index].myY == bestAstroid.myY)) {
                    // skip
                } else if (isDirectLineOfSight(bestAstroid.myX, bestAstroid.myY, astroid_objects[index].myX, astroid_objects[index].myY)) {
                    astroid_objects[index].isVaporized = true;
                    System.out.printf("%d: %d,%d\n", vaporizedOrder++, astroid_objects[index].myX, astroid_objects[index].myY);
                }
            }

            int astroids_left = 0;
            for (int index = 0; index < astroid_objects.length; index++) {
                if (!astroid_objects[index].isVaporized) {
                    ++astroids_left;
                }
            }

            Astroid[] new_astroids = new Astroid[astroids_left];
            astroids_left = 0;
            for (int index = 0; index < astroid_objects.length; index++) {
                if (!astroid_objects[index].isVaporized) {
                    new_astroids[astroids_left++] = astroid_objects[index];
                }
            }
            astroid_objects = new_astroids;

        }


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
        if (Math.abs(y-(m*x+b))<0.00003) {
            if (Math.abs(y-(m*x+b)) > 0) {
//                System.out.printf("%f\n", Math.abs(y-(m*x+b)));
            }
            return ((Math.min(y1,y2) < y) && (y < Math.max(y1,y2)));
        }
        else {
            return false;
        }

    }
}
