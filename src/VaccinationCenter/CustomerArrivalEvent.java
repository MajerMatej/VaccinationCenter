package VaccinationCenter;

import Simulation.Event;
import Simulation.EventSimulationCore;

public class CustomerArrivalEvent extends Event {

    public CustomerArrivalEvent(double m_eventTime, VaccCenterSimCore m_evSimCore) {
        super(m_eventTime, m_evSimCore);
    }

    @Override
    public void execute() {
        VaccCenterSimCore simCore = ((VaccCenterSimCore) m_evSimCore);
        Customer customer = new Customer(simCore.getNextCustomerID());
        customer.setTimeOfArrival(simCore.getActSimTime());
        simCore.addEventToCalendar(new CustomerArrivalEvent(
                simCore.getNextArrivalTime(), simCore
        ));
    }
}
