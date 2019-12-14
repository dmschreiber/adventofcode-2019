package com.schreibersolutions;

public class HullPaintingRobot {

    IntcodeComputer computer = new IntcodeComputer();
    Surface mySurface;
    int direction = 0; // 0-N; 1-E; 2-S; 3-W
    int myX = 0;
    int myY = 0;

    HullPaintingRobot(long[] instructions) {
        computer.program = instructions;
        computer.isInteractive = false;

    }
    public void paint(Surface s) {
        mySurface = s;

        computer.run();
        while (computer.isRunning) {
            computer.inputs.push((long) mySurface.getColor(myX, myY));
            computer.resume();

            long turn = computer.outputs.pop();
            long newColor = computer.outputs.pop();

            s.setColor(myX, myY, (int) newColor);
            if (turn == 0) {
                --direction; }
            else if (turn == 1) {
                ++direction;
            }

            if (direction < 0) direction = 3;
            if (direction > 3) direction = 0;

            if (direction == 0) {
                ++myY;
            } else if (direction == 1) {
                ++myX;
            } else if (direction == 2) {
                --myY;
            } else if (direction == 3) {
                --myX;
            }

            Surface.clearScreen();
            System.out.printf("Color %d, direction %d\n",newColor,direction);
            s.displaySurface();
            try {
                Thread.sleep(10);
            } catch (Exception e) {

            }
        }
    }
}
