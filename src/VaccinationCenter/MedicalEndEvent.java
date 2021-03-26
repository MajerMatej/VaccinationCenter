package VaccinationCenter;

import Simulation.CustomerEmployeeEvent;
import Simulation.EventSimulationCore;
import VaccinationCenter.Employee.Employee;

public class MedicalEndEvent extends CustomerEmployeeEvent {
    public MedicalEndEvent(double eventTime, VaccCenterSimCore evSimCore, Customer customer, Employee employee) {
        super(eventTime, evSimCore, customer, employee);
    }

    @Override
    public void execute() {
        m_employee.setAvailable();
        m_customer.setTimeMedicalEnd(m_evSimCore.getActSimTime());
        VaccCenterSimCore simCore = (VaccCenterSimCore)m_evSimCore;

        simCore.endCustomerMedical(m_customer);
        simCore.medicalNextCustomer();
    }
}
