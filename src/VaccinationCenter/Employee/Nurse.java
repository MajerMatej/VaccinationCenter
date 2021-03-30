package VaccinationCenter.Employee;

public class Nurse extends Employee {
    public Nurse() {
    }
    @Override
    public String toStringWithTime(double time) {
        String str = "Nurse " + super.toStringWithTime(time);
        return str;
    }
}
