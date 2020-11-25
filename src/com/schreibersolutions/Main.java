package com.schreibersolutions;

import com.sun.tools.internal.jxc.ap.Const;
import sun.applet.AppletListener;

public class Main {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

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
       // h.displaySurface();
    }

    public void dec12() {
        Space space = new Space();
        int[][] positions =  Constants.dec12_puzzleinput;
        Moon moons[] = space.generateMoons(4, positions);


//        space.timeLapse(100);
//        System.out.println(space.totalEnergy());
        space.findRepeat();

    }
    public void dec13() {
        ArcadeCabinet c = new ArcadeCabinet(Constants.dec13_puzzleinput);

        c.play();
        System.out.println(c.countBlockTiles());

    }

    public void dec14() {

        Reactions r1 = new Reactions(Constants.dec14_test1);
        long o1 = r1.computeOREforFUEL();
        boolean testPassed1 = (o1 == 165);

        Reactions r2 = new Reactions(Constants.dec14_test2);
        long o2 = r2.computeOREforFUEL();
        boolean testPassed2 = (o2 == 13312);

        Reactions r3 = new Reactions(Constants.dec14_test3);
        long o3 = r3.computeOREforFUEL();
        boolean testPassed3 = (o3 == 180697);
        r3.solver();

        System.out.print("***TEST4\n");
        Reactions r4 = new Reactions(Constants.dec14_test4);
        long o4 = r4.computeOREforFUEL();
        boolean testPassed4 = (o4 == 2210736);
        r4.solver();

        Reactions r5 = new Reactions(Constants.dec14_puzzleinput);
        long o5 = r5.computeOREforFUEL();
        boolean testPassed5 = (o5 == 1920219);
        r5.solver();

        System.out.printf((testPassed1 ? ANSI_RESET : ANSI_RED) + "Test1: %b\n", testPassed1);
        System.out.printf((testPassed2 ? ANSI_RESET : ANSI_RED) + "Test2: %b\n", testPassed2);
        System.out.printf((testPassed3 ? ANSI_RESET : ANSI_RED) + "Test3: %b\n", testPassed3);
        System.out.printf((testPassed4 ? ANSI_RESET : ANSI_RED) + "Test4: %s\n", testPassed4);
        System.out.printf((testPassed5 ? ANSI_RESET : ANSI_RED) + "Test5: %s\n", testPassed5);

    }

    public void dec15() {
        OxygenRepairRobot robot = new OxygenRepairRobot(Constants.dec15_puzzleinput);
        robot.findOxygenSystem();

        Oxygenator o = new Oxygenator(robot.mySurface);
        while (o.incomplete()) {
            o.oxygenate();
        }

        System.out.printf("\nOxygenated in %d minutes\n", o.minutes);

    }
    public static void main(String[] args) {
        Main m = new Main();
        m.dec15();

    }
}
