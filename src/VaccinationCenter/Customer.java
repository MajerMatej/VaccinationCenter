package VaccinationCenter;

public class Customer {
    private int m_id;
    private double m_timeOfArrival;
    private double m_timeRegStart;
    private double m_timeRegEnd;
    private double m_timeMedicalStart;
    private double m_timeMedicalEnd;
    private double m_timeVaccinationStart;
    private double m_timeVaccinationEnd;
    private double m_timeWaiting;
    private CustomerState m_state;

    public Customer(int m_id) {
        this.m_id = m_id;
    }

    public int getId() {
        return m_id;
    }

    public void setId(int m_id) {
        this.m_id = m_id;
    }

    public double getTimeOfArrival() {
        return m_timeOfArrival;
    }

    public void setTimeOfArrival(double timeOfArrival) {
        m_state = CustomerState.REGISTRATION_QUEUE;
        this.m_timeOfArrival = timeOfArrival;
    }

    public double getTimeRegStart() {
        return m_timeRegStart;
    }

    public void setTimeRegStart(double timeRegStart) {
        m_state = CustomerState.REGISTRATION;
        this.m_timeRegStart = timeRegStart;
    }

    public double getTimeRegEnd() {
        return m_timeRegEnd;
    }

    public void setTimeRegEnd(double timeRegEnd) {
        m_state = CustomerState.MEDICAL_QUEUE;
        this.m_timeRegEnd = timeRegEnd;
    }

    public double getTimeMedicalStart() {
        return m_timeMedicalStart;
    }

    public void setTimeMedicalStart(double timeMedicalStart) {
        m_state = CustomerState.MEDICAL;
        this.m_timeMedicalStart = timeMedicalStart;
    }

    public double getTimeMedicalEnd() {
        return m_timeMedicalEnd;
    }

    public void setTimeMedicalEnd(double timeMedicalEnd) {
        m_state = CustomerState.VACCINATION_QUEUE;
        this.m_timeMedicalEnd = timeMedicalEnd;
    }

    public double getTimeVaccinationStart() {
        return m_timeVaccinationStart;
    }

    public void setTimeVaccinationStart(double timeVaccinationStart) {
        m_state = CustomerState.VACCINATION;
        this.m_timeVaccinationStart = timeVaccinationStart;
    }

    public double getTimeVaccinationEnd() {
        return m_timeVaccinationEnd;
    }

    public void setTimeVaccinationEnd(double timeVaccinationEnd) {
        m_state = CustomerState.WAITING_ROOM;
        this.m_timeVaccinationEnd = timeVaccinationEnd;
    }

    public double getTimeWaiting() {
        return m_timeWaiting;
    }

    public void setTimeWaiting(double timeWaiting) {
        this.m_timeWaiting = timeWaiting;
    }

    @Override
    public String toString() {
        String strState = "Lost";
        switch (m_state) {
            case REGISTRATION_QUEUE:
                strState = "Registration Queue";
                break;
            case REGISTRATION:
                strState = "Registration";
                break;
            case MEDICAL_QUEUE:
                strState = "Medical Queue";
                break;
            case MEDICAL:
                strState = "Medical examination";
                break;
            case VACCINATION_QUEUE:
                strState = "Vaccination Queue";
                break;
            case VACCINATION:
                strState = "Vaccination";
                break;
            case WAITING_ROOM:
                strState = "Waiting room";
                break;
        }

        return "Customer{" +
                String.format("ID: %d , Location: %s, TimeOfArrival %.2f, TimeRegStart: %.2f, TimeRegEnd: %.2f, " +
                        "TimeMedicalStart: %.2f, TimeMedicalEnd: %.2f, TimeVaccinationStart: %.2f, TimeVaccinationEnd: %.2f, " +
                        "TimeWaiting: %.2f}", m_id, strState, m_timeOfArrival, m_timeRegStart, m_timeRegEnd,
                        m_timeMedicalStart, m_timeMedicalEnd, m_timeVaccinationStart, m_timeVaccinationEnd,  m_timeWaiting);
    }
}
