package com.schreibersolutions;

import javafx.util.Pair;

import java.util.ArrayList;

public class ScaffoldCamera {
    boolean LOG = true;

    private static long[] program = {1,330,331,332,109,2660,1101,0,1182,15,1102,1439,1,24,1001,0,0,570,1006,570,36,101,0,571,0,1001,570,-1,570,1001,24,1,24,1105,1,18,1008,571,0,571,1001,15,1,15,1008,15,1439,570,1006,570,14,21101,58,0,0,1105,1,786,1006,332,62,99,21102,333,1,1,21102,1,73,0,1105,1,579,1101,0,0,572,1101,0,0,573,3,574,101,1,573,573,1007,574,65,570,1005,570,151,107,67,574,570,1005,570,151,1001,574,-64,574,1002,574,-1,574,1001,572,1,572,1007,572,11,570,1006,570,165,101,1182,572,127,1002,574,1,0,3,574,101,1,573,573,1008,574,10,570,1005,570,189,1008,574,44,570,1006,570,158,1105,1,81,21101,340,0,1,1105,1,177,21102,477,1,1,1106,0,177,21102,514,1,1,21101,176,0,0,1106,0,579,99,21101,184,0,0,1106,0,579,4,574,104,10,99,1007,573,22,570,1006,570,165,1002,572,1,1182,21102,375,1,1,21101,0,211,0,1106,0,579,21101,1182,11,1,21102,222,1,0,1106,0,979,21102,1,388,1,21101,233,0,0,1106,0,579,21101,1182,22,1,21102,244,1,0,1105,1,979,21102,401,1,1,21101,255,0,0,1106,0,579,21101,1182,33,1,21102,1,266,0,1106,0,979,21102,414,1,1,21101,277,0,0,1105,1,579,3,575,1008,575,89,570,1008,575,121,575,1,575,570,575,3,574,1008,574,10,570,1006,570,291,104,10,21101,1182,0,1,21102,313,1,0,1106,0,622,1005,575,327,1101,1,0,575,21101,0,327,0,1106,0,786,4,438,99,0,1,1,6,77,97,105,110,58,10,33,10,69,120,112,101,99,116,101,100,32,102,117,110,99,116,105,111,110,32,110,97,109,101,32,98,117,116,32,103,111,116,58,32,0,12,70,117,110,99,116,105,111,110,32,65,58,10,12,70,117,110,99,116,105,111,110,32,66,58,10,12,70,117,110,99,116,105,111,110,32,67,58,10,23,67,111,110,116,105,110,117,111,117,115,32,118,105,100,101,111,32,102,101,101,100,63,10,0,37,10,69,120,112,101,99,116,101,100,32,82,44,32,76,44,32,111,114,32,100,105,115,116,97,110,99,101,32,98,117,116,32,103,111,116,58,32,36,10,69,120,112,101,99,116,101,100,32,99,111,109,109,97,32,111,114,32,110,101,119,108,105,110,101,32,98,117,116,32,103,111,116,58,32,43,10,68,101,102,105,110,105,116,105,111,110,115,32,109,97,121,32,98,101,32,97,116,32,109,111,115,116,32,50,48,32,99,104,97,114,97,99,116,101,114,115,33,10,94,62,118,60,0,1,0,-1,-1,0,1,0,0,0,0,0,0,1,6,26,0,109,4,1202,-3,1,586,21002,0,1,-1,22101,1,-3,-3,21101,0,0,-2,2208,-2,-1,570,1005,570,617,2201,-3,-2,609,4,0,21201,-2,1,-2,1106,0,597,109,-4,2105,1,0,109,5,2101,0,-4,629,21001,0,0,-2,22101,1,-4,-4,21102,0,1,-3,2208,-3,-2,570,1005,570,781,2201,-4,-3,653,20102,1,0,-1,1208,-1,-4,570,1005,570,709,1208,-1,-5,570,1005,570,734,1207,-1,0,570,1005,570,759,1206,-1,774,1001,578,562,684,1,0,576,576,1001,578,566,692,1,0,577,577,21102,1,702,0,1105,1,786,21201,-1,-1,-1,1106,0,676,1001,578,1,578,1008,578,4,570,1006,570,724,1001,578,-4,578,21102,1,731,0,1106,0,786,1106,0,774,1001,578,-1,578,1008,578,-1,570,1006,570,749,1001,578,4,578,21102,756,1,0,1106,0,786,1105,1,774,21202,-1,-11,1,22101,1182,1,1,21102,1,774,0,1105,1,622,21201,-3,1,-3,1105,1,640,109,-5,2106,0,0,109,7,1005,575,802,21001,576,0,-6,20101,0,577,-5,1106,0,814,21102,1,0,-1,21101,0,0,-5,21101,0,0,-6,20208,-6,576,-2,208,-5,577,570,22002,570,-2,-2,21202,-5,37,-3,22201,-6,-3,-3,22101,1439,-3,-3,1202,-3,1,843,1005,0,863,21202,-2,42,-4,22101,46,-4,-4,1206,-2,924,21101,1,0,-1,1106,0,924,1205,-2,873,21102,1,35,-4,1106,0,924,2101,0,-3,878,1008,0,1,570,1006,570,916,1001,374,1,374,1201,-3,0,895,1101,2,0,0,2102,1,-3,902,1001,438,0,438,2202,-6,-5,570,1,570,374,570,1,570,438,438,1001,578,558,921,21002,0,1,-4,1006,575,959,204,-4,22101,1,-6,-6,1208,-6,37,570,1006,570,814,104,10,22101,1,-5,-5,1208,-5,33,570,1006,570,810,104,10,1206,-1,974,99,1206,-1,974,1101,0,1,575,21101,0,973,0,1106,0,786,99,109,-7,2105,1,0,109,6,21102,0,1,-4,21101,0,0,-3,203,-2,22101,1,-3,-3,21208,-2,82,-1,1205,-1,1030,21208,-2,76,-1,1205,-1,1037,21207,-2,48,-1,1205,-1,1124,22107,57,-2,-1,1205,-1,1124,21201,-2,-48,-2,1105,1,1041,21102,1,-4,-2,1106,0,1041,21101,-5,0,-2,21201,-4,1,-4,21207,-4,11,-1,1206,-1,1138,2201,-5,-4,1059,2102,1,-2,0,203,-2,22101,1,-3,-3,21207,-2,48,-1,1205,-1,1107,22107,57,-2,-1,1205,-1,1107,21201,-2,-48,-2,2201,-5,-4,1090,20102,10,0,-1,22201,-2,-1,-2,2201,-5,-4,1103,2102,1,-2,0,1106,0,1060,21208,-2,10,-1,1205,-1,1162,21208,-2,44,-1,1206,-1,1131,1105,1,989,21101,439,0,1,1105,1,1150,21101,0,477,1,1106,0,1150,21102,514,1,1,21102,1,1149,0,1105,1,579,99,21101,0,1157,0,1105,1,579,204,-2,104,10,99,21207,-3,22,-1,1206,-1,1138,2101,0,-5,1176,2102,1,-4,0,109,-6,2105,1,0,18,9,28,1,7,1,10,9,9,1,7,1,10,1,17,1,7,1,10,1,7,5,5,1,7,1,10,1,7,1,3,1,5,1,7,1,10,1,7,1,1,13,3,1,10,1,7,1,1,1,1,1,5,1,3,1,3,1,10,1,3,9,5,13,6,1,3,1,3,1,1,1,11,1,3,1,3,1,6,1,1,11,9,5,3,1,6,1,1,1,1,1,3,1,1,1,1,1,17,1,6,5,3,1,1,1,1,1,17,1,8,1,5,1,1,1,1,1,17,1,8,1,5,5,17,1,8,1,7,1,19,1,8,1,7,1,17,9,2,1,7,1,17,1,1,1,5,1,2,9,17,1,1,1,5,1,28,1,1,1,5,1,6,11,9,5,5,1,6,1,9,1,9,1,1,1,7,1,6,1,7,5,7,1,1,1,7,1,6,1,7,1,1,1,1,1,7,1,1,1,7,1,6,5,3,1,1,1,1,1,7,11,10,1,3,1,1,1,1,1,9,1,14,9,1,1,1,1,9,1,18,1,5,1,1,1,9,1,18,1,5,13,18,1,7,1,28,1,7,1,28,1,7,1,28,9,18};
    IntcodeComputer computer = new IntcodeComputer();
    CameraSurface mySurface = new CameraSurface(0);

    public void init() {
        computer.program = program;
        computer.isInteractive = false;
        int x = 0;
        int y = 0;
        computer.run();

        while (computer.outputs.size() > 0)
        {

            for (int i = 0; i < computer.outputs.size(); i++) {
                long result = computer.outputs.get(i);
                char c = (char) result;
                if (c == 10) {
                    --y;
                    x = 0;
                } else {
                    mySurface.setColor(x, y, c);
                    ++x;
                }
                mySurface.displayCamera();
            }
            computer.outputs.clear();
            computer.resume();
        }

    }

    public long sumAlignmentParameters() {
        long retval = 0;

        ArrayList<Pair<Integer,Integer>> pairs = mySurface.getLocations('#');

        for (Pair p : pairs) {
            Integer x = (Integer) p.getKey();
            Integer y = (Integer) p.getValue();
            if ((mySurface.getColor(x-1,y) == '#') &&
                    (mySurface.getColor(x+1,y)== '#') &&
                    (mySurface.getColor(x,y-1) == '#') &&
                    (mySurface.getColor(x,y+1) == '#'))
            {
                // intersection
                retval = retval + x * Math.abs(y);
            }
        }

        return retval;
    }

    int NORTH = 1;
    int EAST = 2;
    int SOUTH = 3;
    int WEST = 4;

    private String getTurn(char dir, char desired) {
        if (dir == desired) return "";

        if((dir == 'N') && (desired == 'E')) {
            return "R,";
        }
        if((dir == 'E') && (desired == 'S')) {
            return "R,";
        }
        if((dir == 'S') && (desired == 'W')) {
            return "R,";
        }
        if((dir == 'W') && (desired == 'N')) {
            return "R,";
        }
        if((dir == 'N') && (desired == 'W')) {
            return "L,";
        }
        if((dir == 'W') && (desired == 'S')) {
            return "L,";
        }
        if((dir == 'S') && (desired == 'E')) {
            return "L,";
        }
        if((dir == 'E') && (desired == 'N')) {
            return "L,";
        }
        return "X";

    }

    private int getNewX(int direction, int x) {
        if (direction == 'W') return x-1;
        if (direction == 'E') return x+1;
        return x;
    }

    private int getNewY(int direction, int y) {
        if (direction == 'N') return y+1;
        if (direction == 'S') return y-1;
        return y;
    }

    private char findNewDirection(int direction, int x, int y) {
        if (direction != 'E' && mySurface.getColor(x-1,y) == '#') return 'W';
        if (direction != 'W' && mySurface.getColor(x+1,y) == '#') return 'E';
        if (direction != 'N' && mySurface.getColor(x,y-1) == '#') return 'S';
        if (direction != 'S' && mySurface.getColor(x,y+1) == '#') return 'N';
        return 'X';
    }

    public String findTraverseDirections() {
        String retval = "";
        ArrayList<Pair<Integer,Integer>> pairs = mySurface.getLocations('^');

        int x = pairs.get(0).getKey();
        int y = pairs.get(0).getValue();
        char direction = 'N';
        char desired_direction = 'E';
        int step_count = 0;

        while (desired_direction != 'X') {
            retval = retval + getTurn(direction, desired_direction);
            direction = desired_direction;

            if (mySurface.getColor(getNewX(direction, x), getNewY(direction, y)) == '#') {
                x = getNewX(direction, x);
                y = getNewY(direction, y);
                ++step_count;
            } else // turn
            {
                retval = retval + String.valueOf(step_count) + ",";
                step_count = 0;
                desired_direction = findNewDirection(direction, x, y);
            }
        }

        return retval;
    }

    public long traverse(char[] main, char[] f_a, char[] f_b, char[] f_c) {
        long retval = 0;

        computer = new IntcodeComputer();
        program[0] = 2;
        computer.program = program;

        computer.isInteractive = false;

        computer.inputs.push((long) 10);
        computer.inputs.push((long) 'n');

        computer.inputs.push((long) 10);
        for (int i = f_c.length-1; i >= 0; i--) {
            computer.inputs.push((long) f_c[i]);
        }

        computer.inputs.push((long) 10);
        for (int i = f_b.length-1; i >=0; i--) {
            computer.inputs.push((long) f_b[i]);
        }

        computer.inputs.push((long) 10);
        for (int i = f_a.length-1; i >=0; i--) {
            computer.inputs.push((long) f_a[i]);
        }

        computer.inputs.push((long) 10);
        for (int i = main.length-1; i >=0; i--) {
            computer.inputs.push((long) main[i]);
        }
        int x = 0;
        int y = 0;

        computer.run();
        while (computer.outputs.size() > 0)
        {
            if (computer.outputs.size() > 3) {
                String str = "";
                for (int i = 0; i < computer.outputs.size(); i++) {
                    long result = computer.outputs.get(i);
                    char c = (char) result;
                    str = str + String.valueOf(result);
                }
                System.out.printf("%s\n",str);
            }

            for (int i = 0; i < computer.outputs.size(); i++) {
                long result = computer.outputs.get(i);
                retval = result;
                char c = (char) result;
                if (c == 10) {
                    --y;
                    x = 0;
                } else {
                    mySurface.setColor(x, y, c);
                    ++x;
                }
                mySurface.displayCamera();
            }
            computer.outputs.clear();
            computer.resume();
        }

        return retval;
    }

}
