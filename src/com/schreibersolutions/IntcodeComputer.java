package com.schreibersolutions;
import java.util.Scanner;
import java.util.Stack;

/**
 * Created by dschreiber on 12/7/19.
 */
public class IntcodeComputer {
    public long result = 0;
    public int offset = 0;
    public boolean isInteractive = true;
    public long[] program = {};
    public boolean isRunning = false;
    public boolean isAwaitingInput = false;

    public Stack<Long> inputs = new Stack<>();
    public Stack<Long> outputs = new Stack<>();

    private int offsetIndex = 0;
    private int relativeBase = 0;

    public void init() {
        result = 0;
        offset = 0;
    }

    private void checkMemoryBounds(int pointer) {
        if (pointer >= program.length) {
            long[] new_program = new long[pointer+1];
            System.arraycopy(program,0,new_program,0,program.length);
            for (int index = program.length; index < new_program.length; index++) {
                new_program[index] = 0;
            }
            program = new_program;
        }
    }

    private long getArgument(long arg_type, long value) {
        long retval = -1;

        if (arg_type == 0) {
            checkMemoryBounds((int) value);
            retval = program[(int) value];
        } else if (arg_type == 1) {
            retval = value;
        } else if (arg_type == 2) {
            checkMemoryBounds((int) value + relativeBase);
            retval = program[(int) value + relativeBase];
        }

        return retval;
    }

    public void performOpCode() {
        offset = 0;
        long opCode = program[offsetIndex] % 100;

        long arg1, arg2, arg3;
        long arg1_type, arg2_type, arg3_type;

        arg1_type = ((program[offsetIndex]/100) % 10);
        arg2_type = ((program[offsetIndex]/1000) % 10);
        arg3_type = ((program[offsetIndex]/10000) % 10);

        isAwaitingInput = false;
        if ((opCode == 3) && (inputs.size() == 0) && !(isInteractive)) { // suspend until we get input and resume
            isAwaitingInput = true;
            offset = -2;
        } else if ((outputs.size() >= 3)) {
            offset = -2;
        } else if (opCode == 99) {
            // exit
            offset = -1;
            isRunning = false;
        } else {

            if ((opCode == 1) || (opCode == 2)) {
                // add & multiply
                offset = 4;
                arg1 = getArgument(arg1_type, program[offsetIndex+1]);
                arg2 = getArgument(arg2_type, program[offsetIndex+2]);
                arg3 = (arg3_type == 0) ? program[offsetIndex + 3] : program[offsetIndex + 3] + relativeBase;

                checkMemoryBounds((int) arg3);
                if (opCode == 1) {
                    program[(int) arg3] = arg1 + arg2;

                }

                if (opCode == 2) {
                    program[(int) arg3] = arg1 * arg2;
                }

            } else if (opCode == 3) {
                // input
                arg1 = (arg1_type == 0) ? program[offsetIndex + 1] : program[offsetIndex + 1] + relativeBase;
                long number;

                if (isInteractive) {
                    Scanner scanner = new Scanner(System.in);

                    String input = scanner.nextLine();
                    number = Integer.parseInt(input);
                } else {
                    number = inputs.pop();
                }
                checkMemoryBounds((int) (int) arg1);
                program[(int) arg1] = number;

                offset = 2;

            } else if (opCode == 4) {
                // output
                arg1 = getArgument(arg1_type, program[offsetIndex + 1]);

                if (isInteractive) {
                    System.out.println(arg1);
                } else {
                    outputs.push(arg1);
                }
                offset = 2;

            } else if ((opCode == 5) || (opCode == 6)) {
                // jump to true & false
                offset = 3;
                arg1 = getArgument(arg1_type, program[offsetIndex+1]);
                arg2 = getArgument(arg2_type, program[offsetIndex+2]);

                if (opCode == 5) {
                    if (arg1 != 0) {
                        offsetIndex = (int) arg2;
                        offset = 0;
                    }
                } else if (opCode == 6) {
                    if (arg1 == 0) {
                        offsetIndex = (int) arg2;
                        offset = 0;
                    }
                }
            } else if ((opCode == 7) || (opCode == 8)) {
                // less than & equals
                offset = 4;
                arg1 = getArgument(arg1_type, program[offsetIndex+1]);
                arg2 = getArgument(arg2_type, program[offsetIndex+2]);
                arg3 = (arg3_type == 0) ? program[offsetIndex + 3] : program[offsetIndex + 3] + relativeBase;

                checkMemoryBounds((int) arg3);
                if (opCode == 7) {
                    if (arg1 < arg2) {
                        program[(int) arg3] = 1;
                    } else {
                        program[(int) arg3] = 0;
                    }
                } else if (opCode == 8) {
                    if (arg1 == arg2) {
                        program[(int) arg3] = 1;
                    } else {
                        program[(int) arg3] = 0;
                    }
                }
            } else if (opCode == 9) { // adjust relative base
                offset = 2;
                arg1 = getArgument(arg1_type, program[offsetIndex+1]);
                relativeBase += arg1;
            }
            offsetIndex += offset;

        }
    }

    public void resume() {

        if (isRunning) {
            offset = 0;
            while (offset >= 0) {
                performOpCode();
            }
        }
    }

    public void run() {

        offsetIndex = 0;
        isRunning = true;

        resume();

        result = program[0];
    }
}
