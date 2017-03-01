/*
Copyright 2016 the original author or authors.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package de.hs.mannheim.modUro.model;

import de.hs.mannheim.modUro.model.dialog.SettingFile;
import de.hs.mannheim.modUro.model.dialog.SettingFileWrapper;
import javafx.scene.control.Alert;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Class of the Main Model with Project Data.
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class MainModel {

    //List of Projects
    private List<Project> projectData = new ArrayList<Project>();

    //Setting.xml file with projects and its nodes.
    String settingFilePath;
    // private final URL settingURL;
    private final File SETTINGXML;

    //List with settings loaded from the Setting.xml file.
    private List<SettingFile> settings = new ArrayList<>();

    /**
     * Constructor for MainModel.
     */
    public MainModel(String settingFilePath) {
        this.settingFilePath = settingFilePath;
        // this.settingURL = getClass().getResource(settingFilePath);
        this.SETTINGXML = new File(settingFilePath);

        loadNodeDataFromFile(); // Load Setting File with Project and its nodes.

        //For all Settings in XML file create Project with its data.
        for (SettingFile settingFileItem : settings) {
            if (settingFileItem.getNode() != null) {
                Project project = new Project(settingFileItem);
                // projectCreator.setSettingFile(settingFileItem);
                // projectCreator.createProject();
                projectData.add(project);
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
     *
     * @return
     */
    public List<Project> getProjectData() {
        return projectData;
    }

}