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
        SpaceImage image = new SpaceImage(Constants.dec8_puzzleinput, 25,6);
        int fewestZeros = image.whichLayerHasFewestZeros();
        System.out.println(image.quotientCount(fewestZeros,1,2));
        image.displayImage();
    }

    public void dec9() {
        IntcodeComputer computer;

        computer = new IntcodeComputer();
        computer.program = Constants.dec9_puzzleinput;
        computer.isInteractive = false;
        computer.inputs.push(2L);
        computer.run();
        for (long o : computer.outputs) {
            System.out.println(o);
        }

    }

    public void dec10() {
        AsteroidMap m;
//        m = new AsteroidMap(Constants.dec10_test1);
//        m.solver();
//        m = new AsteroidMap(Constants.dec10_test2);
//        m.solver();
//        m = new AsteroidMap(Constants.dec10_test3);
//        m.solver();
//        m = new AsteroidMap(Constants.dec10_test4);
//        m.solver();
//        m = new AsteroidMap(Constants.dec10_test5);
//        m.solver();
        m = new AsteroidMap(Constants.dec10_puzzleinput);
        m.bestLocationSolver();
        m.laserSweep();

    }

    public void dec11() {
        Surface h = new Surface(0); // default all black

        HullPaintingRobot robot = new HullPaintingRobot(Constants.dec11_puzzleinput);
        h.setColor(0,0,1);
        robot.paint(h);
        System.out.println(h.paintedPanelCount);
        h.displaySurface();
    }
    public static void main(String[] args) {
        Main m = new Main();
        m.dec11();

    }
}
