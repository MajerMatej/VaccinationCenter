package Simulation;

import java.util.PriorityQueue;

public abstract class EventSimulationCore extends SimulationCore{
    protected double m_actSimTime;
    protected PriorityQueue<Event> m_eventCalendar;

    public EventSimulationCore(int numberOfReplications) {
        super(numberOfReplications);
        this.m_actSimTime = 0.0;
        this.m_eventCalendar = new PriorityQueue<>();
    }

    protected void doReplication(double endTime) {
        while(m_actSimTime < endTime && !m_eventCalendar.isEmpty()) {
            Event event = m_eventCalendar.poll();
            m_actSimTime = event.getEventTime();
            event.execute();

        }
    }

    public void addEventToCalendar(Event event) {
        m_eventCalendar.add(event);
    }

    public double getActSimTime() {
        return m_actSimTime;
    }


}
