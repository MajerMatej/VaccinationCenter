package VaccinationCenter;

import Simulation.EventSimulationCore;
import Simulation.ExpRandomGenerator;
import Simulation.NormalRandomGenerator;
import Simulation.TriaRandomGenerator;
import VaccinationCenter.Employee.AdminWorker;
import VaccinationCenter.Employee.Doctor;
import VaccinationCenter.Employee.Nurse;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class VaccCenterSimCore extends EventSimulationCore {
    // Generators ---------------------------------------------
    private NormalRandomGenerator m_registrationGenerator;
    private ExpRandomGenerator m_medicalGenerator;
    private TriaRandomGenerator m_vaccinationGenerator;
    private NormalRandomGenerator m_waitingTimeGenerator;
    private NormalRandomGenerator m_missedAppointmentsGenerator;
    private NormalRandomGenerator m_missedAppDecisionGenerator;
    private LinkedList<NormalRandomGenerator> m_regEmpDecGenerators;
    private LinkedList<NormalRandomGenerator> m_medEmpDecGenerators;
    private LinkedList<NormalRandomGenerator> m_vaccEmpDecGenerators;
    // Queues -------------------------------------------------
    private Queue<Customer> m_registrationQueue;
    private Queue<Customer> m_medicalQueue;
    private Queue<Customer> m_vaccinationQueue;
    // Employees ----------------------------------------------
    private LinkedList<AdminWorker> m_adminWorkers;
    private LinkedList<Doctor> m_doctors;
    private LinkedList<Nurse> m_nurses;
    // Others -------------------------------------------------
    private int m_seed;
    private int m_customerSequence;

    public VaccCenterSimCore(int numberOfReplications) {
        super(numberOfReplications);
        init(-1, 1, 1, 1);
    }

    public VaccCenterSimCore(int numberOfReplications, int seed) {
        super(numberOfReplications);
        init(seed, 1, 1, 1);
    }

    public VaccCenterSimCore(int numberOfReplications, int seed,
        int numOfAdminWorkers, int numOfDoctors, int numOfNurses) {
        super(numberOfReplications);
        init(seed, numOfAdminWorkers, numOfDoctors, numOfNurses);
    }

    private void init(int seed, int numOfAdminWorkers, int numOfDoctors, int numOfNurses) {
        m_customerSequence = 0;
        m_seed = seed;
        initGenerators(seed, numOfAdminWorkers, numOfDoctors, numOfNurses);
        initQueues();
        initEmployees(numOfAdminWorkers, numOfDoctors, numOfNurses);
    }

    private void initQueues() {
        m_registrationQueue = new LinkedList<>();
        m_medicalQueue = new LinkedList<>();
        m_vaccinationQueue = new LinkedList<>();
    }

    private void initGenerators(int seed, int numOfAdminWorkers, int numOfDoctors, int numOfNurses) {
        Random seedGenerator;
        if(m_seed > 0) {
            seedGenerator  = new Random(seed);
        }
        else {
            seedGenerator = new Random();
        }
        m_registrationGenerator = new NormalRandomGenerator(seedGenerator.nextInt(), 140.0, 220.0);
        m_medicalGenerator = new ExpRandomGenerator(seedGenerator.nextInt(),260);
        m_vaccinationGenerator = new TriaRandomGenerator(seedGenerator.nextInt(),20.0,100.0,75.0);
        m_waitingTimeGenerator = new NormalRandomGenerator(seedGenerator.nextInt(),0,100);
        m_missedAppointmentsGenerator = new NormalRandomGenerator(seedGenerator.nextInt(),5,25);
        m_missedAppDecisionGenerator = new NormalRandomGenerator(seedGenerator.nextInt(),0,100);

        for (int i = 0; i < numOfAdminWorkers - 1; i++) {
            NormalRandomGenerator gen = new NormalRandomGenerator(seedGenerator.nextInt(), 0, 100);
        }

        for (int i = 0; i < numOfDoctors - 1; i++) {
            NormalRandomGenerator gen = new NormalRandomGenerator(seedGenerator.nextInt(), 0, 100);
        }

        for (int i = 0; i < numOfNurses - 1; i++) {
            NormalRandomGenerator gen = new NormalRandomGenerator(seedGenerator.nextInt(), 0, 100);
        }
    }

    private void initEmployees(int numOfAdminWorkers, int numOfDoctors, int numOfNurses) {
        m_adminWorkers = new LinkedList<>();
        for (int i = 0; i < numOfAdminWorkers; i++) {
            AdminWorker worker = new AdminWorker();
            m_adminWorkers.add(worker);
        }

        m_doctors = new LinkedList<>();
        for (int i = 0; i < numOfDoctors; i++) {
            Doctor doctor = new Doctor();
            m_doctors.add(doctor);
        }

        m_nurses = new LinkedList<>();
        for (int i = 0; i < numOfNurses; i++) {
            Nurse nurse = new Nurse();
            m_nurses.add(nurse);
        }
    }


    @Override
    protected void onSimulationStart() {

    }

    @Override
    protected void onSimulationEnd() {

    }

    @Override
    protected void onReplicationStart() {

    }

    @Override
    protected void onReplicationEnd() {

    }

    @Override
    protected void doReplication(double endTime) {
        super.doReplication(endTime);
    }

    public int getNextCustomerID() {
        return ++m_customerSequence;
    }

    public double getNextArrivalTime() {
        return m_actSimTime + 60;
    }

    public void addCustToRegQueue(Customer customer) {
        this.m_registrationQueue.add(customer);
        if(m_registrationQueue.size() == 1) {
            //this.addEventToCalendar(new RegistrationStartEvent(m_actSimTime));
        }
    }

}
