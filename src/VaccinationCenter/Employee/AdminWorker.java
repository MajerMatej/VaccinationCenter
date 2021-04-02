package VaccinationCenter.Employee;

public class AdminWorker extends Employee{
    public AdminWorker() {
    }

    @Override
    public String toStringWithTime(double time) {
        String str = "AdminWorker " + super.toStringWithTime(time);
        return str;
    }
}
