package hospital.model;

import java.time.LocalDateTime;

public class PatientStay {
    private final Patient patient;
    private final Bed bed;
    private final LocalDateTime admissionTime;
    private LocalDateTime dischargeTime;

    public PatientStay(Patient patient, Bed bed) {
        this.patient = patient;
        this.bed = bed;
        this.admissionTime = LocalDateTime.now();
    }

    public Patient getPatient() { return patient; }
    public Bed getBed() { return bed; }
    public LocalDateTime getAdmissionTime() { return admissionTime; }
    public LocalDateTime getDischargeTime() { return dischargeTime; }

    public void discharge() {
        this.dischargeTime = LocalDateTime.now();
        this.bed.setState(BedState.AVAILABLE);
    }

    public boolean isActive() {
        return dischargeTime == null;
    }

    public long getStayDurationMinutes() {
        LocalDateTime end = (dischargeTime != null) ? dischargeTime : LocalDateTime.now();
        return java.time.Duration.between(admissionTime, end).toMinutes();
    }

    public String getStayDurationFormatted() {
        long minutes = getStayDurationMinutes();
        long hours = minutes / 60;
        long mins = minutes % 60;
        return hours + "h " + mins + "m";
    }

    @Override
    public String toString() {
        return patient.getPatientName() + " -> " + bed.getBedId()
                + " (admitted: " + admissionTime + ")";
    }
}
