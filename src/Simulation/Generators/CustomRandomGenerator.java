package Simulation.Generators;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public abstract class CustomRandomGenerator extends Random {


    public CustomRandomGenerator() {
    }

    public CustomRandomGenerator(long seed) {
        super(seed);
    }

    public void writeToFile(String fileName, int numOfSamples) {
        try {
            FileWriter writer = new FileWriter(fileName);
            for(int i = 0; i < numOfSamples; i++) {
                writer.write(String.valueOf(this.nextDouble()) + '\n');
            }
            writer.close();
            System.out.println("Succesfully writen to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
