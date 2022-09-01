package hospital.service.assignment;

import hospital.model.Bed;
import hospital.model.Patient;

import java.util.List;

public class FirstFitStrategy implements AssignmentStrategy {

    @Override
    public String getName() {
        return "First Fit";
    }

    @Override
    public Bed findBed(Patient patient, List<Bed> candidates) {
        for (Bed bed : candidates) {
            if (!bed.isAvailable()) continue;
            boolean deptMatch = bed.getDepartmentName().equals(patient.getDepartmentNeeded());
            boolean specMatch = bed.getSpecialism().equals(patient.getSpecialismNeeded());
            boolean catMatch = bed.getCategory().equals(patient.getPreferredRoomCategory());
            if (deptMatch && specMatch && catMatch) {
                return bed;
            }
        }
        for (Bed bed : candidates) {
            if (!bed.isAvailable()) continue;
            boolean deptMatch = bed.getDepartmentName().equals(patient.getDepartmentNeeded());
            boolean specMatch = bed.getSpecialism().equals(patient.getSpecialismNeeded());
            if (deptMatch && specMatch) {
                return bed;
            }
        }
        for (Bed bed : candidates) {
            if (!bed.isAvailable()) continue;
            if (bed.getDepartmentName().equals(patient.getDepartmentNeeded())) {
                return bed;
            }
        }
        return null;
    }
}
