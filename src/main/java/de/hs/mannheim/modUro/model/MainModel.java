package de.hs.mannheim.modUro.model;

import de.hs.mannheim.modUro.creator.ProjectCreator;
import de.hs.mannheim.modUro.model.dialog.SettingFile;
import de.hs.mannheim.modUro.model.dialog.SettingFileWrapper;
import javafx.scene.control.Alert;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Class of the Main Model with Project Data.
 * @author  Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class MainModel {

    //List of Projects
    private List<Project> projectData = new ArrayList<Project>();

    //Creator for create Projects.
    private ProjectCreator projectCreator = new ProjectCreator();

    //Setting.xml file with projects and its nodes.
    String settingFilePath;
    private final URL settingURL;
    private final File SETTINGXML;

    //List with settings loaded from the Setting.xml file.
    private List<SettingFile> settings = new ArrayList<>();

    /**
     * Constructor for MainModel.
     */
    public MainModel(String settingFilePath){
        this.settingFilePath = settingFilePath;
        this.settingURL = getClass().getResource(settingFilePath);
        this.SETTINGXML = new File(settingURL.getPath());

        loadNodeDataFromFile(); // Load Setting File with Project and its nodes.

        //For all Settings in XML file create Project with its data.
        for (SettingFile settingFileItem: settings) {
            if(settingFileItem.getNode() != null) {
                projectCreator.setSettingFile(settingFileItem);
                projectCreator.createProject();
                projectData.add(projectCreator.getProject());
            }
        }
    }

    /**
     * Loads node data from the specified xml file.
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

    /**
     * Gets list with project Data.
     * @return
     */
    public List<Project> getProjectData() {
        return projectData;
    }

    public ProjectCreator getProjectCreator() {
        return projectCreator;
    }
}