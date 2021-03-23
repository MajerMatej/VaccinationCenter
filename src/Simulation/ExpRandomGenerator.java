package Simulation;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class ExpRandomGenerator extends CustomRandomGenerator {
    private Double m_mean;

    public ExpRandomGenerator(Double m_mean) {
        this.m_mean = m_mean;
    }

    public ExpRandomGenerator(long seed, Double m_mean) {
        super(seed);
        this.m_mean = m_mean;
    }

    @Override
    public double nextDouble() {
        return ((- Math.log(super.nextDouble())) * m_mean);
    }
}
