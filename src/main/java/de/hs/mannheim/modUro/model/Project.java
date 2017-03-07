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
import de.hs.mannheim.modUro.config.RegEx;
import de.hs.mannheim.modUro.config.ToolboxLogger;
import de.hs.mannheim.modUro.model.dialog.ProjectSetting;

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

    private List<ModuroModel> moduroModelList;
    private ProjectSetting projectSetting;
    private FilterOption filterOption;
    private List<File> allDirs;
    private List<String> modelTypeNames;

    /**
     * Create a project based on a setting file.
     *
     * @param projectSetting
     */
    public Project(ProjectSetting projectSetting, FilterOption filterOption) {
        this.projectSetting = projectSetting;
        this.filterOption = filterOption;
        modelTypeNames = createAllModelTypeNames();
        moduroModelList = createModels();
    }

    /**
     * @return Name of the project.
     */
    public String getName() {
        return projectSetting.getName();
    }

    /**
     * @return List of nodes for this project.
     */
    public List<Node> getNodes() {
        return projectSetting.getNode();
    }

    /**
     * @return A list of all models part of this project.
     */
    public List<ModuroModel> getModuroModelList() {
        return moduroModelList;
    }

    /**
     * Creates List of ModelTypes.
     *
     * @return
     */
    private List<ModuroModel> createModels() {
        ToolboxLogger.log.config("Building models ...");
        List<ModuroModel> moduroModelList = new ArrayList<>();
        List<File> dirList = new ArrayList<>();

        for (String name : modelTypeNames) {
            for (File file : allDirs) {
                if (file.getName().startsWith(name + "_")) {
                    dirList.add(file);
                }
            }
            ModuroModel moduroModel = new ModuroModel(dirList, filterOption);
            if (!moduroModel.getSimulations().isEmpty()) {
                moduroModelList.add(moduroModel);
            }
            dirList.clear();
        }
        ToolboxLogger.log.config("Created " + moduroModelList.size() + " models.");
        return moduroModelList;
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

        for (Node node : projectSetting.getNode()) {
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