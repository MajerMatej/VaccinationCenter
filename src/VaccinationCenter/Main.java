package VaccinationCenter;

import VaccinationCenter.GUI.Controller;
import VaccinationCenter.GUI.MainWindow;

public class Main {

    public static void main(String[] args) {

        /*ExpRandomGenerator gen = new ExpRandomGenerator(260);
        gen.writeToFile("ExpRandomSamples.txt", 1000000);

        TriaRandomGenerator genTria = new TriaRandomGenerator(20,100,75);
        genTria.writeToFile("TriaRandomSamples.txt", 1000000);

        NormalRandomGenerator genN = new NormalRandomGenerator(5,25);
        genN.writeToFile("NormalRandomGenerator.txt", 1000000);*/

        MainWindow mainWindow = new MainWindow(new Controller());
    }
}