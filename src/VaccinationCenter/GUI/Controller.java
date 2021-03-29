package VaccinationCenter.GUI;

import Simulation.Interfaces.IObserver;
import VaccinationCenter.VaccCenterSimCore;

import java.util.HashMap;

public class Controller implements IObserver {
    private VaccCenterSimCore m_simCore;

    public Controller() {

    }

    public void init(int numberOfReplications, int seed,
                     int numOfAdminWorkers, int numOfDoctors, int numOfNurses, double repTime) {
        m_simCore =new VaccCenterSimCore(numberOfReplications,seed,numOfAdminWorkers,numOfDoctors,numOfNurses,repTime);
        m_simCore.subscribeObserver(this);
    }

    public void subscribeToSimCore(IObserver observer) {
        m_simCore.subscribeObserver(observer);
    }

    public void run() {
        new Thread(() -> {
            m_simCore.simulate();
        }).start();
    }

    @Override
    public void update(Object o) {
        //System.out.println("notification");
        if(o == null) return;

    }

    public void pauseSim() {
        m_simCore.pause();
    }

    public void resumeSim() {
        m_simCore.resume();
    }

    public void stopSim() {
        m_simCore.stop();
    }
}
