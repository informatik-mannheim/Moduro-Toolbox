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
package de.hs.mannheim.modUro.creator;

import de.hs.mannheim.modUro.model.ModelType;
import de.hs.mannheim.modUro.model.Node;
import de.hs.mannheim.modUro.model.Project;
import de.hs.mannheim.modUro.model.dialog.SettingFile;
import de.hs.mannheim.modUro.config.RegEx;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * ProjectCreator helps to create a Project by passing a SettingFile.
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class ProjectCreator {

    //SettingFile for a Project with Nodes
    SettingFile settingFile;

    //Project Instance
    private Project project;

    /**
     * Constructor.
     */
    public ProjectCreator() {
    }

    /**
     * Creates name of the project.
     *
     * @return
     */
    private String createProjectName() {
        String name = settingFile.getName();
        return name;
    }

    /**
     * Creates List of Nodes of a project.
     *
     * @return
     */
    private List<Node> createNodeList() {
        List<Node> nodeList = settingFile.getNode();

        return nodeList;
    }

    /**
     * Creates List of ModelTypes.
     *
     * @return
     */
    private List<ModelType> createModelTypeList() {
        ModelType modelType;
        List<ModelType> modelTypeList = new ArrayList<>();
        ModelTypeCreator modelTypeCreator = new ModelTypeCreator();

        List<String> modelTypeName = getAllModelTypeName();
        List<File> allDir = listAllDirInAllNodes();
        List<File> dirList = new ArrayList<>();

        for (String name : modelTypeName) {
            for (File file : allDir) {
                if (file.getName().contains(name)) {
                    dirList.add(file);
                }
            }
            modelTypeCreator.setFileList(dirList);
            modelTypeCreator.createModelType();
            modelType = modelTypeCreator.getModelType();
            modelTypeList.add(modelType);
            dirList.clear();
        }
        return modelTypeList;
    }

    /**
     * Lists all file in all nodes.
     *
     * @return
     */
    private List<File> listAllDirInAllNodes() {
        List<File> allDir = new ArrayList<>();

        for (Node node : settingFile.getNode()) {
            File[] dirs = node.getPath().listFiles(f -> f.isDirectory());
            for (File file : dirs) {
                allDir.add(file);
            }
        }

        return allDir;
    }

    /**
     * Create List with modeltype name.
     *
     * @return
     */
    private List<String> getAllModelTypeName() {
        List<String> modelTypeNameList = new ArrayList<>();

        for (Node node : settingFile.getNode()) {
            File[] dirs = node.getPath().listFiles(f -> f.isDirectory());
            for (File file : dirs) {
                String name;
                String[] tokenValue = file.getName().split(RegEx.MODEL_TYPE_REG_EX.getName());
                name = tokenValue[0];

                if (!modelTypeNameList.contains(name)) {
                    modelTypeNameList.add(name);
                }
            }
        }

        return modelTypeNameList;
    }

    public void createProject() {
        project = new Project(createProjectName(), createNodeList(), createModelTypeList());
    }

    public Project getProject() {
        return project;
    }

    public void setSettingFile(SettingFile settingFile) {
        this.settingFile = settingFile;
    }
}
