package com.schreibersolutions;

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

    public String processPhase(String input) {

        String output = "";

        char[] sequence = input.toCharArray();

        int index = 0;
        for (char c : sequence) {
            ++index;
            int[] pattern = getPattern(index, input.length());

            int sub_index = 0;
            int total = 0;
            for (char sub_c : sequence) {
                total += (sub_c - '0') * pattern[sub_index];
                if (LOG) System.out.printf("%d*%d ", (sub_c - '0'), pattern[sub_index]);
                ++sub_index;
            }
            if (LOG) System.out.printf(" = %d \n", total);
            total = Math.abs(total);
            total = total % 10;

            output += String.valueOf(total);
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
