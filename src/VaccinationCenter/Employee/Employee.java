package VaccinationCenter.Employee;

public abstract class Employee {
    private boolean m_availability;
    private double m_totalTimeOccupied;
    private double m_lastTimeAvailable;

    public Employee() {
        m_availability = true;
        m_totalTimeOccupied = 0.0;
        m_lastTimeAvailable = 0.0;
    }

    public boolean isAvailable() {
        return m_availability;
    }

    public void setOccupied(double actualTime) {
        m_lastTimeAvailable = actualTime;
        m_availability = false;
    }

    public void setAvailable(double actualTime) {
        m_totalTimeOccupied += actualTime - m_lastTimeAvailable;
        m_lastTimeAvailable = 0.0;
        m_availability = true;
    }

    public double getTotalTimeOccupied() {
        return m_totalTimeOccupied;
    }

    public void resetTime() {
        m_totalTimeOccupied = 0.0;
        m_lastTimeAvailable = 0.0;
    }
}
