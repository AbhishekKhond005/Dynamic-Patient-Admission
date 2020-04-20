package hospital.model;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private String roomName;
    private String category;
    private String specialism;
    private Department department;
    private List<Bed> beds;

    public Room(String roomName, String category, String specialism, Department department) {
        this.roomName = roomName;
        this.category = category;
        this.specialism = specialism;
        this.department = department;
        this.beds = new ArrayList<>();
    }

    public String getRoomName() { return roomName; }
    public String getCategory() { return category; }
    public String getSpecialism() { return specialism; }
    public Department getDepartment() { return department; }
    public List<Bed> getBeds() { return beds; }

    public void addBed(Bed bed) { beds.add(bed); }

    public List<Bed> getAvailableBeds() {
        List<Bed> result = new ArrayList<>();
        for (Bed b : beds) {
            if (b.isAvailable()) result.add(b);
        }
        return result;
    }

    public int getAvailableCount() {
        int count = 0;
        for (Bed b : beds) {
            if (b.isAvailable()) count++;
        }
        return count;
    }

    @Override
    public String toString() {
        return roomName + " (" + category + ")";
    }
}
