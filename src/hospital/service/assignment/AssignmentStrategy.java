package hospital.service.assignment;

import hospital.model.Bed;
import hospital.model.Patient;

import java.util.List;

public interface AssignmentStrategy {
    String getName();
    Bed findBed(Patient patient, List<Bed> candidates);
}
