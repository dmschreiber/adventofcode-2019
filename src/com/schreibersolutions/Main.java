package com.schreibersolutions;

import com.sun.tools.internal.jxc.ap.Const;
import sun.applet.AppletListener;

public class Main {

    public void dec5() {
        IntcodeComputer computer = new IntcodeComputer();
        //computer.program = new int[] {3,0,4,0,99};
        //computer.program = new int[] {1002,4,3,4,33};
        computer.program = Constants.dec5_puzzleinput;

        computer.run();

    }

    public void dec6() {
        String map = Constants.orbit_map;

        OrbitMap m = new OrbitMap(map);
        System.out.println(m.countOrbits());
        m.myOrbtalPath();
        m.santaOrbitalPath();
        System.out.println(m.findCommonPlace());

    }

    public void dec7() {

        AmplifierSolver solver = new AmplifierSolver(Constants.dec7_puzzleinput, 5, 10);

        solver.findMaxOutput();

        for (int index = 0; index < solver.maxSettings.length; index++) {
            System.out.println(solver.maxSettings[index]);
        }
        System.out.println(solver.maxOutput);

    }

    public void dec8() {
//        SpaceImage image = new SpaceImage("123456789012",3,2);
        SpaceImage image = new SpaceImage(Constants.dec8_puzzleinput, 25,6);
        System.out.println(image.quotientCount(image.whichLayerHasFewestZeros(),1,2));
    }

    public static void main(String[] args) {
        Main m = new Main();

        m.dec8();

    }
}
