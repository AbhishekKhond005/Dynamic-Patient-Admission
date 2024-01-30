# Dynamic Patient Admission

A Java-based desktop application for real-time hospital bed management, patient triage, and occupancy tracking. Designed to address bed allocation challenges in resource-limited settings, inspired by the study *"An evaluation of bed management in a rural hospital adjacent to Indo-Nepal border in West Bengal"*.

---

## Context

Rural hospitals near the Indo-Nepal border face unique bed management challenges:
- **Limited bed capacity** with seasonal patient surges
- **Mixed emergency/elective** patient flow requiring triage-based allocation
- **Cross-border patient movement** adding unpredictability
- **Manual bed tracking** leading to inefficiencies and delays

This system provides a digital decision-support tool for real-time allocation, prioritisation, and occupancy monitoring in such constraint-heavy environments.

---

## Data Structure Demonstrations

| Data Structure | Usage | File |
|---|---|---|
| **N-ary Tree** | Department -> Room -> Bed hierarchy with DFS/BFS traversal | `HospitalTree.java` |
| **PriorityQueue** | Patient triage queue ordered by severity + arrival time | `TriageService.java` |
| **Graph (Adjacency List)** | Bed-to-bed spatial relationships with BFS nearest-search | `WardGraph.java` |
| **HashMap (Multi-Index)** | O(1) lookups by department, category, specialism, state | `BedIndex.java` |
| **Strategy Pattern** | Pluggable FirstFit / BestFit bed assignment algorithms | `AssignmentStrategy.java` |

---

## Features
- **Triage-based patient queue** – PriorityQueue orders patients by severity, then FIFO.
- **Tree-structured facility map** – N-ary tree models hospital hierarchy for roll-up bed counts.
- **Bed adjacency graph** – BFS finds nearest available bed; quarantine zone detection by radius.
- **Multi-index bed lookup** – HashMap-based indexes for instant cross-dimension queries.
- **Plug-in assignment strategies** – FirstFit (exact match) and BestFit (scored) algorithms.
- **Patient lifecycle tracking** – Admission/discharge timestamps and stay duration.
- **Excel persistence** – Apache POI reads `hospital_data.xlsx` and writes `PatientDetails.xlsx`.

---

## Tech Stack
- **Language:** Java 8+
- **GUI:** Swing
- **Data Storage:** Excel (via Apache POI)
- **Data Structures:** Tree, PriorityQueue, Graph, HashMap, Enum

---

## Getting Started

### Prerequisites
- Java 8 or higher
- Apache POI libraries (ooxml) on classpath
- Excel files (`hospital_data.xlsx`, `PatientDetails.xlsx`) in project root

### Running
1. Open in IntelliJ IDEA / Eclipse.
2. Ensure `D:/JAVA/JAVA POI/*.jar` on classpath (or copy jars to `lib/`).
3. Compile and run `src/hospital/Main.java`.
4. Or use the pre-built JAR in `JAR/` directory.

### Data Format
`hospital_data.xlsx` columns: `DeptID | DeptName | Specialism | RoomName | RoomCategory | BedName`

---

## Commits Timeline

```
2020-04-20  Package restructure (model, data, gui)
2020-08-15  N-ary tree hierarchy
2021-01-10  PriorityQueue triage system
2021-06-25  Bed adjacency graph with BFS
2022-02-14  HashMap indexing layer
2022-09-01  Strategy pattern for assignment
2023-04-18  Patient lifecycle & departure
2024-01-30  Rural hospital context & docs
```
