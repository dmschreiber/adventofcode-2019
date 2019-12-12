package com.schreibersolutions;

public class Space {

    public Moon[] moons;

    public  Moon[] generateMoons(int number) {
        moons = new Moon[number];

        for (int index = 0; index < number; index++) {
            moons[index] = new Moon();
        }
        return moons;
    }

    Space() {

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
}
