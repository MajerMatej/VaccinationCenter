package Simulation;

import java.util.Random;

public class NormalRandomGenerator extends Random {
    private double m_min;
    private double m_max;

    public NormalRandomGenerator(double m_min, double m_max) {
        this.m_min = m_min;
        this.m_max = m_max;
    }

    public NormalRandomGenerator(long seed, double m_min, double m_max) {
        super(seed);
        this.m_min = m_min;
        this.m_max = m_max;
    }


    @Override
    public double nextDouble() {
        return super.nextDouble() * (m_max - m_min) + m_min;
    }
}
