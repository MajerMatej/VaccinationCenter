package VaccinationCenter;

import Simulation.ExpRandomGenerator;
import VaccinationCenter.GUI.Controller;
import VaccinationCenter.GUI.MainWindow;

public class Main {

    public static void main(String[] args) {
       /*VaccCenterSimCore simCore = new VaccCenterSimCore(10000,2,5,6,3, 9 * 60 * 60);
       double time = System.currentTimeMillis() / 1000.0;
       simCore.simulate();
        System.out.println("Cas :"  + ((System.currentTimeMillis() / 1000.0) - time));*/

        //ExpRandomGenerator gen = new ExpRandomGenerator(260);
       // gen.writeToFile("ExpRandomSamples.txt", 10000);

        MainWindow mainWindow = new MainWindow(new Controller());
    }
}