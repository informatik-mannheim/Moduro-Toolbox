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

import de.hs.mannheim.modUro.config.RegEx;
import de.hs.mannheim.modUro.model.ModelType;
import de.hs.mannheim.modUro.model.Node;
import de.hs.mannheim.modUro.model.Project;
import de.hs.mannheim.modUro.model.dialog.SettingFile;

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

        List<String> modelTypeNames = getAllModelTypeName();
        System.out.println(String.format("Found %d modelTypeNames", modelTypeNames.size()));
        List<File> nodeDirectories = listAllDirInAllNodes();
        System.out.println(String.format("Found %d nodeDirs", nodeDirectories.size()));
        List<File> dirsPerModelTypeName;

        for (String modelTypeName : modelTypeNames) {
             dirsPerModelTypeName = new ArrayList<>();
            System.out.println(String.format("Loading directories starting with %s", modelTypeName));
            for (File nodeDir : nodeDirectories) {
                if(!nodeDir.isDirectory()){
                    System.out.println(nodeDir.getName() + " is not a directory. Only directories can be added to a modelType sublist.");
                    continue;
                }

                String dirName = nodeDir.getName();
                if (dirName.startsWith(modelTypeName)) {
                    System.out.println(String.format(
                            "Adding directory %s for modelTypeName %s", dirName,modelTypeName));
                    dirsPerModelTypeName.add(nodeDir);
                }
            }

            modelTypeCreator.setFileList(dirsPerModelTypeName);
            modelTypeCreator.createModelType();
            modelType = modelTypeCreator.getModelType();
            modelTypeList.add(modelType);
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
                // todo: quite happy path
                System.out.println(String.format("Trying to append file %s to modelTypeNameList.", file.getName()));
                // todo: at the moment I face trouble with this method. The output data of my instance of CompuCell3D is of
                // todo: a different name scheme, which does not the value referenced in the "RegEx"-Class.
                // todo: This will crash the programm on startup

                // todo:  e.g.: We place an empty dir in the node folder, the filename is "I_M_broken";
                // todo: This will be added as a modelTypeName, even though there are no valid files in the dir.

                // todo: 2nd scena We place a valid set of data in the directory. But this time we change the name
                // todo: to SpaCdbCdiInDa.cc3d-2016-08-23-11-03-29-425000 -> this will cause an exception + crash

                // This is because the programm will later compare the count of available directories in the node-dir
                // and the count of directories it was able to convert into a name. If the count doesn't match
                // the programm will crash. Feel free to improve.

                String[] tokenValue = file.getName().split(RegEx.Model_TYPE_NAME_SUFFIX_UNDERSCORE.getName());
                System.out.println("ModelType name is: "+ tokenValue[0]);
                String name = tokenValue[0];

                if (!modelTypeNameList.contains(name) && name.length() > 0) {
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
