package VaccinationCenter;

import Simulation.Event;
import Simulation.EventSimulationCore;

public class WaitingEndEvent extends Event {
    Customer m_customer;
    public WaitingEndEvent(double eventTime, VaccCenterSimCore evSimCore, Customer customer) {
        super(eventTime, evSimCore);
        m_customer = customer;
    }

    @Override
    public void execute() {
        VaccCenterSimCore simCore = (VaccCenterSimCore)m_evSimCore;
        simCore.endSystem(m_customer);
    }
}
