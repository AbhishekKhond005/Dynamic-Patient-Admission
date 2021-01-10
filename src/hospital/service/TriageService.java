package hospital.service;

import hospital.model.Patient;
import hospital.model.TriagePriority;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.PriorityQueue;

public class TriageService {

    private final PriorityQueue<TriageEntry> triageQueue;

    public static class TriageEntry {
        private final Patient patient;
        private final TriagePriority priority;
        private final LocalDateTime arrivalTime;

        public TriageEntry(Patient patient, TriagePriority priority) {
            this.patient = patient;
            this.priority = priority;
            this.arrivalTime = LocalDateTime.now();
        }

        public Patient getPatient() { return patient; }
        public TriagePriority getPriority() { return priority; }
        public LocalDateTime getArrivalTime() { return arrivalTime; }

        @Override
        public String toString() {
            return patient.getPatientName() + " [" + priority.name() + "] @" + arrivalTime;
        }
    }

    public TriageService() {
        this.triageQueue = new PriorityQueue<>(
            Comparator.comparingInt((TriageEntry e) -> e.getPriority().getLevel())
                      .thenComparing(TriageEntry::getArrivalTime)
        );
    }

    public void enqueue(Patient patient, TriagePriority priority) {
        triageQueue.offer(new TriageEntry(patient, priority));
    }

    public TriageEntry dequeue() {
        return triageQueue.poll();
    }

    public TriageEntry peek() {
        return triageQueue.peek();
    }

    public boolean isEmpty() {
        return triageQueue.isEmpty();
    }

    public int size() {
        return triageQueue.size();
    }

    public boolean hasUrgentPatient() {
        for (TriageEntry e : triageQueue) {
            if (e.getPriority() == TriagePriority.RESUSCITATION ||
                e.getPriority() == TriagePriority.EMERGENCY) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        triageQueue.clear();
    }

    public PriorityQueue<TriageEntry> getQueue() {
        return new PriorityQueue<>(triageQueue);
    }

    public int countByPriority(TriagePriority priority) {
        int count = 0;
        for (TriageEntry e : triageQueue) {
            if (e.getPriority() == priority) count++;
        }
        return count;
    }
}
