package com.schreibersolutions;

import com.sun.tools.internal.jxc.ap.Const;
import sun.applet.AppletListener;

import java.time.LocalDateTime;

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

    public void dec16() {
        String input = "12345678";
        char[] input_char;
        char[] output_char;
        FFT fft = new FFT(new int[]{0,1,0,-1});
        boolean testPassed;

        String output = fft.processPhase(input);
        testPassed = (output.compareTo("48226158") == 0);
        System.out.printf((testPassed ? ANSI_RESET : ANSI_RED) + "Test : %s\n", testPassed);

        output = fft.processPhase(output);
        testPassed = (output.compareTo("34040438")==0);
        System.out.printf((testPassed ? ANSI_RESET : ANSI_RED) + "Test : %s\n", testPassed);

        output = fft.processPhase(output);
        testPassed = (output.compareTo("03415518")==0);
        System.out.printf((testPassed ? ANSI_RESET : ANSI_RED) + "Test : %s\n", testPassed);

        output = fft.processPhase(output);
        testPassed = (output.compareTo("01029498")==0);
        System.out.printf((testPassed ? ANSI_RESET : ANSI_RED) + "Test : %s\n", testPassed);


        input = "80871224585914546619083218645595";
        output = fft.processPhase(input, 100);
        testPassed = (output.substring(0,8).compareTo("24176176") == 0);
        System.out.printf((testPassed ? ANSI_RESET : ANSI_RED) + "Test : %s\n", testPassed);
        input = "19617804207202209144916044189917";
        output = fft.processPhase(input, 100);
        testPassed = (output.substring(0,8).compareTo("73745418") == 0);
        System.out.printf((testPassed ? ANSI_RESET : ANSI_RED) + "Test : %s\n", testPassed);
        input = "69317163492948606335995924319873";
//        input = "00000000000000000000995924319873";
        input_char = input.toCharArray();
        output_char = fft.processPhase(input_char, 100);
        testPassed = (String.valueOf(output_char).substring(0,8).compareTo("52432133") == 0);
        System.out.printf((testPassed ? ANSI_RESET : ANSI_RED) + "Test : %s\n", testPassed);

        input = "03036732577212944063491565474664";
//        input = "59773590431003134109950482159532121838468306525505797662142691007448458436452137403459145576019785048254045936039878799638020917071079423147956648674703093863380284510436919245876322537671069460175238260758289779677758607156502756182541996384745654215348868695112673842866530637231316836104267038919188053623233285108493296024499405360652846822183647135517387211423427763892624558122564570237850906637522848547869679849388371816829143878671984148501319022974535527907573180852415741458991594556636064737179148159474282696777168978591036582175134257547127308402793359981996717609700381320355038224906967574434985293948149977643171410237960413164669930";
        input_char = input.toCharArray();

        int offset = Integer.valueOf(input.substring(0,7));

        input_char = fft.multiplyByTenThousand(input_char);
        // Only need to calculate starting at the offset
        fft.offset = offset;

        output_char = fft.processPhase(input_char, 100);
        testPassed = (output_char[offset] == '8');
        testPassed = (output_char[offset+1] == '4') && testPassed;
        testPassed = (output_char[offset+2] == '4') && testPassed;
        testPassed = (output_char[offset+3] == '6') && testPassed;
        testPassed = (output_char[offset+4] == '2') && testPassed;
        testPassed = (output_char[offset+5] == '0') && testPassed;
        testPassed = (output_char[offset+6] == '2') && testPassed;
        testPassed = (output_char[offset+7] == '6') && testPassed;
        System.out.printf((testPassed ? ANSI_RESET : ANSI_RED) + "Test : %s\n", testPassed);
        for (int i = 0; i < 8; i++) {
            System.out.print(String.valueOf(output_char[offset+i]));
        }

    }

    public void dec17() {

        ScaffoldCamera c = new ScaffoldCamera();
        c.init();
        long retval = c.sumAlignmentParameters();
        System.out.println(c.findTraverseDirections());
        char[] main = "A,A,B,C,B,C,B,C,C,A".toCharArray();
        char[] f_a = "R,8,L,4,R,4,R,10,R,8".toCharArray();
        char[] f_b = "L,12,L,12,R,8,R,8".toCharArray();
        char[] f_c = "R,10,R,4,R,4".toCharArray();
        System.out.println(c.traverse(main,f_a,f_b,f_c));
    }

    public void dec18() {
        CameraSurface s = new CameraSurface(0);

        try {
            s.readFromFile("/Users/dschreiber/Projects/advent-of-code/dec18.txt");
        } catch (Exception e) {
            System.err.printf("Error: %s\n", e.getMessage());
        }

        s.displayCamera();

        // vaultState.init(s); // load keys, doors and location & remove them from the surface map
        // vaultSolver.solve(s,vaultState); // for each key, find the path.setps and then explore from there.
        

    }
    public static void main(String[] args) {
        Main m = new Main();
        m.dec18();

    }
}
