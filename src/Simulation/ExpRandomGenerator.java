package Simulation;

public class ExpRandomGenerator extends CustomRandomGenerator {
    private double m_mean;

    public ExpRandomGenerator(double m_mean) {
        this.m_mean = m_mean;
    }

    public ExpRandomGenerator(long seed, double m_mean) {
        super(seed);
        this.m_mean = m_mean;
    }

    @Override
    public double nextDouble() {
        return ((- Math.log((1 - super.nextDouble()))) * m_mean);
    }
}
