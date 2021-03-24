package VaccinationCenter.Employee;

public abstract class Employee {
    private boolean m_availability;

    public Employee() {
        m_availability = true;
    }

    public boolean isAvailable() {
        return m_availability;
    }

    public void setOccupied() {
        m_availability = false;
    }

    public void setAvailable() {
        m_availability = true;
    }
}
