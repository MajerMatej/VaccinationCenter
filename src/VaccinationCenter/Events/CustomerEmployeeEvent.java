package VaccinationCenter.Events;

import Simulation.Event;
import Simulation.EventSimulationCore;
import VaccinationCenter.Customer;
import VaccinationCenter.Employee.Employee;

public abstract class CustomerEmployeeEvent extends Event {
    protected Customer m_customer;
    protected Employee m_employee;

    public CustomerEmployeeEvent(double eventTime, EventSimulationCore evSimCore, Customer customer, Employee employee) {
        super(eventTime, evSimCore);
        this.m_customer = customer;
        this.m_employee = employee;
    }

    @Override
    public void execute() {
    }
}
