package VaccinationCenter;

import Simulation.*;
import VaccinationCenter.Employee.AdminWorker;
import VaccinationCenter.Employee.Doctor;
import VaccinationCenter.Employee.Nurse;
import Simulation.Interfaces.IObserver;
import Simulation.Interfaces.ISubject;

import java.util.*;

public class VaccCenterSimCore extends EventSimulationCore implements ISubject {
    // Generators ---------------------------------------------
    private NormalRandomGenerator m_registrationGenerator;
    //private ExpRandomGenerator m_registrationGenerator;
    private ExpRandomGenerator m_medicalGenerator;
    private TriaRandomGenerator m_vaccinationGenerator;
    private NormalRandomGenerator m_waitingTimeGenerator;
    private NormalRandomGenerator m_missedAppointmentsGenerator;
    private NormalRandomGenerator m_missedAppDecisionGenerator;

    //todo delete this
    private ExpRandomGenerator m_arrivalgen;

    private LinkedList<NormalRandomGenerator> m_regEmpDecGenerators;
    private LinkedList<NormalRandomGenerator> m_medEmpDecGenerators;
    private LinkedList<NormalRandomGenerator> m_vaccEmpDecGenerators;
    // Queues -------------------------------------------------
    private Queue<Customer> m_registrationQueue;
    private Queue<Customer> m_medicalQueue;
    private Queue<Customer> m_vaccinationQueue;

    private LinkedList<Customer> m_waitingRoom;
    // Employees ----------------------------------------------
    private LinkedList<AdminWorker> m_adminWorkers;
    private LinkedList<Doctor> m_doctors;
    private LinkedList<Nurse> m_nurses;
    // Stat variables -----------------------------------------
    private HashMap<String, Double> m_statistics;

    // Others -------------------------------------------------
    private int m_seed;
    private int m_customerSequence;
    private double m_missedAppointmentNum;
    //private int m_missedCustomers;
    private double m_repTime;

    public VaccCenterSimCore(int numberOfReplications) {
        super(numberOfReplications);
        init(-1, 1, 1, 1);
    }

    public VaccCenterSimCore(int numberOfReplications, int seed) {
        super(numberOfReplications);
        init(seed, 1, 1, 1);
    }

    public VaccCenterSimCore(int numberOfReplications, int seed,
        int numOfAdminWorkers, int numOfDoctors, int numOfNurses, double repTime) {
        super(numberOfReplications);
        m_repTime = repTime;
        init(seed, numOfAdminWorkers, numOfDoctors, numOfNurses);

    }

    private void init(int seed, int numOfAdminWorkers, int numOfDoctors, int numOfNurses) {
        m_statistics = new HashMap<>();
        m_statistics.put("SumTimeRegQueue", 0.0);
        m_statistics.put("SumTimeMedQueue", 0.0);
        m_statistics.put("SumTimeVaccQueue", 0.0);
        m_statistics.put("RegisteredCustomers", 0.0);
        m_statistics.put("ExaminedCustomers", 0.0);
        m_statistics.put("VaccinatedCustomers", 0.0);
        m_statistics.put("MissedCustomers", 0.0);
        m_statistics.put("SumTimeInSystem", 0.0);
        m_statistics.put("SumCustomersCompleted", 0.0);

        m_statistics.put("SumTimeRegQueueGlobal", 0.0);
        m_statistics.put("SumTimeMedQueueGlobal", 0.0);
        m_statistics.put("SumTimeVaccQueueGlobal", 0.0);
        m_statistics.put("RegisteredCustomersGlobal", 0.0);
        m_statistics.put("ExaminedCustomersGlobal", 0.0);
        m_statistics.put("VaccinatedCustomersGlobal", 0.0);
        m_statistics.put("MissedCustomersGlobal", 0.0);
        m_statistics.put("SumTimeInSystemGlobal", 0.0);
        m_statistics.put("SumCustomersCompletedGlobal", 0.0);
        m_statistics.put("CompleteReplications", 0.0);

        m_customerSequence = 0;
        m_seed = seed;

        m_missedAppointmentNum = 0;

        initGenerators(seed, numOfAdminWorkers, numOfDoctors, numOfNurses);
        initQueues();
        initEmployees(numOfAdminWorkers, numOfDoctors, numOfNurses);
    }

    private void initQueues() {
        m_registrationQueue = new LinkedList<>();
        m_medicalQueue = new LinkedList<>();
        m_vaccinationQueue = new LinkedList<>();
        m_waitingRoom = new LinkedList<>();
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
        //m_registrationGenerator = new ExpRandomGenerator(4 * 60);
        m_medicalGenerator = new ExpRandomGenerator(seedGenerator.nextInt(),260);
        m_vaccinationGenerator = new TriaRandomGenerator(seedGenerator.nextInt(),20.0,100.0,75.0);
        m_waitingTimeGenerator = new NormalRandomGenerator(seedGenerator.nextInt(),0,1);
        m_missedAppointmentsGenerator = new NormalRandomGenerator(seedGenerator.nextInt(),5,25);
        m_missedAppDecisionGenerator = new NormalRandomGenerator(seedGenerator.nextInt(),0,1);
        m_regEmpDecGenerators = new LinkedList<>();
        m_medEmpDecGenerators = new LinkedList<>();
        m_vaccEmpDecGenerators = new LinkedList<>();

        for (int i = 0; i < numOfAdminWorkers - 1; i++) {
            NormalRandomGenerator gen = new NormalRandomGenerator(seedGenerator.nextInt(), 0, 1);
            m_regEmpDecGenerators.add(gen);
        }

        for (int i = 0; i < numOfDoctors - 1; i++) {
            NormalRandomGenerator gen = new NormalRandomGenerator(seedGenerator.nextInt(), 0, 1);
            m_medEmpDecGenerators.add(gen);
        }

        for (int i = 0; i < numOfNurses - 1; i++) {
            NormalRandomGenerator gen = new NormalRandomGenerator(seedGenerator.nextInt(), 0, 1);
            m_vaccEmpDecGenerators.add(gen);
        }

        //todo delete this
        m_arrivalgen = new ExpRandomGenerator(seedGenerator.nextInt(),1.5 *60);
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

    private void reset() {
        m_statistics.replace("SumTimeRegQueue", 0.0);
        m_statistics.replace("SumTimeMedQueue", 0.0);
        m_statistics.replace("SumTimeVaccQueue", 0.0);
        m_statistics.replace("RegisteredCustomers", 0.0);
        m_statistics.replace("ExaminedCustomers", 0.0);
        m_statistics.replace("VaccinatedCustomers", 0.0);
        m_statistics.replace("MissedCustomers", 0.0);
        m_statistics.replace("SumTimeInSystem", 0.0);
        m_statistics.replace("SumCustomersCompleted", 0.0);

        m_customerSequence = 0;

        m_missedAppointmentNum = 0;

        m_registrationQueue.clear();
        m_medicalQueue.clear();
        m_vaccinationQueue.clear();
        m_waitingRoom.clear();

        for (AdminWorker worker : m_adminWorkers) {
            worker.setAvailable();
        }

        for (Doctor doctor : m_doctors) {
            doctor.setAvailable();
        }

        for (Nurse nurse : m_nurses) {
            nurse.setAvailable();
        }

        m_actSimTime = 0.0;
    }

    @Override
    protected void onSimulationStart() {

    }

    @Override
    protected void onSimulationEnd() {
        /*System.out.println("Average time in registration Queue: " +
                m_statistics.get("SumTimeRegQueueGlobal") /
                        m_statistics.get("CompleteReplications"));
        System.out.println("Average ppl in registration Queue: " +
                m_statistics.get("RegisteredCustomersGlobal") /
                        m_statistics.get("CompleteReplications"));
        System.out.println("Average time in medical examination Queue: " +
                m_statistics.get("SumTimeMedQueueGlobal") /
                        m_statistics.get("CompleteReplications"));
        System.out.println("Average ppl in medical examination Queue: " +
                m_statistics.get("ExaminedCustomersGlobal") /
                        m_statistics.get("CompleteReplications"));
        System.out.println("Average time in vaccination Queue: " +
                m_statistics.get("SumTimeVaccQueueGlobal") /
                        m_statistics.get("CompleteReplications"));
        System.out.println("Average ppl in vaccination Queue: " +
                m_statistics.get("VaccinatedCustomersGlobal") /
                        m_statistics.get("CompleteReplications"));
        System.out.println("Average time in system: " +
                m_statistics.get("SumTimeInSystemGlobal") /
                        m_statistics.get("CompleteReplications"));
        System.out.println("Average customers completed: " +
                m_statistics.get("SumCustomersCompletedGlobal") /
                        m_statistics.get("CompleteReplications"));*/

    }

    @Override
    protected void onReplicationStart() {
        Event firstArrival = new CustomerArrivalEvent(0, this);
        m_eventCalendar.add(firstArrival);
        m_missedAppointmentNum = m_missedAppointmentsGenerator.nextDouble();

        doReplication(m_repTime);
    }

    @Override
    protected void onReplicationEnd() {
        m_statistics.replace("SumTimeRegQueueGlobal",
                m_statistics.get("SumTimeRegQueueGlobal") +
                        m_statistics.get("SumTimeRegQueue") / m_statistics.get("RegisteredCustomers"));
        m_statistics.replace("SumTimeMedQueueGlobal",
                m_statistics.get("SumTimeMedQueueGlobal") +
                        m_statistics.get("SumTimeMedQueue") / m_statistics.get("ExaminedCustomers"));
        m_statistics.replace("SumTimeVaccQueueGlobal",
                m_statistics.get("SumTimeVaccQueueGlobal") +
                        m_statistics.get("SumTimeVaccQueue") / m_statistics.get("VaccinatedCustomers"));
        m_statistics.replace("RegisteredCustomersGlobal",
                m_statistics.get("RegisteredCustomersGlobal") +
                        m_statistics.get("SumTimeRegQueue") / m_actSimTime);
        m_statistics.replace("ExaminedCustomersGlobal",
                m_statistics.get("ExaminedCustomersGlobal") +
                        m_statistics.get("SumTimeMedQueue") / m_actSimTime);
        m_statistics.replace("VaccinatedCustomersGlobal",
                m_statistics.get("VaccinatedCustomersGlobal") +
                        m_statistics.get("SumTimeVaccQueue") / m_actSimTime);
        m_statistics.replace("SumTimeInSystemGlobal",
                m_statistics.get("SumTimeInSystemGlobal") +
                        m_statistics.get("SumTimeInSystem") / m_statistics.get("SumCustomersCompleted"));
        m_statistics.replace("SumCustomersCompletedGlobal",
                m_statistics.get("SumCustomersCompletedGlobal") +
                        m_statistics.get("SumCustomersCompleted"));

        reset();

        m_statistics.replace("CompleteReplications",
                m_statistics.get("CompleteReplications").doubleValue() + 1);

        this.m_eventCalendar.clear();
    }

    @Override
    protected void doReplication(double endTime) {
        super.doReplication(endTime);
    }

    public int getNextCustomerID() {
        return ++m_customerSequence;
    }

    public boolean customerArrived() {
        if(m_missedAppDecisionGenerator.nextDouble() < m_missedAppointmentNum / (m_repTime / 60)) {
            m_statistics.replace("MissedCustomers",
                    m_statistics.get("MissedCustomers") + 1);
            return false;
        }
        return true;
    }

    public double getNextArrivalTime() {
        double time = m_actSimTime;

        /*while(!customerArrived()) {
            time += 60;
        }*/
        return time + m_arrivalgen.nextDouble();
    }
//----------------------------------------------------------------------------------------
//  ************************** Registration sequence *************************************
    public void addCustToRegQueue(Customer customer) {
        this.m_registrationQueue.add(customer);
        registerNextCustomer();
    }

    public void registerNextCustomer() {
        if(m_registrationQueue.isEmpty()) {
            return;
        }
        LinkedList<AdminWorker> availableWorkers = getAvailableAdminWorkers();
        if(availableWorkers.isEmpty()) {
            return;
        }

        if(availableWorkers.size() == 1) {
            this.addEventToCalendar(new RegistrationStartEvent(m_actSimTime, this, m_registrationQueue.poll(), availableWorkers.poll()));
            return;
        }

        /*-1 for index from0, -1 no need for generator for 1 worker */
        double randValue = m_regEmpDecGenerators.get(availableWorkers.size() - 2 ).nextDouble();
        for(int i = 0; i < availableWorkers.size(); i++) {
            if(randValue < (i + 1) / availableWorkers.size()) {
                this.addEventToCalendar(new RegistrationStartEvent(m_actSimTime, this, m_registrationQueue.poll(), availableWorkers.remove(i)));
                return;
            }
        }
    }

    public LinkedList<AdminWorker> getAvailableAdminWorkers() {
        LinkedList<AdminWorker> result = new LinkedList<>();
        for(AdminWorker worker : m_adminWorkers) {
            if (worker.isAvailable())
            {
                result.add(worker);
            }
        }
        return result;
    }

    public double getRegDuration() {
        return m_registrationGenerator.nextDouble();
    }

    public void endCustomerRegistration(Customer customer) {
        m_statistics.replace("SumTimeRegQueue",
                m_statistics.get("SumTimeRegQueue") +
                        (customer.getTimeRegStart() - customer.getTimeOfArrival()));
        m_statistics.replace("RegisteredCustomers",
                m_statistics.get("RegisteredCustomers") + 1);
        //go to next queue
        addCustToMedQueue(customer);
    }

//----------------------------------------------------------------------------------------
//  ************************** Medical examination sequence *************************************
    public void addCustToMedQueue(Customer customer) {
        this.m_medicalQueue.add(customer);
        medicalNextCustomer();
    }

    public void medicalNextCustomer() {
        if(m_medicalQueue.isEmpty()) {
            return;
        }
        LinkedList<Doctor> availableDoctors = getAvailableDoctors();
        if(availableDoctors.isEmpty()) {
            return;
        }

        if(availableDoctors.size() == 1) {
            this.addEventToCalendar(new MedicalStartEvent(m_actSimTime, this, m_medicalQueue.poll(), availableDoctors.poll()));
            return;
        }

        //-1 for index from0, -1 no need for generator for 1 worker
        double randValue = m_medEmpDecGenerators.get(availableDoctors.size() - 2 ).nextDouble();
        for(int i = 0; i < availableDoctors.size(); i++) {
            if(randValue < (i + 1) / availableDoctors.size()) {
                this.addEventToCalendar(new MedicalStartEvent(m_actSimTime, this, m_medicalQueue.poll(), availableDoctors.remove(i)));
                return;
            }
        }
    }

    public LinkedList<Doctor> getAvailableDoctors() {
        LinkedList<Doctor> result = new LinkedList<>();
        for(Doctor doctor : m_doctors) {
            if (doctor.isAvailable())
            {
                result.add(doctor);
            }
        }
        return result;
    }

    public double getMedDuration() {
        return m_medicalGenerator.nextDouble();
    }

    public void endCustomerMedical(Customer customer) {
        m_statistics.replace("SumTimeMedQueue",
                m_statistics.get("SumTimeMedQueue") +
                        (customer.getTimeMedicalStart() - customer.getTimeRegEnd()));
        m_statistics.replace("ExaminedCustomers",
                m_statistics.get("ExaminedCustomers") + 1);
        //go to next queue
        addCustToVaccQueue(customer);
    }

//----------------------------------------------------------------------------------------
//  ************************** Vaccination sequence *************************************
    public void addCustToVaccQueue(Customer customer) {
        this.m_vaccinationQueue.add(customer);
        vaccinateNextCustomer();
    }

    public void vaccinateNextCustomer() {
        if(m_vaccinationQueue.isEmpty()) {
            return;
        }
        LinkedList<Nurse> availableNurses = getAvailableNurses();
        if(availableNurses.isEmpty()) {
            return;
        }

        if(availableNurses.size() == 1) {
            this.addEventToCalendar(new VaccinationStartEvent(m_actSimTime, this, m_vaccinationQueue.poll(), availableNurses.poll()));
            return;
        }

        //-1 for index from0, -1 no need for generator for 1 worker
        double randValue = m_vaccEmpDecGenerators.get(availableNurses.size() - 2 ).nextDouble();
        for(int i = 0; i < availableNurses.size(); i++) {
            if(randValue < (i + 1) / availableNurses.size()) {
                this.addEventToCalendar(new VaccinationStartEvent(m_actSimTime, this, m_vaccinationQueue.poll(), availableNurses.remove(i)));
                return;
            }
        }
    }

    public LinkedList<Nurse> getAvailableNurses() {
        LinkedList<Nurse> result = new LinkedList<>();
        for(Nurse nurse : m_nurses) {
            if (nurse.isAvailable())
            {
                result.add(nurse);
            }
        }
        return result;
    }

    public double getVaccDuration() {
        return m_vaccinationGenerator.nextDouble();
    }

    public void endCustomerVaccination(Customer customer) {
        m_statistics.replace("SumTimeVaccQueue",
                m_statistics.get("SumTimeVaccQueue") +
                        (customer.getTimeVaccinationStart() - customer.getTimeMedicalEnd()));
        m_statistics.replace("VaccinatedCustomers",
                m_statistics.get("VaccinatedCustomers") + 1);
        //go to next queue

        double rand = m_waitingTimeGenerator.nextDouble();
        customer.setTimeWaiting((rand < 0.95) ? 15 * 60 : 30 * 60);
        addEventToCalendar(new WaitingEndEvent(
                m_actSimTime + customer.getTimeWaiting(), this, customer
        ));
        m_waitingRoom.add(customer);
    }
//----------------------------------------------------------------------------------------
//  ************************** End of story for the customer *************************************

    public void endSystem(Customer customer) {
        m_statistics.replace("SumTimeInSystem",
                m_statistics.get("SumTimeInSystem") +
                (m_actSimTime - customer.getTimeOfArrival()));
        m_statistics.replace("SumCustomersCompleted",
                m_statistics.get("SumCustomersCompleted") + 1);
        Customer toBeRemoved = null;
        for (Customer cust : m_waitingRoom) {
            if(customer == cust) {
                toBeRemoved = cust;
            }
        }
        if(toBeRemoved != null) {
            m_waitingRoom.remove(toBeRemoved);
        }
    }
    // --------------------------------------------------------------------
    public HashMap<String, Double> getStatics() {
        return m_statistics;
    }

    @Override
    public void notifyObservers() {
        for (IObserver observer : m_observers) {
            observer.update(m_statistics);
        }
    }
}
