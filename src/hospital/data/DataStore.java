package hospital.data;

import hospital.model.*;
import hospital.service.WardGraph;

import java.util.*;
import java.util.stream.Collectors;

public class DataStore {
    private static DataStore instance;

    private List<Department> departments;
    private List<Room> rooms;
    private List<Bed> beds;
    private List<Patient> patientQueue;
    private Map<Patient, Bed> assignmentMap;
    private List<String> roomCategories;
    private Map<String, List<String>> deptSpecialisms;
    private List<PatientStay> stayHistory;
    private BedIndex bedIndex;

    private DataStore() {
        departments = new ArrayList<>();
        rooms = new ArrayList<>();
        beds = new ArrayList<>();
        patientQueue = new ArrayList<>();
        assignmentMap = new HashMap<>();
        roomCategories = new ArrayList<>();
        deptSpecialisms = new HashMap<>();
        stayHistory = new ArrayList<>();
        bedIndex = new BedIndex();
    }

    public static synchronized DataStore getInstance() {
        if (instance == null) instance = new DataStore();
        return instance;
    }

    public static void reset() {
        instance = new DataStore();
    }

    public void addDepartment(Department d) { departments.add(d); }
    public void addRoom(Room r) { rooms.add(r); }
    public void addBed(Bed b) {
        beds.add(b);
        bedIndex.indexBed(b);
    }

    public List<Department> getDepartments() { return departments; }
    public List<Room> getRooms() { return rooms; }
    public List<Bed> getBeds() { return beds; }

    public List<String> getDepartmentNames() {
        return departments.stream().map(Department::getName).collect(Collectors.toList());
    }

    public List<String> getRoomCategories() { return roomCategories; }

    public void buildRoomCategoryList() {
        Set<String> catSet = new HashSet<>();
        for (Room r : rooms) catSet.add(r.getCategory());
        roomCategories = new ArrayList<>(catSet);
    }

    public Map<String, List<String>> getDeptSpecialisms() { return deptSpecialisms; }

    public void registerSpecialism(String dept, String spec) {
        deptSpecialisms.computeIfAbsent(dept, k -> new ArrayList<>());
        if (!deptSpecialisms.get(dept).contains(spec)) {
            deptSpecialisms.get(dept).add(spec);
        }
    }

    public List<String> getSpecialismsForDept(String dept) {
        return deptSpecialisms.getOrDefault(dept, Collections.emptyList());
    }

    public List<String> getAllSpecialisms() {
        return deptSpecialisms.values().stream()
                .flatMap(Collection::stream).distinct().collect(Collectors.toList());
    }

    public List<Department> getUniqueDepartments() {
        List<Department> unique = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        for (Department d : departments) {
            if (!seen.contains(d.getName())) {
                seen.add(d.getName());
                unique.add(d);
            }
        }
        return unique;
    }

    public void addToPatientQueue(Patient p) { patientQueue.add(p); }
    public List<Patient> getPatientQueue() { return patientQueue; }
    public Patient pollFirstPatient() {
        return patientQueue.isEmpty() ? null : patientQueue.remove(0);
    }

    public void assignBed(Patient p, Bed b) {
        assignmentMap.put(p, b);
        b.setState(BedState.OCCUPIED);
        stayHistory.add(new PatientStay(p, b));
        bedIndex.updateState(b, BedState.AVAILABLE);
    }

    public Map<Patient, Bed> getAssignmentMap() { return assignmentMap; }

    public Bed findAssignedBed(Patient p) { return assignmentMap.get(p); }

    public void freeBed(Bed bed) {
        bed.setState(BedState.AVAILABLE);
        bedIndex.updateState(bed, BedState.OCCUPIED);
        assignmentMap.entrySet().removeIf(e -> e.getValue().equals(bed));
    }

    public void freeBedByPatientName(String patientName) {
        for (Map.Entry<Patient, Bed> e : assignmentMap.entrySet()) {
            if (e.getKey().getPatientName().equals(patientName)) {
                Bed bed = e.getValue();
                bed.setState(BedState.AVAILABLE);
                bedIndex.updateState(bed, BedState.OCCUPIED);
                for (PatientStay stay : stayHistory) {
                    if (stay.getPatient().equals(e.getKey()) && stay.isActive()) {
                        stay.discharge();
                    }
                }
                assignmentMap.remove(e.getKey());
                return;
            }
        }
    }

    public BedIndex getBedIndex() {
        return bedIndex;
    }

    public List<PatientStay> getStayHistory() {
        return Collections.unmodifiableList(stayHistory);
    }

    public List<PatientStay> getActiveStays() {
        List<PatientStay> active = new ArrayList<>();
        for (PatientStay stay : stayHistory) {
            if (stay.isActive()) active.add(stay);
        }
        return active;
    }

    public Bed findPatientBed(String patientName) {
        for (Map.Entry<Patient, Bed> e : assignmentMap.entrySet()) {
            if (e.getKey().getPatientName().equals(patientName)) return e.getValue();
        }
        return null;
    }

    public List<Bed> getAvailableBeds() {
        List<Bed> result = new ArrayList<>();
        for (Bed b : beds) {
            if (b.isAvailable()) result.add(b);
        }
        return result;
    }

    public List<Bed> getAvailableBedsFor(String department, String specialism, String category) {
        return beds.stream()
                .filter(Bed::isAvailable)
                .filter(b -> b.getDepartmentName().equals(department))
                .filter(b -> b.getSpecialism().equals(specialism))
                .filter(b -> b.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    public int getTotalBedCount() { return beds.size(); }
    public int getAvailableBedCount() { return (int) beds.stream().filter(Bed::isAvailable).count(); }
    public int getOccupiedBedCount() { return assignmentMap.size(); }

    public WardGraph buildWardGraph() {
        WardGraph graph = new WardGraph();
        for (Room room : rooms) {
            graph.addBidirectionalEdges(room);
        }
        return graph;
    }
}
