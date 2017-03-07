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

import de.hs.mannheim.modUro.config.FilterOption;
import de.hs.mannheim.modUro.config.ToolboxParameter;
import de.hs.mannheim.modUro.model.dialog.ProjectSetting;
import de.hs.mannheim.modUro.model.dialog.SettingFileMarshaller;
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
    private final File settingsXMLFile;

    //List with project settings loaded from the xml file.
    private List<ProjectSetting> projectSettings = new ArrayList<>();

    public MainModel(String settingFilePath) {
        this(settingFilePath, new FilterOption());
    }

    /**
     * Constructor for MainModel.
     */
    public MainModel(String settingFilePath, FilterOption filterOption) {
        this.settingFilePath = settingFilePath;
        // this.settingURL = getClass().getResource(settingFilePath);
        this.settingsXMLFile = new File(settingFilePath);

        loadXMLSettingsFile(); // Load Setting File with Project and its nodes.

        //For all Settings in XML file create Project with its data.
        for (ProjectSetting projectSettingItem : projectSettings) {
            if (projectSettingItem.getNode() != null) {
                Project project = new Project(projectSettingItem, filterOption);
                projectData.add(project);
            }
        }
    }

    /**
     * Loads node data from the specified xml file.
     */
    private void loadXMLSettingsFile() {
        try {
            JAXBContext context = JAXBContext.newInstance(SettingFileMarshaller.class);
            Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the file and unmarshalling.
            SettingFileMarshaller wrapper = (SettingFileMarshaller) um.unmarshal(settingsXMLFile);

            ToolboxParameter.params = wrapper.getParameter();
            projectSettings.clear();
            projectSettings.addAll(wrapper.getProject());

            // Save the file path to the registry.
            //setPersonFilePath(file);

        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load settings file.");
            alert.setContentText("Could not load settings file:\n" + settingsXMLFile.getPath());
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