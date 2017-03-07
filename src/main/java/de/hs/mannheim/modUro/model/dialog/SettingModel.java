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
package de.hs.mannheim.modUro.model.dialog;

import javafx.scene.control.Alert;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class SettingModel {

    // TODO: absolute path name!
    private final File SETTINGXML = new File("C:\\Users\\m.pathmanathan\\Desktop\\ImplConfig\\Setting.xml");

    private List<ProjectSetting> settings;

    public SettingModel() {
        settings = new ArrayList<>();

        // loadNodeDataFromFile();
    }

    /**
     * Loads setting data from the specified file.
     */
    /*
    private void loadNodeDataFromFile() {
        try {
            JAXBContext context = JAXBContext.newInstance(SettingFileMarshaller.class);
            Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the file and unmarshalling.
            SettingFileMarshaller wrapper = (SettingFileMarshaller) um.unmarshal(SETTINGXML);

            settings.clear();
            settings.addAll(wrapper.getProjects());

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
    */
    public List<ProjectSetting> getSettings() {
        return settings;
    }

    /**
     * Save setting data in the specified file.
     */
    private void saveNodeDataToFile() {
        try {
            JAXBContext context = JAXBContext.newInstance(SettingFileMarshaller.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping our setting data.
            SettingFileMarshaller wrapper = new SettingFileMarshaller();
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
