package Simulation;

import java.util.PriorityQueue;

public abstract class EventSimulationCore extends SimulationCore{
    protected double m_actSimTime;
    protected PriorityQueue<Event> m_eventCalendar;
    private boolean m_paused;
    private boolean m_stoped;

    public EventSimulationCore(int numberOfReplications) {
        super(numberOfReplications);
        this.m_actSimTime = 0.0;
        this.m_eventCalendar = new PriorityQueue<>();
        m_paused = false;
    }

    protected void doReplication(double endTime) {
        while(m_actSimTime < endTime && !m_eventCalendar.isEmpty() && !m_stoped) {
            if(m_paused) {
                while(m_paused) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            Event event = m_eventCalendar.poll();
            m_actSimTime = event.getEventTime();
            event.execute();
            notifyObservers();
        }
    }
    public void addEventToCalendar(Event event) {
        m_eventCalendar.add(event);
    }

    public double getActSimTime() {
        return m_actSimTime;
    }

    public void pause() {
        m_paused = true;
    }

    public void resume() {
        m_paused = false;
    }

    public void stop() {
        m_stoped = true;
    }
}
