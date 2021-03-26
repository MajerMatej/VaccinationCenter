package VaccinationCenter;

import Simulation.ExpRandomGenerator;
import Simulation.TriaRandomGenerator;

public class Main {

    public static void main(String[] args) {
       VaccCenterSimCore simCore = new VaccCenterSimCore(1,1,1,1,3, 200000000);
       simCore.simulate();
    }
}
