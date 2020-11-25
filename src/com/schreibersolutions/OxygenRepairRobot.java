package com.schreibersolutions;

import java.util.ArrayList;
import java.util.Stack;

public class OxygenRepairRobot {
    IntcodeComputer computer = new IntcodeComputer();
    Surface mySurface;
    ArrayList<Integer> robot_steps = new ArrayList<>();
    ArrayList<Integer> robot_control = new ArrayList<>();

    int DIRECTION_NORTH = 1;
    int DIRECTION_SOUTH = 2;
    int DIRECTION_WEST = 3;
    int DIRECTION_EAST = 4;

    int SPACE_WALL = 100;
    int SPACE_BLANK = 101;
    int SPACE_OXYGEN = 102;

    int RESULT_WALL = 0;
    int RESULT_MOVED = 1;
    int RESULT_MOVED_OXYGEN_SYSTEM = 2;

    int direction = 0; // 1-N; 2-S; 3-W; 4-E;
    int myX = 0;
    int myY = 0;
    long[] initial_instructions;

    boolean exploreSpaces = true;

    private void init(long[] instructions) {
        initial_instructions = instructions;
        computer.program = instructions;
        computer.isInteractive = false;

        mySurface = new Surface(0); // default all black
        mySurface.setColor(0,0,SPACE_BLANK);

    }

    OxygenRepairRobot(long[] instructions) {
        init(instructions);
        robot_control.add(DIRECTION_NORTH);
        robot_control.add(DIRECTION_EAST);
        robot_control.add(DIRECTION_SOUTH);
        robot_control.add(DIRECTION_WEST);
    }

    OxygenRepairRobot(long[] instructions, ArrayList<Integer> initial_steps, Surface s, int x, int y) {
        init(instructions);
        robot_steps = initial_steps;
        mySurface = s;
        myX = x;
        myY = y;

        exploreSpaces = true;
        robot_control.clear();
        robot_control.add(DIRECTION_NORTH);
        robot_control.add(DIRECTION_EAST);
        robot_control.add(DIRECTION_SOUTH);
        robot_control.add(DIRECTION_WEST);
        findOxygenSystem();

    }

    private int getNewX(int direction) {
        if (direction == DIRECTION_WEST) {
            return myX - 1;
        } else if (direction == DIRECTION_EAST) {
            return myX + 1;
        } else {
            return myX;
        }
    }

    private int getNewY(int direction) {
        if (direction == DIRECTION_NORTH) {
            return myY + 1;
        } else if (direction == DIRECTION_SOUTH) {
            return myY - 1;
        } else {
            return myY;
        }
    }

    public void findOxygenSystem() {
        computer.run();
        int instruction = 0;

        while ((computer.isRunning) && (instruction < robot_control.size())) {
            int direction = robot_control.get(instruction++);
            if (mySurface.getColor(getNewX(direction),getNewY(direction)) == SPACE_BLANK) {
                System.out.printf("I've been there before, so I'll skip it\n");
            } else {
                // TODO: move(direction);
                computer.inputs.push((long) direction);
                computer.resume();

                long result = computer.outputs.pop();
                System.out.printf(!exploreSpaces ? "Replay move %d, result %d\n": "Explore move %d, result %d\n", direction, result);

                if (result == RESULT_WALL) {
                    // wall
                    mySurface.setColor(getNewX(direction), getNewY(direction), SPACE_WALL);
                } else {
                    // moved
                    robot_steps.add(direction);
                    myX = getNewX(direction);
                    myY = getNewY(direction);
                    if (result == RESULT_MOVED) {
                        mySurface.setColor(myX, myY, SPACE_BLANK);
                        if (exploreSpaces) {
                            OxygenRepairRobot r = new OxygenRepairRobot(computer.program, (ArrayList<Integer>) robot_steps.clone(), mySurface, myX, myY);
                        }
                        // TODO: move(oppositeDirection(direction));
                    } else if (result == RESULT_MOVED_OXYGEN_SYSTEM) {
                        // success!
                        mySurface.setColor(myX, myY, SPACE_OXYGEN);
                        System.out.print(robot_steps);
                    }
                }


//            Surface.clearScreen();
//            mySurface.displaySurface();
                try {
                    Thread.sleep(10);
                } catch (Exception e) {

                }
                if ((result == RESULT_MOVED_OXYGEN_SYSTEM)) {
                    mySurface.displaySurface();
                    break;
                }
            } // else not someplace we've been before
        }
        System.out.printf("Explored all my options!\n");
        System.out.print(robot_steps.size());
        mySurface.displaySpace(myX, myY);

    }

}
