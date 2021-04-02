package Simulation;

public class SystemEvent extends Event{
        private int m_speed;
        public SystemEvent(double eventTime, EventSimulationCore evSimCore, int speed) {
        super(eventTime, evSimCore);
        m_speed = speed;
    }

    @Override
    public void execute() {
        try {
            Thread.sleep(300 / m_speed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(!m_evSimCore.isSimulationCooling()) {
            m_evSimCore.addEventToCalendar(new SystemEvent(m_eventTime + 60, m_evSimCore, m_speed));
        }
    }
}
