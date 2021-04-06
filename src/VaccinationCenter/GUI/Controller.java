package VaccinationCenter.GUI;

import Simulation.Interfaces.IObserver;
import VaccinationCenter.VaccCenterSimCore;

import java.util.HashMap;
import java.util.LinkedList;

public class Controller implements IObserver {
    private VaccCenterSimCore m_simCore;

    private IObserver m_observer = null;
    private boolean exp3Running = false;
    private boolean exp2Running = false;

    public Controller() {

    }

    public void init(int numberOfReplications, int seed,
                     int numOfAdminWorkers, int numOfDoctors, int numOfNurses, double repTime, int speed, int maxCustomers) {
        m_simCore =new VaccCenterSimCore(numberOfReplications,seed,numOfAdminWorkers,numOfDoctors,numOfNurses,repTime, maxCustomers);
        m_simCore.subscribeObserver(this);
        m_simCore.setSpeed(speed);
    }

    public void subscribeToSimCore(IObserver observer) {
        m_observer = observer;

    }

    public void run() {
        m_simCore.subscribeObserver(m_observer);
        new Thread(() -> {
            m_simCore.simulate();
        }).start();
    }

    @Override
    public void update(Object o) {
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

    public void setTurbo(boolean turbo) {
        m_simCore.setTurbo(turbo);
    }

    public LinkedList<String> experiment2(int doctors, int reps, int customers, int workers, int nurses, int repTime) {
        exp2Running = true;

        VaccCenterSimCore simCore;
        simCore = new VaccCenterSimCore(reps,-1,workers,doctors,nurses,repTime, 540);
        simCore.setTurbo(true);

        simCore.simulate();
        double initWoUtil = simCore.getWorkersUtilization();
        double initDocUtil = simCore.getDoctorsUtilization();
        double initNurUtil = simCore.getNursesUtilization();

        System.out.println("" + initWoUtil);

        double newWoUtil = 100.0;
        int tmpW = workers;
        do {
            tmpW++;
            simCore.init(-1, tmpW, doctors, nurses, customers);
            simCore.simulate();
            newWoUtil = simCore.getWorkersUtilization();
            System.out.println("" + tmpW + ": " + newWoUtil);
        }while(newWoUtil > initWoUtil);
        //System.out.println("" + tmpW + ": " + newWoUtil);

        double newDocUtil = 100.0;
        int tmpD = doctors;
        do {
            tmpD++;
            simCore.init(-1, tmpW, tmpD, nurses, customers);
            simCore.simulate();
            newDocUtil = simCore.getDoctorsUtilization();
            System.out.println("" + tmpD + ": " + newDocUtil);
        }while(newDocUtil > initDocUtil);

        double newNurUtil = 100.0;
        int tmpN = nurses;
        do {
            tmpN++;
            simCore.init(-1, tmpW, tmpD, tmpN, customers);
            simCore.simulate();
            newNurUtil = simCore.getNursesUtilization();
            System.out.println("" + tmpN + ": " + newNurUtil);
        }while(newNurUtil > initNurUtil);


        exp2Running = false;

        LinkedList<String> result = new LinkedList<>();
        result.add(String.format("AdminWorkers %d %.2f",tmpW, newWoUtil));
        result.add(String.format("Doctors %d %.2f",tmpD, newDocUtil));
        result.add(String.format("Nurses %d %.2f",tmpN, newNurUtil));
        return result;
    }

    public void experiment3(int minDoc, int maxDoc, int reps, int customers, int workers, int nurses, int repTime) {
        exp3Running = true;
        new Thread(() -> {
            VaccCenterSimCore simCore;
            simCore = new VaccCenterSimCore(reps,-1,workers,minDoc,nurses,repTime, customers);
            simCore.subscribeObserver(this);
            simCore.setTurbo(true);
            if(m_observer != null) {
                simCore.subscribeObserver(m_observer);
            }
            for(int docs = minDoc; docs <= maxDoc; docs++) {
                simCore.init(-1, workers, docs, nurses, customers);
                simCore.simulate();
            }
            exp3Running = false;
        }).start();
    }

    public boolean getExp3run() {
        return exp3Running;
    }

    public boolean getExp2run() {
        return exp2Running;
    }
}
