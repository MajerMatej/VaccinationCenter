package VaccinationCenter.Events;

import VaccinationCenter.Customer;
import VaccinationCenter.Employee.Employee;
import VaccinationCenter.VaccCenterSimCore;

public class RegistrationStartEvent extends CustomerEmployeeEvent {
    public RegistrationStartEvent(double eventTime, VaccCenterSimCore evSimCore, Customer customer, Employee employee) {
        super(eventTime, evSimCore, customer, employee);
    }

    @Override
    public void execute() {
        m_employee.setOccupied(m_evSimCore.getActSimTime());
        m_customer.setTimeRegStart(m_evSimCore.getActSimTime());
        VaccCenterSimCore simCore = (VaccCenterSimCore)m_evSimCore;

        simCore.addEventToCalendar(new RegistrationEndEvent(
                simCore.getActSimTime() + simCore.getRegDuration(), simCore, m_customer, m_employee
        ));
        simCore.updateAvailableWorkers();
    }
}
