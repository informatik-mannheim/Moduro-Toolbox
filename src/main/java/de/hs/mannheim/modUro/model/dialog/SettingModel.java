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

        /*
        * Node node = new Node("Node1", new File("test"));
        Node node2 = new Node("Node2", new File("test2"));
        Node node3 = new Node("Node3", new File("test3"));

        List<Node> nodelist = new ArrayList<>();
        nodelist.add(0, node);
        nodelist.add(1, node2);
        nodelist.add(2, node3);

        SettingFile s1 = new SettingFile();
        s1.setName("Granada");
        s1.setNode(nodelist);

        SettingFile s2 = new SettingFile();
        s2.setName("KrebsProjekt");
        s2.setNodeName("Laufwerk");
        s2.setPath("C:\\Mathu\\Laufwerk");


        SettingFile s3 = new SettingFile();
        s3.setName("Granada");
        s3.setNodeName("Laufwerk");
        s3.setPath("C:\\Mathu\\Laufwerk");

        settings.add(0, s1);
        settings.add(1,s2);
        settings.add(2,s3);

        saveNodeDataToFile();
        * */
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
