package hospital.data;

import hospital.model.Bed;
import hospital.model.BedState;
import hospital.model.Department;

import java.util.*;
import java.util.stream.Collectors;

public class BedIndex {

    private final Map<String, List<Bed>> byDepartment;
    private final Map<String, List<Bed>> byCategory;
    private final Map<String, List<Bed>> bySpecialism;
    private final Map<BedState, List<Bed>> byState;
    private final Map<String, List<Bed>> byRoom;
    private final Map<String, Bed> byBedId;
    private final Map<String, List<String>> departmentsBySpecialism;

    public BedIndex() {
        this.byDepartment = new HashMap<>();
        this.byCategory = new HashMap<>();
        this.bySpecialism = new HashMap<>();
        this.byState = new HashMap<>();
        this.byRoom = new HashMap<>();
        this.byBedId = new HashMap<>();
        this.departmentsBySpecialism = new HashMap<>();
    }

    public void index(List<Bed> beds) {
        clear();
        for (Bed bed : beds) {
            indexBed(bed);
        }
    }

    public void indexBed(Bed bed) {
        byDepartment.computeIfAbsent(bed.getDepartmentName(), k -> new ArrayList<>()).add(bed);
        byCategory.computeIfAbsent(bed.getCategory(), k -> new ArrayList<>()).add(bed);
        bySpecialism.computeIfAbsent(bed.getSpecialism(), k -> new ArrayList<>()).add(bed);
        byState.computeIfAbsent(bed.getState(), k -> new ArrayList<>()).add(bed);
        byRoom.computeIfAbsent(bed.getRoomName(), k -> new ArrayList<>()).add(bed);
        byBedId.put(bed.getBedId(), bed);

        departmentsBySpecialism
            .computeIfAbsent(bed.getSpecialism(), k -> new ArrayList<>())
            .add(bed.getDepartmentName());
    }

    public void updateState(Bed bed, BedState oldState) {
        List<Bed> oldList = byState.get(oldState);
        if (oldList != null) oldList.remove(bed);
        byState.computeIfAbsent(bed.getState(), k -> new ArrayList<>()).add(bed);
    }

    public void clear() {
        byDepartment.clear();
        byCategory.clear();
        bySpecialism.clear();
        byState.clear();
        byRoom.clear();
        byBedId.clear();
        departmentsBySpecialism.clear();
    }

    public List<Bed> getByDepartment(String dept) {
        return byDepartment.getOrDefault(dept, Collections.emptyList());
    }

    public List<Bed> getByCategory(String category) {
        return byCategory.getOrDefault(category, Collections.emptyList());
    }

    public List<Bed> getBySpecialism(String specialism) {
        return bySpecialism.getOrDefault(specialism, Collections.emptyList());
    }

    public List<Bed> getByState(BedState state) {
        return byState.getOrDefault(state, Collections.emptyList());
    }

    public List<Bed> getByRoom(String roomName) {
        return byRoom.getOrDefault(roomName, Collections.emptyList());
    }

    public Bed getByBedId(String bedId) {
        return byBedId.get(bedId);
    }

    public List<Bed> query(String department, String specialism, String category, BedState state) {
        return byBedId.values().stream()
                .filter(b -> department == null || b.getDepartmentName().equals(department))
                .filter(b -> specialism == null || b.getSpecialism().equals(specialism))
                .filter(b -> category == null || b.getCategory().equals(category))
                .filter(b -> state == null || b.getState() == state)
                .collect(Collectors.toList());
    }

    public List<Bed> getAvailableBedsFor(String department, String specialism, String category) {
        return query(department, specialism, category, BedState.AVAILABLE);
    }

    public int countByDepartment(String dept) {
        return getByDepartment(dept).size();
    }

    public int countAvailableByDepartment(String dept) {
        return (int) getByDepartment(dept).stream().filter(Bed::isAvailable).count();
    }

    public int countByCategory(String category) {
        return getByCategory(category).size();
    }

    public int totalIndexed() {
        return byBedId.size();
    }

    public Set<String> getAllDepartments() {
        return byDepartment.keySet();
    }

    public Set<String> getAllCategories() {
        return byCategory.keySet();
    }

    public Set<String> getAllSpecialisms() {
        return bySpecialism.keySet();
    }

    public List<String> getDepartmentsForSpecialism(String specialism) {
        return departmentsBySpecialism.getOrDefault(specialism, Collections.emptyList());
    }
}
