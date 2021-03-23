package Simulation;

public abstract class SimulationCore {
    protected int m_numOfReplications;

    public SimulationCore(int numberOfReplications) {
        this.m_numOfReplications = numberOfReplications;
    }

    protected abstract void onSimulationStart();
    protected abstract void onSimulationEnd();
    protected abstract void onReplicationStart();
    protected abstract void onReplicationEnd();

    public void simulate() {
        onSimulationStart();
        for (int i = 0; i < m_numOfReplications; i++) {
            onReplicationStart();
            onReplicationEnd();
        }
        onSimulationEnd();
    }
}
