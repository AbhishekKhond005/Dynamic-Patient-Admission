package hospital.service;

import hospital.data.DataStore;
import hospital.model.*;
import hospital.service.assignment.AssignmentStrategy;
import hospital.service.assignment.BestFitStrategy;
import hospital.service.assignment.FirstFitStrategy;

import java.util.List;
import java.util.Optional;

public class BedAssignmentService {

    private final DataStore store;
    private AssignmentStrategy strategy;

    public static class AssignmentResult {
        private final Patient patient;
        private final Bed bed;
        private final boolean success;
        private final String message;

        public AssignmentResult(Patient patient, Bed bed, boolean success, String message) {
            this.patient = patient;
            this.bed = bed;
            this.success = success;
            this.message = message;
        }

        public Patient getPatient() { return patient; }
        public Bed getBed() { return bed; }
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }

        public static AssignmentResult success(Patient p, Bed b) {
            return new AssignmentResult(p, b, true,
                    "Assigned " + b.getBedId() + " in " + b.getRoomName()
                    + " (" + b.getDepartmentName() + ")");
        }

        public static AssignmentResult failure(Patient p, String reason) {
            return new AssignmentResult(p, null, false, "No bed available: " + reason);
        }
    }

    public BedAssignmentService() {
        this.store = DataStore.getInstance();
        this.strategy = new FirstFitStrategy();
    }

    public void setStrategy(AssignmentStrategy strategy) {
        this.strategy = strategy;
    }

    public String getStrategyName() {
        return strategy.getName();
    }

    public AssignmentResult assign(Patient patient) {
        if (patient == null) {
            return AssignmentResult.failure(null, "Null patient");
        }

        List<Bed> candidates = store.getBeds();
        Bed chosen = strategy.findBed(patient, candidates);

        if (chosen == null) {
            return AssignmentResult.failure(patient, "No matching bed for "
                    + patient.getDepartmentNeeded() + "/" + patient.getSpecialismNeeded());
        }

        store.assignBed(patient, chosen);
        return AssignmentResult.success(patient, chosen);
    }

    public AssignmentResult assignWithTriage(Patient patient) {
        if (patient == null) return AssignmentResult.failure(null, "Null patient");

        TriagePriority triage = patient.getTriagePriority();
        if (triage == TriagePriority.RESUSCITATION || triage == TriagePriority.EMERGENCY) {
            List<Bed> allAvailable = store.getAvailableBeds();
            Optional<Bed> anyBed = allAvailable.stream().findFirst();
            if (anyBed.isPresent()) {
                store.assignBed(patient, anyBed.get());
                return AssignmentResult.success(patient, anyBed.get());
            }
            return AssignmentResult.failure(patient, "Emergency but no beds at all");
        }

        return assign(patient);
    }

    public boolean discharge(Patient patient) {
        Bed bed = store.findAssignedBed(patient);
        if (bed == null) return false;
        store.freeBed(bed);
        return true;
    }

    public boolean dischargeByName(String patientName) {
        Bed bed = store.findPatientBed(patientName);
        if (bed == null) return false;
        store.freeBed(bed);
        return true;
    }

    public int getAvailableBedCount() {
        return store.getAvailableBedCount();
    }

    public int getOccupiedBedCount() {
        return store.getOccupiedBedCount();
    }

    public double getOccupancyRate() {
        int total = store.getTotalBedCount();
        if (total == 0) return 0;
        return (double) store.getOccupiedBedCount() / total * 100;
    }
}
