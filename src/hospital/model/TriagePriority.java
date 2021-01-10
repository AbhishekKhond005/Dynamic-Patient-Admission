package hospital.model;

public enum TriagePriority implements Comparable<TriagePriority> {
    RESUSCITATION(1, "Immediate life-threatening"),
    EMERGENCY(2, "Potential life threat"),
    URGENT(3, "Serious but stable"),
    SEMI_URGENT(4, "Minor condition"),
    NON_URGENT(5, "Routine checkup");

    private final int level;
    private final String description;

    TriagePriority(int level, String description) {
        this.level = level;
        this.description = description;
    }

    public int getLevel() { return level; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return name() + " (" + description + ")";
    }
}
