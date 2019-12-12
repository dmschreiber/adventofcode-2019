package com.schreibersolutions;

public class Moon {

    public int[] position = new int[3];
    public int[] velocity = new int[3];

    Moon () {
        setVelocity(0,0,0);
    }
    public void setPosition(int x, int y, int z) {
        position[0] = x;
        position[1] = y;
        position[2] = z;
    }

    public int getX() {
        return position[0];
    }
    public int getY() {
        return position[1];
    }
    public int getZ() {
        return position[2];
    }

    public void setVelocity(int x, int y, int z) {
        velocity[0] = x;
        velocity[1] = y;
        velocity[2] = z;
    }
}
