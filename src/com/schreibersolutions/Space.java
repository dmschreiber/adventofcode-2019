package com.schreibersolutions;

public class Space {

    private int[][] initialState;

    public Moon[] moons;

    public  Moon[] generateMoons(int number, int[][] positions) {
        moons = new Moon[number];

        initialState = positions.clone();

        for (int index = 0; index < number; index++) {
            moons[index] = new Moon();
            moons[index].setPosition(positions[index][0], positions[index][1], positions[index][2]);
        }

        return moons;
    }

    Space() {

    }

    public boolean isBackToInitialState(int dimensions) {
        boolean retval = true;

        for (int m1 = 0; m1 < moons.length; m1++) {
                retval = retval && (moons[m1].position[dimensions] == initialState[m1][dimensions]);
        }
        return retval;
    }

    public boolean isBackToInitialState() {
        return (isBackToInitialState(0) && isBackToInitialState(1) && isBackToInitialState(2));
    }

    public void applyGravity() {
        for (int m1 = 0; m1 < moons.length; m1++) {
            for (int m2 = m1; m2 < moons.length; m2++) {
                if (m1 != m2) {
                    for (int dimensions = 0; dimensions < 3; dimensions++) {
                        if (moons[m1].position[dimensions] < moons[m2].position[dimensions]) {
                            ++moons[m1].velocity[dimensions];
                            --moons[m2].velocity[dimensions];
                        } else if (moons[m1].position[dimensions] > moons[m2].position[dimensions]) {
                            --moons[m1].velocity[dimensions];
                            ++moons[m2].velocity[dimensions];

                        }
                    }
                }
            }
        }
    }

    public void updatePositions() {
        for (Moon m: moons) {
            for (int dimensions = 0; dimensions < 3; dimensions++) {
                m.position[dimensions] += m.velocity[dimensions];
            }
        }
    }

    public void displayMoons() {
        for (Moon m: moons) {
                System.out.printf("pos=<x=%d, y=%d, z=%d>, vel=<x=%d, y=%d, z=%d>\n",
                        m.position[0], m.position[1], m.position[2],
                        m.velocity[0], m.velocity[1], m.velocity[2]);
        }
    }

    public int totalEnergy() {
        int energy = 0;

        displayMoons();
        for (Moon m: moons) {
                energy += ((Math.abs(m.position[0]) + Math.abs(m.position[1]) + Math.abs(m.position[2])) *
                 (Math.abs(m.velocity[0]) + Math.abs(m.velocity[1]) + Math.abs(m.velocity[2])));
        }
        return energy;
    }


    public void timeLapse (int howMany) {

        for (int index = 0; index < howMany; index++) {
            System.out.printf("After %d steps\n", index);
            displayMoons();
            applyGravity();
            updatePositions();
        }
    }

    public void findRepeat () {
        long timeCount = 0;
        boolean keepGoing = true;
        long[] seeds = new long[3];

        while (keepGoing) {
            applyGravity();
            updatePositions();
            ++timeCount;
            // if ((timeCount % 10000) == 0) System.out.println(timeCount);

            if (isBackToInitialState(0)) {
                System.out.printf("%d:%d %d\n", 0, timeCount, seeds[0]);
                if (seeds[0] > 0) {
                }
                else {
                    seeds[0] = timeCount+1;
                }
            }

            if (isBackToInitialState(1)) {
                System.out.printf("%d:%d %d\n", 1, timeCount, seeds[1]);
                if (seeds[1] > 0) {
                }
                else {
                    seeds[1] = timeCount+1;
                }
            }
            if (isBackToInitialState(2)) {
                System.out.printf("%d:%d %d\n", 2, timeCount, seeds[2]);
                if (seeds[2] > 0) {
                }
                else {
                    seeds[2] = timeCount+1;
                }
            }

            keepGoing = ((seeds[0] == 0) || (seeds[1] == 0) || (seeds[2] == 0));
        }

        System.out.printf("<%d, %d, %d>\n", seeds[0], seeds[1], seeds[2]);
        System.out.println(lcm(lcm(seeds[0],seeds[1]),seeds[2]));
        

    }

    public long lcm(long number1, long number2) {
        if (number1 == 0 || number2 == 0) {
            return 0;
        }
        long absNumber1 = Math.abs(number1);
        long absNumber2 = Math.abs(number2);
        long absHigherNumber = Math.max(absNumber1, absNumber2);
        long absLowerNumber = Math.min(absNumber1, absNumber2);
        long lcm = absHigherNumber;
        while (lcm % absLowerNumber != 0) {
            lcm += absHigherNumber;
        }
        return lcm;
    }
}
