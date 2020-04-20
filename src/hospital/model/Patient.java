package hospital.model;

public class Patient {
    private String patientName;
    private int age;
    private String gender;
    private String departmentNeeded;
    private String specialismNeeded;
    private String preferredRoomCategory;

    public Patient() {}

    public Patient(String patientName, int age, String gender, String departmentNeeded,
                   String preferredRoomCategory, String specialismNeeded) {
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

    public void setPatientName(String patientName) { this.patientName = patientName; }
    public void setAge(int age) { this.age = age; }
    public void setGender(String gender) { this.gender = gender; }
    public void setDepartmentNeeded(String departmentNeeded) { this.departmentNeeded = departmentNeeded; }
    public void setSpecialismNeeded(String specialismNeeded) { this.specialismNeeded = specialismNeeded; }
    public void setPreferredRoomCategory(String preferredRoomCategory) { this.preferredRoomCategory = preferredRoomCategory; }

    @Override
    public String toString() {
        return patientName + " [" + departmentNeeded + " / " + specialismNeeded + "]";
    }
}
