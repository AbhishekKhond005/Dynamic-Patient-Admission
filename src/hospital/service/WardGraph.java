package hospital.service;

import hospital.model.Bed;
import hospital.model.BedState;
import hospital.model.Room;

import java.util.*;

public class WardGraph {

    private final Map<Bed, Set<Bed>> adjacencyMap;

    public WardGraph() {
        this.adjacencyMap = new HashMap<>();
    }

    public void addEdge(Bed bed1, Bed bed2) {
        adjacencyMap.computeIfAbsent(bed1, k -> new HashSet<>()).add(bed2);
        adjacencyMap.computeIfAbsent(bed2, k -> new HashSet<>()).add(bed1);
    }

    public void addBidirectionalEdges(Room room) {
        List<Bed> beds = room.getBeds();
        for (int i = 0; i < beds.size(); i++) {
            for (int j = i + 1; j < beds.size(); j++) {
                addEdge(beds.get(i), beds.get(j));
            }
        }
    }

    public void addCrossRoomEdge(Bed bed1, Bed bed2) {
        addEdge(bed1, bed2);
    }

    public Set<Bed> getNeighbors(Bed bed) {
        return adjacencyMap.getOrDefault(bed, Collections.emptySet());
    }

    public boolean hasEdge(Bed bed1, Bed bed2) {
        return adjacencyMap.containsKey(bed1) && adjacencyMap.get(bed1).contains(bed2);
    }

    public int degree(Bed bed) {
        return getNeighbors(bed).size();
    }

    public Bed findNearestAvailable(Bed source) {
        if (source.isAvailable()) return source;

        Queue<Bed> queue = new LinkedList<>();
        Set<Bed> visited = new HashSet<>();
        Map<Bed, Integer> distance = new HashMap<>();

        queue.add(source);
        visited.add(source);
        distance.put(source, 0);

        while (!queue.isEmpty()) {
            Bed current = queue.poll();
            int currentDist = distance.get(current);

            if (current != source && current.isAvailable()) {
                return current;
            }

            for (Bed neighbor : getNeighbors(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    distance.put(neighbor, currentDist + 1);
                    queue.add(neighbor);
                }
            }
        }
        return null;
    }

    public Set<Bed> getQuarantineZone(Bed source, int radius) {
        Set<Bed> zone = new HashSet<>();
        Queue<Bed> queue = new LinkedList<>();
        Set<Bed> visited = new HashSet<>();
        Map<Bed, Integer> distance = new HashMap<>();

        queue.add(source);
        visited.add(source);
        distance.put(source, 0);

        while (!queue.isEmpty()) {
            Bed current = queue.poll();
            int currentDist = distance.get(current);

            if (currentDist > radius) break;

            if (current != source) zone.add(current);

            if (currentDist < radius) {
                for (Bed neighbor : getNeighbors(current)) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        distance.put(neighbor, currentDist + 1);
                        queue.add(neighbor);
                    }
                }
            }
        }
        return zone;
    }

    public List<Bed> findShortestPath(Bed from, Bed to) {
        Queue<Bed> queue = new LinkedList<>();
        Map<Bed, Bed> predecessor = new HashMap<>();
        Set<Bed> visited = new HashSet<>();

        queue.add(from);
        visited.add(from);
        predecessor.put(from, null);

        while (!queue.isEmpty()) {
            Bed current = queue.poll();
            if (current.equals(to)) {
                return reconstructPath(predecessor, to);
            }
            for (Bed neighbor : getNeighbors(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    predecessor.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }
        return Collections.emptyList();
    }

    private List<Bed> reconstructPath(Map<Bed, Bed> predecessor, Bed target) {
        LinkedList<Bed> path = new LinkedList<>();
        Bed current = target;
        while (current != null) {
            path.addFirst(current);
            current = predecessor.get(current);
        }
        return path;
    }

    public Map<Bed, Set<Bed>> getAdjacencyMap() {
        return Collections.unmodifiableMap(adjacencyMap);
    }

    public Set<Bed> getConnectedComponent(Bed start) {
        Set<Bed> component = new HashSet<>();
        Queue<Bed> queue = new LinkedList<>();
        queue.add(start);
        component.add(start);
        while (!queue.isEmpty()) {
            Bed current = queue.poll();
            for (Bed neighbor : getNeighbors(current)) {
                if (!component.contains(neighbor)) {
                    component.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        return component;
    }

    public Map<BedState, List<Bed>> getBedsByState() {
        Map<BedState, List<Bed>> grouped = new HashMap<>();
        for (Bed bed : adjacencyMap.keySet()) {
            grouped.computeIfAbsent(bed.getState(), k -> new ArrayList<>()).add(bed);
        }
        return grouped;
    }
}
