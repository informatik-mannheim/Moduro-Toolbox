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

import de.hs.mannheim.modUro.config.RegEx;
import de.hs.mannheim.modUro.config.ToolboxLogger;
import de.hs.mannheim.modUro.model.dialog.SettingFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class for a Project.
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 * @author Markus Gumbel (m.gumbel@hs-mannheim.de)
 */
public class Project {

    private List<ModelType> modelTypeList;
    private SettingFile settingFile;
    private List<File> allDirs;
    private List<String> modelTypeNames;

    /**
     * Create a project based on a setting file.
     * @param settingFile
     */
    public Project(SettingFile settingFile) {
        this.settingFile = settingFile;
        modelTypeNames = createAllModelTypeNames();
        modelTypeList = createModelTypeList();
    }

    /**
     * @return Name of the project.
     */
    public String getName() {
        return settingFile.getName();
    }

    /**
     *
     * @return List of nodes for this project.
     */
    public List<Node> getNodes() {
        return settingFile.getNode();
    }

    /**
     *
     * @return A list of all models part of this project.
     */
    public List<ModelType> getModelTypeList() {
        return modelTypeList;
    }

    /**
     * Creates List of ModelTypes.
     *
     * @return
     */
    private List<ModelType> createModelTypeList() {
        ToolboxLogger.log.config("Building metrics ...");
        List<ModelType> modelTypeList = new ArrayList<>();
        List<File> dirList = new ArrayList<>();

        for (String name : modelTypeNames) {
            for (File file : allDirs) {
                if (file.getName().startsWith(name + "_")) {
                    dirList.add(file);
                }
            }
            // Change here:
            ModelType modelType = new ModelType(dirList);
            modelTypeList.add(modelType);
            dirList.clear();
        }
        ToolboxLogger.log.config("Created " + modelTypeList.size() + " models.");
        return modelTypeList;
    }

    /**
     * Create List with modeltype name.
     *
     * @return
     */
    private List<String> createAllModelTypeNames() {
        ToolboxLogger.log.config("Scanning simulation directories...");
        int counter = 1;
        int stepSize = 25;
        List<String> modelTypeNameList = new ArrayList<>();
        allDirs = new ArrayList<>();

        for (Node node : settingFile.getNode()) {
            File[] dirs = node.getPath().listFiles(f -> f.isDirectory());
            for (File file : dirs) {
                allDirs.add(file);
                counter++;
                String name;
                String[] tokenValue = file.getName().split(RegEx.MODEL_TYPE_REG_EX.getName());
                name = tokenValue[0];

                if (!modelTypeNameList.contains(name) && name.length() > 0) {
                    modelTypeNameList.add(name);
                }
                if (counter % stepSize == 0) {
                    ToolboxLogger.log.config("Scanned " + counter + " files so far.");
                }
            }
        }
        ToolboxLogger.log.config("Found " + allDirs.size() + " directories and " +
                modelTypeNameList.size() + " models.");
        return modelTypeNameList;
    }
}