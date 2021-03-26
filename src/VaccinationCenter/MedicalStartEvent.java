package VaccinationCenter;

import Simulation.CustomerEmployeeEvent;
import Simulation.EventSimulationCore;
import VaccinationCenter.Employee.Employee;

public class MedicalStartEvent extends CustomerEmployeeEvent {
    public MedicalStartEvent(double eventTime, VaccCenterSimCore evSimCore, Customer customer, Employee employee) {
        super(eventTime, evSimCore, customer, employee);
    }

    @Override
    public void execute() {
        m_employee.setOccupied();
        m_customer.setTimeMedicalStart(m_evSimCore.getActSimTime());
        VaccCenterSimCore simCore = (VaccCenterSimCore)m_evSimCore;

        simCore.addEventToCalendar(new MedicalEndEvent(
                simCore.getActSimTime() + simCore.getMedDuration(), simCore, m_customer, m_employee
        ));
    }
}
