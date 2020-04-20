package hospital.model;

public class Bed {
    private String bedId;
    private BedState state;
    private Room room;

    public Bed(String bedId, Room room) {
        this.bedId = bedId;
        this.room = room;
        this.state = BedState.AVAILABLE;
    }

    public String getBedId() { return bedId; }
    public BedState getState() { return state; }
    public void setState(BedState state) { this.state = state; }
    public Room getRoom() { return room; }
    public boolean isAvailable() { return state == BedState.AVAILABLE; }
    public String getCategory() { return room.getCategory(); }
    public String getDepartmentName() { return room.getDepartment().getName(); }
    public String getSpecialism() { return room.getSpecialism(); }
    public String getRoomName() { return room.getRoomName(); }

    @Override
    public String toString() {
        return bedId + " [" + state + "] in " + room.getRoomName();
    }
}
