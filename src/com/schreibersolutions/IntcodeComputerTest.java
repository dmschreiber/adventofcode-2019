package com.schreibersolutions;

import com.sun.tools.internal.jxc.ap.Const;

import static org.junit.jupiter.api.Assertions.*;

class IntcodeComputerTest {
    IntcodeComputer computer;

    private String makeString(long[] data) {
        String retval  = "";
        for (int index=0;index<data.length;index++) {
            retval += String.valueOf(data[index]);
        }
        return retval;
    }

    @org.junit.jupiter.api.Test
    void run() {
        computer = new IntcodeComputer();
        computer.program = Constants.dec9_output_largenumber;
        computer.isInteractive = false;
        computer.run();
        assertEquals(1125899906842624L,computer.outputs.get(0));

        computer = new IntcodeComputer();
        computer.program = Constants.dec9_output_sixteendigits;
        computer.isInteractive = false;
        computer.run();
        assertEquals(1219070632396864L,computer.outputs.get(0));

//        computer = new IntcodeComputer();
//        computer.program = Constants.dec9_relative_testprogram;
//        computer.isInteractive = false;
//        computer.run();
//        assertEquals(makeString(Constants.dec9_relative_testprogram),makeString(a) );
    }
}