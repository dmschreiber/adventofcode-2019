package com.schreibersolutions;

/**
 * Created by dschreiber on 12/8/19.
 */
public class Amplifier {

    private int[] myProgram;
    private IntcodeComputer[] computer = new IntcodeComputer[5];
    public int[] phaseSetting = new int[5];
    public int ampOutput = -1;
    public boolean isFeedback = false;

    Amplifier(int[] program) {
        myProgram = program;
        for (int index = 0; index < computer.length; index++) {
            computer[index] = new IntcodeComputer();
            computer[index].program = myProgram.clone();
            computer[index].isInteractive = false;


        }
    }

    private void runFeedback() {
        int output = 0;
        boolean firstCycle = true;

        while (firstCycle || computer[0].isRunning || computer[1].isRunning || computer[2].isRunning || computer[3].isRunning || computer[4].isRunning) {
            for (int index = 0; (index < computer.length); index++) {
                computer[index].inputs.push(output);
                if (firstCycle) {
                    computer[index].inputs.push(phaseSetting[index]);
                    computer[index].run();
                } else {
                    computer[index].resume();
                }

                output = computer[index].outputs.pop();
                if (index == 4) {
                    ampOutput = output;
                }
            }
            firstCycle = false;
        }

    }

    private void runNoFeedback() {
        int output = 0;

        for (int index = 0; index < computer.length; index++) {
            computer[index].inputs.push(output);
            computer[index].inputs.push(phaseSetting[index]);
            computer[index].run();
            output = computer[index].outputs.pop();
        }
        ampOutput = output;
    }

    public void run() {

        if (isFeedback) {
            runFeedback();
        } else {
            runNoFeedback();
        }
    }
}
