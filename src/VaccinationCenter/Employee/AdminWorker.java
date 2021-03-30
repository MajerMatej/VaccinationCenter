package VaccinationCenter.Employee;

public class AdminWorker extends Employee{
    public AdminWorker() {
    }

    @Override
    public String toStringWithTime(double time) {
        String str = "Admin worker " + super.toStringWithTime(time);
        return str;
    }
}
