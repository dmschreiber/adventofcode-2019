package com.schreibersolutions;

import java.util.Scanner;

public class ArcadeCabinet {
    IntcodeComputer computer = new IntcodeComputer();
    ArcadeConsole s = new ArcadeConsole(0);
    long score = 0;

    ArcadeCabinet (long[] program) {
        computer.program = program;
        computer.isInteractive = false;
    }

    private int getJoystick() {
        int retval = 0;
        try {
//            char c = (char) System.in.read();
            if (lastBall < lastBar) {
                retval = -1;
            } else if (lastBall > lastBar) {
                retval = 1;
            } else {
                retval = 0;
            }
        } catch (Exception e) {

        }
        return retval;
    }
    private long lastBall = 0;
    private long lastBar = 0;

    private void displayOutputs() {
        while (computer.outputs.size() > 0) {
            long color = computer.outputs.pop();
            long y = computer.outputs.pop();
            long x = computer.outputs.pop();

            if ((x==-1) && (y==0)) {
                score = color;
            }

            if (color == 4) { lastBall = x; }
            if (color == 3) {lastBar = x; }

            s.setColor((int) x, (int) (-1*y), (int) color);
            Surface.clearScreen();
            s.displaySurface();
            System.out.printf("Score: %d\n", score);
        }
    }
    public void play() {
        computer.run();

        while (computer.isRunning) {
            if (computer.isAwaitingInput) {
                int joystick = getJoystick();
                computer.inputs.push((long) joystick);
            }
            computer.resume();
            displayOutputs();

        }


    }

    public int countBlockTiles() {
        int retval = 0;

        for (int p : s.panels.values()) {
            if (p == 2) {
                ++retval;
            }
        }
        return retval;
    }

}
