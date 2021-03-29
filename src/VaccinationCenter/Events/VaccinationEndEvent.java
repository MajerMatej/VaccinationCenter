package VaccinationCenter.Events;

import Simulation.CustomerEmployeeEvent;
import Simulation.EventSimulationCore;
import VaccinationCenter.Customer;
import VaccinationCenter.Employee.Employee;
import VaccinationCenter.VaccCenterSimCore;

public class VaccinationEndEvent extends CustomerEmployeeEvent {
    public VaccinationEndEvent(double eventTime, VaccCenterSimCore evSimCore, Customer customer, Employee employee) {
        super(eventTime, evSimCore, customer, employee);
    }

    @Override
    public void execute() {
        m_employee.setAvailable(m_evSimCore.getActSimTime());
        m_customer.setTimeVaccinationEnd(m_evSimCore.getActSimTime());
        VaccCenterSimCore simCore = (VaccCenterSimCore)m_evSimCore;

        simCore.endCustomerVaccination(m_customer);
        simCore.vaccinateNextCustomer();
        simCore.updateAvailableNurses();
    }
}
