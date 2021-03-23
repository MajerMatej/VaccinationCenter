package VaccinationCenter;

import Simulation.ExpRandomGenerator;
import Simulation.TriaRandomGenerator;

public class Main {

    public static void main(String[] args) {
        ExpRandomGenerator gen = new ExpRandomGenerator(5.0);
        gen.writeToFile("expSamples.txt", 100000);

        TriaRandomGenerator genT = new TriaRandomGenerator(20.0, 100.0, 75.0);
        genT.writeToFile("triaSamples.txt", 100000);

	// write your code here
    }
}
