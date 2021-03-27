package VaccinationCenter;

import Simulation.ExpRandomGenerator;
import Simulation.TriaRandomGenerator;

public class Main {

    public static void main(String[] args) {
       VaccCenterSimCore simCore = new VaccCenterSimCore(1,1,2,3,2, 3020400);
       double time = System.currentTimeMillis() / 1000.0;
       simCore.simulate();
        System.out.println("Cas :"  + ((System.currentTimeMillis() / 1000.0) - time));
    }
}
