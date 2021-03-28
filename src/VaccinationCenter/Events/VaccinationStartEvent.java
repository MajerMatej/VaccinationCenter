package VaccinationCenter.Events;

import Simulation.CustomerEmployeeEvent;
import VaccinationCenter.Customer;
import VaccinationCenter.Employee.Employee;
import VaccinationCenter.VaccCenterSimCore;

public class VaccinationStartEvent extends CustomerEmployeeEvent {
    public VaccinationStartEvent(double eventTime, VaccCenterSimCore evSimCore, Customer customer, Employee employee) {
        super(eventTime, evSimCore, customer, employee);
    }

    @Override
    public void execute() {
        m_employee.setOccupied();
        m_customer.setTimeVaccinationStart(m_evSimCore.getActSimTime());
        VaccCenterSimCore simCore = (VaccCenterSimCore)m_evSimCore;

        simCore.addEventToCalendar(new VaccinationEndEvent(
                simCore.getActSimTime() + simCore.getVaccDuration(), simCore, m_customer, m_employee
        ));
    }
}
