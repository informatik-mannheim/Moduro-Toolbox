package de.hs.mannheim.modUro.model.dialog;

import javafx.scene.control.Alert;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by m.pathmanathan on 18.12.2015.
 */
public class SettingModel {

    private final File SETTINGXML = new File("C:\\Users\\m.pathmanathan\\Desktop\\ImplConfig\\Setting.xml");

    private List<SettingFile> settings;

    public SettingModel() {
        settings = new ArrayList<>();

        loadNodeDataFromFile();
    }

    /**
     * Loads setting data from the specified file.
     */
    private void loadNodeDataFromFile() {
        try {
            JAXBContext context = JAXBContext.newInstance(SettingFileWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the file and unmarshalling.
            SettingFileWrapper wrapper = (SettingFileWrapper) um.unmarshal(SETTINGXML);

            settings.clear();
            settings.addAll(wrapper.getProject());

            // Save the file path to the registry.
            //setPersonFilePath(file);

        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + SETTINGXML.getPath());

            alert.showAndWait();
        }
    }
    public List<SettingFile> getSettings() {
        return settings;
    }

    /**
     * Save setting data in the specified file.
     */
    private void saveNodeDataToFile() {
        try {
            JAXBContext context = JAXBContext.newInstance(SettingFileWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping our setting data.
            SettingFileWrapper wrapper = new SettingFileWrapper();
            wrapper.setProject(settings);

            // Marshalling and saving XML to the file.
            m.marshal(wrapper, SETTINGXML);

            // Save the file path to the registry.
            // setPersonFilePath(nodeConfigXMLFile);

        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + SETTINGXML.getPath());

            alert.showAndWait();
        }
    }
}
