package com.schreibersolutions;

import java.util.ArrayList;

public class OxygenRepairRobot {
    boolean LOG = false;

    IntcodeComputer computer = new IntcodeComputer();
    Surface mySurface;
    ArrayList<Integer> robot_steps = new ArrayList<>();
    ArrayList<Integer> robot_control = new ArrayList<>();

    int DIRECTION_NORTH = 1;
    int DIRECTION_SOUTH = 2;
    int DIRECTION_WEST = 3;
    int DIRECTION_EAST = 4;

    static int SPACE_WALL = 100;
    static int SPACE_BLANK = 101;
    static int SPACE_OXYGENATOR = 102;
    static int SPACE_OXYGEN = 103;

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

    private long move(int direction) {
        computer.inputs.push((long) direction);
        computer.resume();

        long result = computer.outputs.pop();
        if (result != RESULT_WALL) {
            myX = getNewX(direction);
            myY = getNewY(direction);
        }
        return result;
    }

    private int oppositeDirection(int direction) {
        int retval = 0;

        if (direction == DIRECTION_SOUTH) retval = DIRECTION_NORTH;
        if (direction == DIRECTION_NORTH) retval = DIRECTION_SOUTH;
        if (direction == DIRECTION_EAST) retval = DIRECTION_WEST;
        if (direction == DIRECTION_WEST) retval = DIRECTION_EAST;

        return retval;
    }

    public void findOxygenSystem() {
        computer.run();
        int instruction = 0;

        while ((computer.isRunning) && (instruction < robot_control.size())) {
            int direction = robot_control.get(instruction++);
            if (LOG) System.out.printf("Move %d\n", direction);
            if (mySurface.getColor(getNewX(direction),getNewY(direction)) == SPACE_BLANK) {
                if (LOG) System.out.printf("I've been there before, so I'll skip it\n");
            } else {
                long result = move(direction);
                if (LOG) System.out.printf("Move %d, result %d\n", direction, result);

                if (result == RESULT_WALL) {
                    // wall
                    mySurface.setColor(getNewX(direction), getNewY(direction), SPACE_WALL);
                } else {
                    // moved
                    if (result == RESULT_MOVED) {
                        mySurface.setColor(myX, myY, SPACE_BLANK);
                    } else if (result == RESULT_MOVED_OXYGEN_SYSTEM) {
                        // success!
                        mySurface.setColor(myX, myY, SPACE_OXYGENATOR);

                        ArrayList<Integer> new_steps = (ArrayList<Integer>) robot_steps.clone();
                        new_steps.add(direction);
                        mySurface.solutions.add(new_steps);

                        System.out.printf("Depth: %d\n", robot_steps.size()+1);
                        System.out.print(robot_steps);
                    }

                    if (exploreSpaces) {
                        ArrayList<Integer> new_steps = (ArrayList<Integer>) robot_steps.clone();
                        new_steps.add(direction);
                        OxygenRepairRobot r = new OxygenRepairRobot(computer.program,  new_steps, mySurface, myX, myY);
                    }
                    move(oppositeDirection(direction));
                }


//            Surface.clearScreen();
//            mySurface.displaySurface();
                try {
                    Thread.sleep(10);
                } catch (Exception e) {

                }
                if ((result == RESULT_MOVED_OXYGEN_SYSTEM)) {
                    System.out.printf("found\n");
//                    break;
                }
            } // else not someplace we've been before
        }
        if ((mySurface.solutions.size() > 0) && (robot_steps.size() == 0)) {
            mySurface.displaySpace(0, 0);
            System.out.printf("solution %d\n", mySurface.solutions.size());
            for (ArrayList<Integer> sol : mySurface.solutions) {
                System.out.printf("Steps %d\n", sol.size());
                System.out.print(sol);
            }
        }
    }

}
