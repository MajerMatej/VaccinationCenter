package VaccinationCenter.Employee;

public class Doctor extends Employee{
    public Doctor() {
    }

    @Override
    public String toStringWithTime(double time) {
        String str = "Doctor " + super.toStringWithTime(time);
        return str;
    }
}
