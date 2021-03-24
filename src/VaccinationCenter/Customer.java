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
    private double m_timeWaitingTime;

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
        this.m_timeOfArrival = timeOfArrival;
    }

    public double getTimeRegStart() {
        return m_timeRegStart;
    }

    public void setTimeRegStart(double timeRegStart) {
        this.m_timeRegStart = timeRegStart;
    }

    public double getTimeRegEnd() {
        return m_timeRegEnd;
    }

    public void setTimeRegEnd(double timeRegEnd) {
        this.m_timeRegEnd = timeRegEnd;
    }

    public double getTimeMedicalStart() {
        return m_timeMedicalStart;
    }

    public void setTimeMedicalStart(double timeMedicalStart) {
        this.m_timeMedicalStart = timeMedicalStart;
    }

    public double getTimeMedicalEnd() {
        return m_timeMedicalEnd;
    }

    public void setTimeMedicalEnd(double timeMedicalEnd) {
        this.m_timeMedicalEnd = timeMedicalEnd;
    }

    public double getTimeVaccinationStart() {
        return m_timeVaccinationStart;
    }

    public void setTimeVaccinationStart(double timeVaccinationStart) {
        this.m_timeVaccinationStart = timeVaccinationStart;
    }

    public double getTimeVaccinationEnd() {
        return m_timeVaccinationEnd;
    }

    public void setTimeVaccinationEnd(double timeVaccinationEnd) {
        this.m_timeVaccinationEnd = timeVaccinationEnd;
    }

    public double getTimeWaitingTime() {
        return m_timeWaitingTime;
    }

    public void setTimeWaitingTime(double timeWaitingTime) {
        this.m_timeWaitingTime = timeWaitingTime;
    }


}
