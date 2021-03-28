package VaccinationCenter.GUI;

import Simulation.Interfaces.IObserver;
import VaccinationCenter.VaccCenterSimCore;

import java.util.HashMap;

public class Controller implements IObserver {
    private VaccCenterSimCore m_simCore;

    public Controller() {
        m_simCore =new VaccCenterSimCore(10000,2,5,6,3,9*60*60);
        m_simCore.subscribeObserver(this);

    }

    public void subscribeToSimCore(IObserver observer) {
        m_simCore.subscribeObserver(observer);
    }

    public void run() {
        new Thread(() -> {
            m_simCore.simulate();
        }).run();
    }

    @Override
    public void update(Object o) {
        //System.out.println("notification");
        if(o == null) return;

        HashMap<String, Double> m_statistics = (HashMap<String, Double>)o;
        System.out.println(m_statistics.get("CompleteReplications"));
    }
}
