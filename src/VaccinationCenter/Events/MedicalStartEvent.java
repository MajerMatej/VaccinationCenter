package VaccinationCenter.Events;

import VaccinationCenter.Customer;
import VaccinationCenter.Employee.Employee;
import VaccinationCenter.VaccCenterSimCore;

public class MedicalStartEvent extends CustomerEmployeeEvent {
    public MedicalStartEvent(double eventTime, VaccCenterSimCore evSimCore, Customer customer, Employee employee) {
        super(eventTime, evSimCore, customer, employee);
    }

    @Override
    public void execute() {
        m_employee.setOccupied(m_evSimCore.getActSimTime());
        m_customer.setTimeMedicalStart(m_evSimCore.getActSimTime());
        VaccCenterSimCore simCore = (VaccCenterSimCore)m_evSimCore;

        simCore.addEventToCalendar(new MedicalEndEvent(
                simCore.getActSimTime() + simCore.getMedDuration(), simCore, m_customer, m_employee
        ));
        simCore.updateAvailableDoctors();
    }
}
