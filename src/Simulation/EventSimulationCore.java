package Simulation;

import java.util.PriorityQueue;

public abstract class EventSimulationCore extends SimulationCore{
    protected double m_actSimTime;
    protected PriorityQueue<Event> m_eventCalendar;
    private boolean m_paused;
    private int m_speed;
    private boolean m_turboMode;
    private boolean m_cooling;

    public EventSimulationCore(int numberOfReplications) {
        super(numberOfReplications);
        this.m_actSimTime = 0.0;
        this.m_eventCalendar = new PriorityQueue<>();
        m_paused = false;
        m_turboMode = false;
        m_speed = 1;
        m_cooling = false;
    }

    protected void doReplication(double endTime) {
        m_cooling = false;
        if(!m_turboMode) {
            addEventToCalendar(new SystemEvent(0.0, this, m_speed));
        }
        while(/*m_actSimTime < endTime &&*/ !m_eventCalendar.isEmpty() && !m_stoped) {
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
            if(!m_turboMode && event instanceof SystemEvent) {
                notifyObservers();
            }
            if(m_actSimTime >= endTime) {
                m_cooling = true;
            }
        }

        /*if(m_turboMode) {
            notifyObservers();
        }*/
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

    public void setTurbo(boolean turbo) {
        m_turboMode = turbo;
    }

    public void setSpeed(int speed) {
        m_speed = speed;
    }

    public boolean isSimulationCooling() {
        return m_cooling;
    }
}
