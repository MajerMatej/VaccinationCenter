package Simulation;

import Simulation.Interfaces.IObserver;
import Simulation.Interfaces.ISubject;

import java.util.LinkedList;
import java.util.List;

public abstract class SimulationCore implements ISubject {
    protected int m_numOfReplications;
    protected List<IObserver> m_observers;

    public SimulationCore(int numberOfReplications) {
        this.m_numOfReplications = numberOfReplications;
        m_observers = new LinkedList<>();
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

    @Override
    public void subscribeObserver(IObserver observer) {
        m_observers.add(observer);
    }

    @Override
    public void unsubscribeObserver(IObserver observer) {
        m_observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (IObserver observer : m_observers) {
            observer.update(null);
        }
    }
}
