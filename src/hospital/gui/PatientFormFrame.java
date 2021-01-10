package hospital.gui;

import hospital.data.DataStore;
import hospital.model.*;
import hospital.service.TriageService;
import hospital.service.TriageService.TriageEntry;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PatientFormFrame {

    private JFrame frame;
    private JPanel panel;
    private JLabel nameLabel, ageLabel, genderLabel, roomLabel, departmentLabel, specialismLabel, dPatientLabel;
    private JTextField nameField, ageField, departPatientField;
    private JComboBox<String> genderBox, roomBox, departmentBox, specialismBox, triageBox;
    private JButton submitBtn, findBedBtn, saveBtn, departBtn;

    private DataStore store;
    private TriageService triageService;

    public PatientFormFrame() {
        store = DataStore.getInstance();
        triageService = new TriageService();
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Dynamic Patient Admission");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 650);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(new Color(65, 65, 65));

        panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(65, 65, 65));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 40, 10, 40);

        Font labelFont = new Font("Poppins", Font.BOLD, 16);
        Font buttonFont = new Font("Poppins", Font.PLAIN, 16);
        Font comboFont = new Font("Poppins", Font.PLAIN, 16);
        Color fieldBg = new Color(78, 78, 78);
        Color accent = new Color(220, 140, 96);

        nameLabel = createLabel("Name:", labelFont);
        c.gridx = 0; c.gridy = 0; c.weightx = 1.0;
        panel.add(nameLabel, c);

        nameField = createField(comboFont, fieldBg);
        c.gridx = 0; c.gridy = 1; c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(nameField, c);

        ageLabel = createLabel("Age:", labelFont);
        c.gridx = 1; c.gridy = 0; c.weightx = 1.0;
        panel.add(ageLabel, c);

        ageField = createField(buttonFont, fieldBg);
        c.gridx = 1; c.gridy = 1; c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(ageField, c);

        genderLabel = createLabel("Gender:", labelFont);
        c.gridx = 0; c.gridy = 2;
        panel.add(genderLabel, c);

        genderBox = new JComboBox<>(new String[]{"Male", "Female"});
        styleCombo(genderBox, comboFont, fieldBg);
        c.gridx = 0; c.gridy = 3;
        panel.add(genderBox, c);

        departmentLabel = createLabel("Department:", labelFont);
        c.gridx = 0; c.gridy = 4;
        panel.add(departmentLabel, c);

        String[] deptNames = store.getDepartmentNames().toArray(new String[0]);
        departmentBox = new JComboBox<>(deptNames);
        styleCombo(departmentBox, comboFont, fieldBg);
        c.gridx = 0; c.gridy = 5;
        panel.add(departmentBox, c);

        specialismLabel = createLabel("Specialism:", labelFont);
        c.gridx = 1; c.gridy = 4;
        panel.add(specialismLabel, c);

        specialismBox = new JComboBox<>();
        styleCombo(specialismBox, comboFont, fieldBg);
        c.gridx = 1; c.gridy = 5;
        panel.add(specialismBox, c);

        roomLabel = createLabel("Preferred Room:", labelFont);
        c.gridx = 1; c.gridy = 2;
        panel.add(roomLabel, c);

        JLabel triageLabel = createLabel("Triage Priority:", labelFont);
        c.gridx = 1; c.gridy = 6;
        panel.add(triageLabel, c);

        triageBox = new JComboBox<>(new String[]{
            "RESUSCITATION (Immediate)",
            "EMERGENCY (Potential threat)",
            "URGENT (Serious)",
            "SEMI_URGENT (Minor)",
            "NON_URGENT (Routine)"
        });
        styleCombo(triageBox, comboFont, fieldBg);
        c.gridx = 1; c.gridy = 7;
        panel.add(triageBox, c);

        String[] rooms = store.getRoomCategories().toArray(new String[0]);
        roomBox = new JComboBox<>(rooms);
        styleCombo(roomBox, comboFont, fieldBg);
        c.gridx = 1; c.gridy = 3;
        panel.add(roomBox, c);

        departmentBox.addActionListener(e -> {
            String selected = (String) departmentBox.getSelectedItem();
            if (selected == null) return;
            specialismBox.removeAllItems();
            for (String s : store.getSpecialismsForDept(selected)) {
                specialismBox.addItem(s);
            }
        });
        if (deptNames.length > 0) {
            departmentBox.setSelectedIndex(0);
        }

        submitBtn = new JButton("Submit");
        submitBtn.setFont(buttonFont);
        submitBtn.setBackground(accent);
        submitBtn.setForeground(Color.WHITE);
        submitBtn.addActionListener(e -> submitPatient());
        c.gridx = 0; c.gridy = 8; c.gridwidth = 2; c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(submitBtn, c);

        findBedBtn = new JButton("Find Bed");
        findBedBtn.setFont(buttonFont);
        findBedBtn.setBackground(accent);
        findBedBtn.setForeground(Color.WHITE);
        findBedBtn.addActionListener(e -> assignBed());
        c.gridx = 0; c.gridy = 9; c.gridwidth = 2;
        panel.add(findBedBtn, c);

        saveBtn = new JButton("Save");
        saveBtn.setFont(buttonFont);
        saveBtn.setBackground(accent);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> savePatientDetails());
        c.gridx = 0; c.gridy = 10; c.gridwidth = 2;
        panel.add(saveBtn, c);

        dPatientLabel = createLabel("Depart Patient:", labelFont);
        c.gridx = 0; c.gridy = 11; c.gridwidth = 1;
        panel.add(dPatientLabel, c);

        departPatientField = createField(buttonFont, fieldBg);
        c.gridx = 0; c.gridy = 12; c.gridwidth = 2; c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(departPatientField, c);

        departBtn = new JButton("Depart");
        departBtn.setFont(buttonFont);
        departBtn.setBackground(accent);
        departBtn.setForeground(Color.WHITE);
        departBtn.addActionListener(e -> departPatient());
        c.gridx = 0; c.gridy = 13; c.gridwidth = 2;
        panel.add(departBtn, c);

        frame.add(panel);
        frame.setVisible(true);
    }

    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(Color.WHITE);
        return label;
    }

    private JTextField createField(Font font, Color bg) {
        JTextField field = new JTextField(50);
        field.setFont(font);
        field.setForeground(Color.WHITE);
        field.setBackground(bg);
        return field;
    }

    private void styleCombo(JComboBox<String> box, Font font, Color bg) {
        box.setFont(font);
        box.setForeground(Color.WHITE);
        box.setBackground(bg);
    }

    private void submitPatient() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) { JOptionPane.showMessageDialog(frame, "Enter patient name"); return; }
        int age;
        try { age = Integer.parseInt(ageField.getText().trim()); }
        catch (NumberFormatException e) { JOptionPane.showMessageDialog(frame, "Enter valid age"); return; }

        String gender = (String) genderBox.getSelectedItem();
        String dept = (String) departmentBox.getSelectedItem();
        String spec = (String) specialismBox.getSelectedItem();
        String roomCat = (String) roomBox.getSelectedItem();

        Patient p = new Patient(name, age, gender, dept, roomCat, spec);
        int triageIdx = triageBox.getSelectedIndex();
        TriagePriority priority = TriagePriority.values()[triageIdx];
        p.setTriagePriority(priority);
        store.addToPatientQueue(p);
        triageService.enqueue(p, priority);
        JOptionPane.showMessageDialog(frame, "Patient added: " + name + " [" + priority.name() + "]");
        nameField.setText("");
        ageField.setText("");
    }

    private void assignBed() {
        TriageEntry entry = triageService.dequeue();
        if (entry == null) { JOptionPane.showMessageDialog(frame, "No patients in queue"); return; }
        Patient p = entry.getPatient();
        store.getPatientQueue().remove(p);

        java.util.List<Bed> candidates = store.getAvailableBedsFor(
                p.getDepartmentNeeded(), p.getSpecialismNeeded(), p.getPreferredRoomCategory());

        if (candidates.isEmpty()) {
            candidates = store.getAvailableBedsFor(
                    p.getDepartmentNeeded(), p.getSpecialismNeeded(), null);
        }
        if (candidates.isEmpty()) {
            candidates = store.getAvailableBedsFor(
                    p.getDepartmentNeeded(), null, null);
        }
        if (candidates.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No available beds for " + p.getPatientName());
            store.addToPatientQueue(p);
            return;
        }

        Bed chosen = candidates.get(0);
        store.assignBed(p, chosen);
        JOptionPane.showMessageDialog(frame, "Assigned " + chosen.getBedId() + " in "
                + chosen.getRoomName() + " (" + chosen.getDepartmentName() + ")");
    }

    private void savePatientDetails() {
        String filename = "PatientDetails.xlsx";
        LocalDateTime now = LocalDateTime.now();
        String dateStr = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String timeStr = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        try {
            File file = new File(filename);
            XSSFWorkbook workbook;
            XSSFSheet sheet;

            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                workbook = new XSSFWorkbook(fis);
                sheet = workbook.getSheetAt(0);
            } else {
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet("Patient Details");
                Row header = sheet.createRow(0);
                header.createCell(0).setCellValue("Name");
                header.createCell(1).setCellValue("Age");
                header.createCell(2).setCellValue("Department");
                header.createCell(3).setCellValue("Specialism");
                header.createCell(4).setCellValue("Room Category");
                header.createCell(5).setCellValue("Room Name");
                header.createCell(6).setCellValue("Bed Name");
                header.createCell(7).setCellValue("Entry Date");
                header.createCell(8).setCellValue("Entry Time");
                header.createCell(9).setCellValue("Depart Date");
                header.createCell(10).setCellValue("Depart Time");
            }

            int rowNum = sheet.getLastRowNum() + 1;
            for (java.util.Map.Entry<Patient, Bed> e : store.getAssignmentMap().entrySet()) {
                Patient p = e.getKey();
                Bed b = e.getValue();

                boolean exists = false;
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row r = sheet.getRow(i);
                    if (r.getCell(0).getStringCellValue().equals(p.getPatientName())) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(p.getPatientName());
                    row.createCell(1).setCellValue(p.getAge());
                    row.createCell(2).setCellValue(p.getDepartmentNeeded());
                    row.createCell(3).setCellValue(p.getSpecialismNeeded());
                    row.createCell(4).setCellValue(p.getPreferredRoomCategory());
                    row.createCell(5).setCellValue(b.getRoomName());
                    row.createCell(6).setCellValue(b.getBedId());
                    row.createCell(7).setCellValue(dateStr);
                    row.createCell(8).setCellValue(timeStr);
                }
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
            workbook.close();
            JOptionPane.showMessageDialog(frame, "Patient details saved to Excel");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error saving: " + ex.getMessage());
        }
    }

    private void departPatient() {
        String name = departPatientField.getText().trim();
        if (name.isEmpty()) { JOptionPane.showMessageDialog(frame, "Enter patient name to depart"); return; }

        Bed bed = store.findPatientBed(name);
        if (bed == null) { JOptionPane.showMessageDialog(frame, "Patient not found"); return; }

        store.freeBedByPatientName(name);
        LocalDateTime now = LocalDateTime.now();
        String dateStr = now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String timeStr = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        String filename = "PatientDetails.xlsx";
        try {
            File file = new File(filename);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(filename);
                Workbook wb = WorkbookFactory.create(fis);
                org.apache.poi.ss.usermodel.Sheet s = wb.getSheetAt(0);
                for (Row r : s) {
                    if (r.getCell(0).getStringCellValue().equals(name)) {
                        if (r.getCell(9) == null) r.createCell(9);
                        r.getCell(9).setCellValue(dateStr);
                        if (r.getCell(10) == null) r.createCell(10);
                        r.getCell(10).setCellValue(timeStr);
                        break;
                    }
                }
                try (FileOutputStream fos = new FileOutputStream(filename)) {
                    wb.write(fos);
                }
                wb.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        JOptionPane.showMessageDialog(frame, "Patient " + name + " departed. Bed freed.");
        departPatientField.setText("");
    }
}
