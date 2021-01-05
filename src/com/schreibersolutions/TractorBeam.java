package com.schreibersolutions;

import java.util.HashMap;

public class TractorBeam {
    IntcodeComputer computer;

    TractorBeam() {

    }

    private static HashMap<String,Long> probe_map = new HashMap<>();

    private long probe(long row, long col) {
        String key = String.format("%4d,%4d",row,col);
        if (probe_map.containsKey(key)) {
            return probe_map.get(key);
        }

        computer = new IntcodeComputer();
        computer.program = Constants.dec19_puzzleinput;
        computer.isInteractive = false;

        computer.inputs.push(row);
        computer.inputs.push(col);

        computer.run();

        if (computer.outputs.size() > 0) {
            long result = computer.outputs.pop();
            probe_map.put(key, result);
            return result;

        } else {
            return 0;
        }
    }

    void draw() {
        long row = 0;
        long col = 0;
        long total = 0;

        System.out.println("Beam starting");

        System.out.print("  ");
        for (col = 0; col < 50; col++) {
            System.out.printf("%1d", col % 10);
        }
        System.out.println();
        for (row = 0; row < 50; row++) {
            System.out.printf("%2d",row);
            for (col = 0; col < 150; col++) {
                if ( probe(row,col) > 0 ) {
                    System.out.print("#");
                    ++total;
                } else {
                    System.out.print(".");

                }
            }
            System.out.println();
        }
        System.out.printf("Total is %d", total);
    }

    private static HashMap<String,Integer> map = new HashMap<>();
    int checkSquare (int width, int start_row, int start_col) {
        String key = String.format("%4d,%4d",start_row,start_col);
        if (map.containsKey(key)) {
            return map.get(key);
        }
        int total = 0;
        for (int row = start_row; row < start_row + width; row++) {
            for (int col = start_col; col < start_col + width; col++) {
                if (probe(row,col) == 0) {
//                    System.out.printf("Row %d Col %d Total %d\n", start_row, start_col, total);
                    return total;
                } else {
                    total +=1;
                }
            }
        }

        map.put(key,total);
        return total;
    }

    String findSquare(int width) {
        int row = 3*width;
        int col = 8*width;
//        if (row > 100) { row = row + 80; col = col + 200;}
        int max_row = 300*width;
        int max_col = 800*width;

        if ( (checkSquare(width,max_row, max_col) != width*width) || (checkSquare(width,row,col) == width*width)) {
            return ("Assumption Fails");
        }

        boolean should_continue = true;
        int steps = 0;

        while (should_continue) {
            int how_many_lo = checkSquare(width, row, col);
            int how_many_hi = checkSquare(width, max_row, max_col);
            int how_many_mid = checkSquare(width, (max_row+row)/2,(max_col+col)/2);
//            should_continue = how_many_mid < width*width;

//            System.out.printf("%4d, %4d - %4d,%4d\n",col, row, max_col, max_row);
            System.out.printf("%4d < %4d < %4d\n",how_many_lo, how_many_mid, how_many_hi);
            if (should_continue) {
                if (how_many_lo == width*width) {
                    max_col = col;
                    max_row = row;
                    should_continue = false;
                } else if (how_many_mid == width*width) {
                    max_row = (max_row+row)/2;
                    max_col = (max_col+col)/2;
                } else {
                     if (checkSquare(width, row + 1, col) < checkSquare(width, row, col + 1)) {
                        col = col + 1;
                    } else {
                        row = row + 1;
                    }
                }
//                System.out.printf("New lo-hi are %4d, %4d - %4d,%4d\n",col, row, max_col, max_row);
                ++steps;
            }

            if ((max_row - row) <= 1 && (max_col - col) <= 1) { System.out.printf("answer in %d steps\n", steps); should_continue = false; }
        }

        return String.format("%d", 10000*max_col+max_row);
    }
}