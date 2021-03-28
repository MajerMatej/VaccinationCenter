package Simulation.Interfaces;

public interface ISubject {
    public void subscribeObserver(IObserver observer);
    public void unsubscribeObserver(IObserver observer);
    public void notifyObservers();
}
