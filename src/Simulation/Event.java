package Simulation;

import java.util.Objects;

public abstract class Event implements Comparable{
    protected double m_eventTime;
    protected EventSimulationCore m_evSimCore;

    public Event(double eventTime, EventSimulationCore evSimCore) {
        this.m_eventTime = eventTime;
        this.m_evSimCore = evSimCore;
    }

    public abstract void execute();

    public double getEventTime() {
        return m_eventTime;
    }

    public void setEventTime(double eventTime) {
        this.m_eventTime = eventTime;
    }

    @Override
    public int compareTo(Object o) {
        return (this.m_eventTime - ((Event)o).getEventTime() < 0) ? -1 :
                (this.m_eventTime - ((Event)o).getEventTime() == 0) ? 0 : 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return m_eventTime == (event.m_eventTime) &&
                Objects.equals(m_evSimCore, event.m_evSimCore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_eventTime, m_evSimCore);
    }
}
