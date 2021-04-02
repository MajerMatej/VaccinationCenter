package VaccinationCenter.Events;

import VaccinationCenter.Customer;
import VaccinationCenter.Employee.Employee;
import VaccinationCenter.VaccCenterSimCore;

public class RegistrationEndEvent extends CustomerEmployeeEvent {
    public RegistrationEndEvent(double eventTime, VaccCenterSimCore evSimCore, Customer customer, Employee employee) {
        super(eventTime, evSimCore, customer, employee);
    }

    @Override
    public void execute() {
        m_employee.setAvailable(m_evSimCore.getActSimTime());
        m_customer.setTimeRegEnd(m_evSimCore.getActSimTime());
        VaccCenterSimCore simCore = (VaccCenterSimCore)m_evSimCore;

        simCore.endCustomerRegistration(m_customer);
        simCore.registerNextCustomer();
        simCore.updateAvailableWorkers();
    }
}
