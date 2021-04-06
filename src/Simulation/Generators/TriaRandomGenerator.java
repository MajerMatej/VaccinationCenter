package Simulation.Generators;

public class TriaRandomGenerator extends CustomRandomGenerator {
    private double m_minValue;
    private double m_maxValue;
    private double m_meanValue;

    public TriaRandomGenerator(double min, double max, double mean) {
        this.m_minValue = min;
        this.m_maxValue = max;
        this.m_meanValue = mean;
    }

    public TriaRandomGenerator(long seed, double min, double max, double mean) {
        super(seed);
        this.m_minValue = min;
        this.m_maxValue = max;
        this.m_meanValue = mean;
    }

    @Override
    public double nextDouble() {
        double func = (m_meanValue - m_minValue)/(m_maxValue - m_minValue);
        double sample = super.nextDouble();
        if(sample < func) {
            return (m_minValue + Math.sqrt(sample * (m_maxValue - m_minValue) * (m_meanValue - m_minValue)));
        }
        return (m_maxValue - Math.sqrt((1 - sample) * (m_maxValue - m_minValue) * (m_maxValue - m_meanValue)));
    }
}
