package com.schreibersolutions;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public class FFT {
    private boolean LOG = false;

    private int[] base_pattern;

    public FFT(int[] pattern) {
        base_pattern = pattern;
    }

    private int[] getPattern(int which_pos, int size) {
        int[] retval = new int[size];

        for (int i = 0; i < size; i++) {
            if (which_pos == 1) {
                retval[i] = base_pattern[(i + 1) % base_pattern.length];
            }
            if (which_pos >= 2) {
                retval[i] = base_pattern[((i + 1) / which_pos) % base_pattern.length];
            }
        }
        if (LOG) {
            System.out.printf("At which_pos %d\n", which_pos);

            for (int i = 0; i < size; i++) {
                System.out.printf("%d,", retval[i]);
            }
            System.out.printf("\n");
        }

        return retval;
    }

    private int calculateRow(int index) {


    }

    public char[] processPhase (char[] sequence) {
        char[] retval = new char[sequence.length];
        int index = 0;
        long calcs = 0;
        LocalDateTime start = LocalDateTime.now();

        for (index = 0; index < sequence.length; index++) {
            //int[] pattern = getPattern(index+1, sequence.length);

            int sequence_length = sequence.length;
            int sub_index = 0;
            int total = 0;
            int master_index = 0;
            int repeat_length = (index+1)*base_pattern.length;
            // for sub_index: index+master_index*repeat_length to index*2+master_index*repeat_length; -> 1
            while (index+master_index*repeat_length < sequence_length) {
                for (sub_index = (index+master_index*repeat_length); sub_index < (index*2+master_index*repeat_length+1); sub_index++) {
                    if (sub_index >= sequence_length) break;
                    char sub_c = sequence[sub_index];
                    total += (sub_c - '0'); // * pattern[sub_index]; HARD CODED

                    ++calcs;

                }
                for (sub_index = (index+repeat_length/2+master_index*repeat_length); sub_index < (index*2+repeat_length/2+master_index*repeat_length+1); sub_index++) {
                    if (sub_index >= sequence_length) break;
                    char sub_c = sequence[sub_index];
                    total -= (sub_c - '0'); // * pattern[sub_index]; HARD CODED

                    ++calcs;

                }
                ++master_index;
            }

            if (index % 10000 == 0) System.out.printf("Finished char %d of %d at %d (%d calcs)\n", index, sequence.length, start.until(LocalDateTime.now(), ChronoUnit.SECONDS),calcs);
            if (LOG) System.out.printf(" = %d \n", total);
            total = Math.abs(total);
            total = total % 10;
            retval[index] = (char) ('0' + total);
        }
        return retval;
    }

    public String processPhase(String input) {

        String output = "";
        char[] sequence = input.toCharArray();

        output = String.valueOf(processPhase(input.toCharArray()));

        return output;
    }

    public char[] multiplyByTenThousand(char[] input) {
        char[] retval = new char[input.length*10000];
        for (int i = 0; i< 10000; i++) {
            for (int j = 0; j<input.length; j++) {
                retval[i*input.length + j] = input[j];
            }
        }
        return retval;
    }

    public String multiplyByTenThousand(String input) {
        String output;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i< 10000; i++) {
            sb.append(input);
            if (i%1000 == 0) System.out.println(LocalDateTime.now());
        }
        output = sb.toString();
        return output;
    }
    public char[] processPhase(char[] input, int phase_count) {
        char[] output = input;

        for (int i = 0; i < phase_count; i++) {
            System.out.printf("Starting phase %d\n", i);
            output = processPhase(output);
        }
        return output;

    }

    public String processPhase(String input, int phase_count) {
        String output = input;

        for (int i = 0; i < phase_count; i++) {
            output = processPhase(output);
        }
        return output;
    }
}
