package VaccinationCenter.Events;

import Simulation.Event;
import VaccinationCenter.Customer;
import VaccinationCenter.VaccCenterSimCore;

public class CustomerArrivalEvent extends Event {

    public CustomerArrivalEvent(double m_eventTime, VaccCenterSimCore m_evSimCore) {
        super(m_eventTime, m_evSimCore);
    }

    @Override
    public void execute() {
        VaccCenterSimCore simCore = ((VaccCenterSimCore) m_evSimCore);
        Customer customer = new Customer(simCore.getNextCustomerID());
        customer.setTimeOfArrival(simCore.getActSimTime());
        if(!simCore.isSimulationCooling()) {
            simCore.addEventToCalendar(new CustomerArrivalEvent(
                    simCore.getNextArrivalTime(), simCore
            ));

            simCore.addCustToRegQueue(customer);
        }
    }
}
