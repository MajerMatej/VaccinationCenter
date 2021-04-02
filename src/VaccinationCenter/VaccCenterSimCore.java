package VaccinationCenter;

import Simulation.*;
import VaccinationCenter.Employee.AdminWorker;
import VaccinationCenter.Employee.Doctor;
import VaccinationCenter.Employee.Nurse;
import Simulation.Interfaces.IObserver;
import Simulation.Interfaces.ISubject;
import VaccinationCenter.Events.*;

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
    //private ExpRandomGenerator m_arrivalgen;

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

    private ArrayList<Customer> m_customers;
    // Stat variables -----------------------------------------
    private HashMap<String, Double> m_statistics;

    // Others -------------------------------------------------
    private int m_seed;
    private int m_customerSequence;
    private double m_missedAppointmentNum;
    private int m_maxCustomers;
    //private int m_missedCustomers;
    private double m_repTime;

    public VaccCenterSimCore(int numberOfReplications) {
        super(numberOfReplications);
        init(-1, 1, 1, 1, 540);
    }

    public VaccCenterSimCore(int numberOfReplications, int seed) {
        super(numberOfReplications);
        init(seed, 1, 1, 1, 540);
    }

    public VaccCenterSimCore(int numberOfReplications, int seed,
        int numOfAdminWorkers, int numOfDoctors, int numOfNurses, double repTime, int maxCustomers) {
        super(numberOfReplications);
        m_repTime = repTime;
        init(seed, numOfAdminWorkers, numOfDoctors, numOfNurses, maxCustomers);

    }

    private void init(int seed, int numOfAdminWorkers, int numOfDoctors, int numOfNurses, int maxCustomers) {
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
        m_statistics.put("ActualSimulationTime", 0.0);

        m_statistics.put("AdminWorkersUtilization", 0.0);
        m_statistics.put("DoctorsUtilization", 0.0);
        m_statistics.put("NursesUtilization", 0.0);

        m_statistics.put("AdminWorkersAvailability", 0.0);
        m_statistics.put("DoctorsAvailability", 0.0);
        m_statistics.put("NursesAvailability", 0.0);

        m_statistics.put("RegQueueSize", 0.0);
        m_statistics.put("MedQueueSize", 0.0);
        m_statistics.put("VaccQueueSize", 0.0);
        m_statistics.put("WaitingRoomSize", 0.0);
        m_statistics.put("SumTimeInWaitingRoom", 0.0);
        m_statistics.put("SumWaitingRoomSize", 0.0);

        m_statistics.put("WorkerUtilizationGlobal", 0.0);
        m_statistics.put("DoctorUtilizationGlobal", 0.0);
        m_statistics.put("NurseUtilizationGlobal", 0.0);
        m_statistics.put("WaitingRoomSizeGlobal", 0.0);

        m_customerSequence = 0;
        m_seed = seed;
        m_maxCustomers = maxCustomers;
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
        //m_arrivalgen = new ExpRandomGenerator(seedGenerator.nextInt(),1.5 *60);
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

        m_customers = new ArrayList<>();
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
        m_statistics.replace("SumWaitingRoomSize", 0.0);

        m_statistics.replace("AdminWorkersUtilization", 0.0);
        m_statistics.replace("DoctorsUtilization", 0.0);
        m_statistics.replace("NursesUtilization", 0.0);

        m_statistics.replace("AdminWorkersAvailability", 0.0);
        m_statistics.replace("DoctorsAvailability", 0.0);
        m_statistics.replace("NursesAvailability", 0.0);

        m_statistics.replace("RegQueueSize", 0.0);
        m_statistics.replace("MedQueueSize", 0.0);
        m_statistics.replace("VaccQueueSize", 0.0);
        m_statistics.replace("WaitingRoomSize", 0.0);
        m_statistics.replace("SumTimeInWaitingRoom", 0.0);
        m_statistics.replace("SumWaitingRoomSize", 0.0);

        m_customerSequence = 0;

        m_missedAppointmentNum = 0;

        m_registrationQueue.clear();
        m_medicalQueue.clear();
        m_vaccinationQueue.clear();
        m_waitingRoom.clear();

        m_actSimTime = 0.0;

        for (AdminWorker worker : m_adminWorkers) {
            worker.setAvailable(m_actSimTime);
            worker.resetTime();
        }

        for (Doctor doctor : m_doctors) {
            doctor.setAvailable(m_actSimTime);
            doctor.resetTime();
        }

        for (Nurse nurse : m_nurses) {
            nurse.setAvailable(m_actSimTime);
            nurse.resetTime();
        }
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
        m_stoped = false;
        m_statistics.replace("ActualSimulationTime", m_actSimTime);
        notifyObservers();
    }

    @Override
    protected void onReplicationStart() {
        reset();
        //this.m_eventCalendar.clear();
        Event firstArrival = new CustomerArrivalEvent(0, this);
        m_eventCalendar.add(firstArrival);
        m_missedAppointmentNum = (m_missedAppointmentsGenerator.nextDouble() / 540) * m_maxCustomers;
        //System.out.println(m_missedAppointmentNum);

        doReplication(m_repTime);
    }

    @Override
    protected void onReplicationEnd() {
        m_statistics.replace("ActualSimulationTime", m_actSimTime);
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

        m_statistics.replace("WorkerUtilizationGlobal",
                m_statistics.get("WorkerUtilizationGlobal") +
                        (m_statistics.get("AdminWorkersUtilization") /
                                m_statistics.get("ActualSimulationTime")));
        m_statistics.replace("DoctorUtilizationGlobal",
                m_statistics.get("DoctorUtilizationGlobal") +
                        (m_statistics.get("DoctorsUtilization") /
                                m_statistics.get("ActualSimulationTime")));
        m_statistics.replace("NurseUtilizationGlobal",
                m_statistics.get("NurseUtilizationGlobal") +
                        (m_statistics.get("NursesUtilization") /
                                m_statistics.get("ActualSimulationTime")));
        m_statistics.replace("WaitingRoomSizeGlobal",
                m_statistics.get("WaitingRoomSizeGlobal") +
                        (m_statistics.get("SumWaitingRoomSize") /
                                m_statistics.get("ActualSimulationTime")));

        m_statistics.replace("CompleteReplications",
                m_statistics.get("CompleteReplications").doubleValue() + 1);
    }

    @Override
    protected void doReplication(double endTime) {
        super.doReplication(endTime);
    }

    public int getNextCustomerID() {
        return ++m_customerSequence;
    }

    public boolean customerArrived() {
        if(m_missedAppDecisionGenerator.nextDouble() < m_missedAppointmentNum / m_maxCustomers) {
            m_statistics.replace("MissedCustomers",
                    m_statistics.get("MissedCustomers") + 1);
            return false;
        }
        return true;
    }

    public double getNextArrivalTime() {
        double time = m_actSimTime;

        while(!customerArrived()) {
            time += m_repTime / m_maxCustomers;
        }
        return time + m_repTime / m_maxCustomers;
    }
//----------------------------------------------------------------------------------------
//  ************************** Registration sequence *************************************
    public void addCustToRegQueue(Customer customer) {
        this.m_registrationQueue.add(customer);
        this.m_customers.add(customer);
        registerNextCustomer();
        m_statistics.replace("RegQueueSize", (double)m_registrationQueue.size());
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
            if(randValue < ((i + 1) / (double)availableWorkers.size())) {
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

    public void updateAvailableWorkers() {
        double totalUtilizationTime = 0.0;
        int availableWorkers = 0;
        for (AdminWorker worker : m_adminWorkers) {
            if(worker.isAvailable()) {
                availableWorkers++;
            }
            totalUtilizationTime += worker.getTotalTimeOccupied();
        }

        m_statistics.replace("AdminWorkersUtilization",  totalUtilizationTime / m_adminWorkers.size());
        m_statistics.replace("AdminWorkersAvailability",  (double)availableWorkers);
        m_statistics.replace("RegQueueSize", (double)m_registrationQueue.size());
    }

//----------------------------------------------------------------------------------------
//  ************************** Medical examination sequence *************************************
    public void addCustToMedQueue(Customer customer) {
        this.m_medicalQueue.add(customer);
        medicalNextCustomer();
        m_statistics.replace("MedQueueSize", (double)m_medicalQueue.size());
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
            if(randValue < (i + 1) / (double)availableDoctors.size()) {
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

    public void updateAvailableDoctors() {
        double totalUtilizationTime = 0.0;
        int availableDoctors = 0;
        for (Doctor doctor : m_doctors) {
            if(doctor.isAvailable()) {
                availableDoctors++;
            }
            totalUtilizationTime += doctor.getTotalTimeOccupied();
        }

        m_statistics.replace("DoctorsUtilization",  totalUtilizationTime / m_doctors.size());
        m_statistics.replace("DoctorsAvailability",  (double)availableDoctors);
        m_statistics.replace("MedQueueSize", (double)m_medicalQueue.size());
    }

//----------------------------------------------------------------------------------------
//  ************************** Vaccination sequence *************************************
    public void addCustToVaccQueue(Customer customer) {
        this.m_vaccinationQueue.add(customer);
        vaccinateNextCustomer();
        m_statistics.replace("VaccQueueSize", (double)m_vaccinationQueue.size());
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
            if(randValue < (i + 1) / (double)availableNurses.size()) {
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
        m_statistics.replace("WaitingRoomSize", (double)m_waitingRoom.size());
        m_statistics.replace("SumWaitingRoomSize",
                m_statistics.get("SumWaitingRoomSize") + m_statistics.get("WaitingRoomSize"));
    }

    public void updateAvailableNurses() {
        double totalUtilizationTime = 0.0;
        int availableNurses = 0;
        for (Nurse nurse : m_nurses) {
            if(nurse.isAvailable()) {
                availableNurses++;
            }
            totalUtilizationTime += nurse.getTotalTimeOccupied();
        }

        m_statistics.replace("NursesUtilization",  totalUtilizationTime / m_nurses.size());
        m_statistics.replace("NursesAvailability",  (double)availableNurses);
        m_statistics.replace("VaccQueueSize", (double)m_vaccinationQueue.size());
    }
//----------------------------------------------------------------------------------------
//  ************************** End of story for the customer *************************************

    public void endSystem(Customer customer) {
        m_statistics.replace("SumTimeInSystem",
                m_statistics.get("SumTimeInSystem") +
                (m_actSimTime - customer.getTimeOfArrival()));
        m_statistics.replace("SumCustomersCompleted",
                m_statistics.get("SumCustomersCompleted") + 1);
        Customer toBeRemovedWR = null;
        for (Customer cust : m_waitingRoom) {
            if(customer == cust) {
                toBeRemovedWR = cust;
            }
        }
        if(toBeRemovedWR != null) {
            m_waitingRoom.remove(toBeRemovedWR);
        }

        Customer toBeRemoved = null;
        for (Customer cust : m_customers) {
            if(customer == cust) {
                toBeRemoved = cust;
            }
        }
        if(toBeRemoved != null) {
            m_customers.remove(toBeRemoved);
        }

        m_statistics.replace("WaitingRoomSize", (double)m_waitingRoom.size());
        m_statistics.replace("SumWaitingRoomSize",
                m_statistics.get("SumWaitingRoomSize") + customer.getTimeWaiting());

    }
    // --------------------------------------------------------------------
    @Override
    public void notifyObservers() {
        m_statistics.replace("ActualSimulationTime", m_actSimTime);
        for (IObserver observer : m_observers) {
            observer.update(m_statistics.get("CompleteReplications"));
            observer.update(m_statistics);
            observer.update(employeesToString());
            observer.update(customersToString());
        }
    }

    private LinkedList<String> employeesToString() {
        LinkedList<String> result = new LinkedList<>();
        for (AdminWorker worker : m_adminWorkers) {
            result.add(worker.toStringWithTime(m_actSimTime));
        }

        for(Doctor doctor : m_doctors) {
            result.add(doctor.toStringWithTime(m_actSimTime));
        }

        for(Nurse nurse : m_nurses) {
            result.add(nurse.toStringWithTime(m_actSimTime));
        }
        return result;
    }

    private ArrayList<String> customersToString() {
        ArrayList<String> result = new ArrayList<>();
        for (Customer customer : m_customers) {
            result.add(customer.toString());
        }
        return result;
    }
}
