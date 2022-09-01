package hospital.service.assignment;

import hospital.model.Bed;
import hospital.model.Patient;

import java.util.List;

public class BestFitStrategy implements AssignmentStrategy {

    @Override
    public String getName() {
        return "Best Fit";
    }

    @Override
    public Bed findBed(Patient patient, List<Bed> candidates) {
        Bed best = null;
        int bestScore = -1;

        for (Bed bed : candidates) {
            if (!bed.isAvailable()) continue;

            int score = 0;
            if (bed.getDepartmentName().equals(patient.getDepartmentNeeded())) score += 10;
            if (bed.getSpecialism().equals(patient.getSpecialismNeeded())) score += 5;
            if (bed.getCategory().equals(patient.getPreferredRoomCategory())) score += 3;

            if (score > bestScore) {
                bestScore = score;
                best = bed;
            }
        }
        return best;
    }
}
