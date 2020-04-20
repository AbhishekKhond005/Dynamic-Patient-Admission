package hospital.model;

import java.util.ArrayList;
import java.util.List;

public class Department {
    private String name;
    private int deptId;
    private List<Room> rooms;

    public Department(String name, int deptId) {
        this.name = name;
        this.deptId = deptId;
        this.rooms = new ArrayList<>();
    }

    public String getName() { return name; }
    public int getDeptId() { return deptId; }
    public List<Room> getRooms() { return rooms; }

    public void addRoom(Room room) { rooms.add(room); }

    public Room getRoom(String roomName) {
        for (Room r : rooms) {
            if (r.getRoomName().equals(roomName)) return r;
        }
        return null;
    }

    public List<Bed> getAllBeds() {
        List<Bed> all = new ArrayList<>();
        for (Room r : rooms) all.addAll(r.getBeds());
        return all;
    }

    public List<Bed> getAvailableBeds() {
        List<Bed> result = new ArrayList<>();
        for (Room r : rooms) result.addAll(r.getAvailableBeds());
        return result;
    }

    public int getAvailableBedCount() {
        int count = 0;
        for (Room r : rooms) count += r.getAvailableCount();
        return count;
    }

    @Override
    public String toString() {
        return name;
    }
}
