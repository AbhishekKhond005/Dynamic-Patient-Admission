package hospital.model;

import java.time.LocalDateTime;
import java.util.Comparator;

public class Patient implements Comparable<Patient> {
    private String patientName;
    private int age;
    private String gender;
    private String departmentNeeded;
    private String specialismNeeded;
    private String preferredRoomCategory;
    private TriagePriority triagePriority;
    private LocalDateTime arrivalTime;

    public Patient() {
        this.arrivalTime = LocalDateTime.now();
        this.triagePriority = TriagePriority.NON_URGENT;
    }

    public Patient(String patientName, int age, String gender, String departmentNeeded,
                   String preferredRoomCategory, String specialismNeeded) {
        this();
        this.patientName = patientName;
        this.age = age;
        this.gender = gender;
        this.departmentNeeded = departmentNeeded;
        this.preferredRoomCategory = preferredRoomCategory;
        this.specialismNeeded = specialismNeeded;
    }

    public String getPatientName() { return patientName; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getDepartmentNeeded() { return departmentNeeded; }
    public String getSpecialismNeeded() { return specialismNeeded; }
    public String getPreferredRoomCategory() { return preferredRoomCategory; }
    public TriagePriority getTriagePriority() { return triagePriority; }
    public LocalDateTime getArrivalTime() { return arrivalTime; }

    public void setPatientName(String patientName) { this.patientName = patientName; }
    public void setAge(int age) { this.age = age; }
    public void setGender(String gender) { this.gender = gender; }
    public void setDepartmentNeeded(String departmentNeeded) { this.departmentNeeded = departmentNeeded; }
    public void setSpecialismNeeded(String specialismNeeded) { this.specialismNeeded = specialismNeeded; }
    public void setPreferredRoomCategory(String preferredRoomCategory) { this.preferredRoomCategory = preferredRoomCategory; }
    public void setTriagePriority(TriagePriority triagePriority) { this.triagePriority = triagePriority; }

    @Override
    public int compareTo(Patient other) {
        return Comparator.comparingInt((Patient p) -> p.triagePriority.getLevel())
                .thenComparing(Patient::getArrivalTime)
                .compare(this, other);
    }

    @Override
    public String toString() {
        return patientName + " [" + departmentNeeded + " / " + specialismNeeded + "] "
                + (triagePriority != null ? triagePriority.name() : "NO_TRIAGE");
    }
}
