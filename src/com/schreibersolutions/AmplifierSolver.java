package com.schreibersolutions;

/**
 * Created by dschreiber on 12/8/19.
 */
public class AmplifierSolver {
    public int[] maxSettings = new int[5];
    public int maxOutput = -1;
    private int rangeLow = 0;
    private int rangeHigh = 0;
    private int[] myProgram;

    public AmplifierSolver(int[] program, int lowPhaseRange, int highPhaseRange) {
        myProgram = program;
        rangeLow = lowPhaseRange;
        rangeHigh = highPhaseRange;
    }

    public void findMaxOutput() {

        for (int a = rangeLow; a < rangeHigh; a++) {
            for (int b = rangeLow; b < rangeHigh; b++) {
                if (a != b) {
                    for (int c = rangeLow; c < rangeHigh; c++) {
                        if ((c != b) && (c != a) ) {
                            for (int d = rangeLow; d < rangeHigh; d++) {
                                if ((d != c) && (d != b) && (d != a)) {
                                    for (int e = rangeLow; e < rangeHigh; e++) {
                                        if ((e != d) && (e != c) && (e != b) && (e != a)) {
                                            Amplifier amp = new Amplifier(myProgram);
                                            amp.isFeedback = true;
                                            amp.phaseSetting[0] = a;
                                            amp.phaseSetting[1] = b;
                                            amp.phaseSetting[2] = c;
                                            amp.phaseSetting[3] = d;
                                            amp.phaseSetting[4] = e;
                                            amp.run();
                                            if (amp.ampOutput > maxOutput) {
                                                maxOutput = amp.ampOutput;
                                                maxSettings = amp.phaseSetting.clone();
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


    }
}
