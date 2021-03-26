package VaccinationCenter;

import Simulation.CustomerEmployeeEvent;
import Simulation.EventSimulationCore;
import VaccinationCenter.Employee.Employee;

public class VaccinationEndEvent extends CustomerEmployeeEvent {
    public VaccinationEndEvent(double eventTime, VaccCenterSimCore evSimCore, Customer customer, Employee employee) {
        super(eventTime, evSimCore, customer, employee);
    }

    @Override
    public void execute() {
        m_employee.setAvailable();
        m_customer.setTimeVaccinationEnd(m_evSimCore.getActSimTime());
        VaccCenterSimCore simCore = (VaccCenterSimCore)m_evSimCore;

        simCore.endCustomerVaccination(m_customer);
        simCore.vaccinateNextCustomer();
    }
}
