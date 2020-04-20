package hospital.data;

import hospital.model.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class HospitalDataLoader {

    private HospitalDataLoader() {}

    public static void load() {
        DataStore store = DataStore.getInstance();
        Map<String, Department> deptMap = new HashMap<>();
        Map<String, Room> roomMap = new HashMap<>();

        try (FileInputStream file = new FileInputStream(new File("hospital_data.xlsx"))) {
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);

            boolean firstRow = true;
            for (Row row : sheet) {
                if (firstRow) { firstRow = false; continue; }

                int deptId = (int) row.getCell(0).getNumericCellValue();
                String deptName = row.getCell(1).getStringCellValue();
                String specialism = row.getCell(2).getStringCellValue();
                String roomName = row.getCell(3).getStringCellValue();
                String roomCategory = row.getCell(4).getStringCellValue();
                String bedName = row.getCell(5).getStringCellValue();

                Department dept = deptMap.get(deptName);
                if (dept == null) {
                    dept = new Department(deptName, deptId);
                    deptMap.put(deptName, dept);
                    store.addDepartment(dept);
                }

                String roomKey = deptName + "::" + roomName;
                Room room = roomMap.get(roomKey);
                if (room == null) {
                    room = new Room(roomName, roomCategory, specialism, dept);
                    roomMap.put(roomKey, room);
                    store.addRoom(room);
                    dept.addRoom(room);
                }

                Bed bed = new Bed(bedName, room);
                store.addBed(bed);
                room.addBed(bed);
                store.registerSpecialism(deptName, specialism);
            }

            store.buildRoomCategoryList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
