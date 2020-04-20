package hospital;

import hospital.data.DataStore;
import hospital.data.HospitalDataLoader;
import hospital.gui.PatientFormFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        HospitalDataLoader.load();
        DataStore.getInstance().buildRoomCategoryList();

        SwingUtilities.invokeLater(PatientFormFrame::new);
    }
}
