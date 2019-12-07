package com.schreibersolutions;
import java.util.Scanner;

/**
 * Created by dschreiber on 12/7/19.
 */
public class IntcodeComputer {
    public int result = 0;
    public int offset = 0;
    int offsetIndex = 0;

    public int[] program = {};

    public void init() {
        result = 0;
        offset = 0;
    }

    public void performOpCode() {
        offset = 0;
        int opCode = program[offsetIndex] % 100;

        int arg1, arg2, arg3;
        int arg1_type, arg2_type;

        arg1_type = ((program[offsetIndex]/100) % 10);
        arg2_type = ((program[offsetIndex]/1000) % 10);

        if (opCode == 99) {
            // exit
            offset = -1;
        } else {

            if ((opCode == 1) || (opCode == 2)) {
                // add & multiply
                offset = 4;
                arg1 = (arg1_type == 0) ? program[program[offsetIndex + 1]] : program[offsetIndex + 1];
                arg2 = (arg2_type == 0) ? program[program[offsetIndex + 2]] : program[offsetIndex + 2];
                arg3 = program[offsetIndex + 3];

                if (opCode == 1) {
                    program[arg3] = arg1 + arg2;

                }

                if (opCode == 2) {
                    program[arg3] = arg1 * arg2;
                }
                offsetIndex += offset;

            } else if (opCode == 3) {
                // input
                arg1 = program[offsetIndex + 1];
                Scanner scanner = new Scanner(System.in);

                String input = scanner.nextLine();
                int number = Integer.parseInt(input);
                program[arg1] = number;

                offset = 2;
                offsetIndex += offset;

            } else if (opCode == 4) {
                // output
                arg1 = (arg1_type == 0) ? program[program[offsetIndex + 1]] : program[offsetIndex + 1];
                System.out.println(arg1);
                offset = 2;
                offsetIndex += offset;

            } else if ((opCode == 5) || (opCode == 6)) {
                // jump to true & false
                offset = 3;
                arg1 = (arg1_type == 0) ? program[program[offsetIndex + 1]] : program[offsetIndex + 1];
                arg2 = (arg2_type == 0) ? program[program[offsetIndex + 2]] : program[offsetIndex + 2];

                if (opCode == 5) {
                    if (arg1 != 0) {
                        offsetIndex = arg2;
                    } else {
                        offsetIndex += offset;
                    }
                } else if (opCode == 6) {
                    if (arg1 == 0) {
                        offsetIndex = arg2;
                    } else {
                        offsetIndex += offset;
                    }
                }
            } else if ((opCode == 7) || (opCode == 8)) {
                // less than & equals
                offset = 4;
                arg1 = (arg1_type == 0) ? program[program[offsetIndex + 1]] : program[offsetIndex + 1];
                arg2 = (arg2_type == 0) ? program[program[offsetIndex + 2]] : program[offsetIndex + 2];
                arg3 = program[offsetIndex + 3];

                if (opCode == 7) {
                    if (arg1 < arg2) {
                        program[arg3] = 1;
                    } else {
                        program[arg3] = 0;
                    }
                } else if (opCode == 8) {
                    if (arg1 == arg2) {
                        program[arg3] = 1;
                    } else {
                        program[arg3] = 0;
                    }
                }
                offsetIndex += offset;
            }
        }
    }

    public void run() {
        offsetIndex = 0;

        while (offset >= 0) {
            performOpCode();
        }

        result = program[0];
    }
}
