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

import de.hs.mannheim.modUro.config.FileName;
import de.hs.mannheim.modUro.model.ModelType;
import de.hs.mannheim.modUro.model.Simulation;
import de.hs.mannheim.modUro.config.RegEx;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * ModelTypeCreator helps to create a Modeltype by passing directories.
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class ModelTypeCreator {

    private final String INVALID_NAME_DEFAULT = "INVALID_NAME";
    //Input List of Dir of specific Model Type
    private List<File> fileList;

    //ModelType Instance
    private ModelType modelType;

    /**
     * Constructor.
     */
    public ModelTypeCreator() {
    }

    /**
     * Creates name of Modeltype.
     *
     * @return
     */
    private String createModelTypeName() {

        if (fileList == null || fileList.size() == 0) {
            System.out.println("Can't set modelTypeName because of missing fileList.");
            return INVALID_NAME_DEFAULT;
        }

        // todo: kann man hier nicht den ModelTypeName direkt setzen?
        return  fileList.get(0).getName().split(RegEx.Model_TYPE_NAME_SUFFIX_UNDERSCORE.getName())[0];
    }

    /**
     * Creates SimulationList of the modeltype.
     *
     * @return
     */
    private List<Simulation> createSimulationList() {
        List<Simulation> simulationList = new ArrayList<>();

        SimulationCreator simulationCreator = new SimulationCreator();
        for (File file : fileList) {
            if (file.isDirectory() && directoryContainsMetricDataFile(file)) {
                simulationCreator.setDir(file);
                simulationCreator.createSimulation();
                simulationList.add(simulationCreator.getSimulation());
            }
        }
        return simulationList;
    }

    /**
     * Checks if directory contains a parameter dump file
     *
     * @param file
     * @return
     */
    private boolean directoryContainsMetricDataFile(File file) {
        boolean containsFitnessPlot = false;
        File[] allFiles = file.listFiles();
        for (File fileItem : allFiles) {
            if (fileItem.getName().equals(FileName.PARAMETER_DUMP.getName())) {
                containsFitnessPlot = true;
            }
        }
        return containsFitnessPlot;
    }

    public void createModelType() {
        modelType = new ModelType(createModelTypeName(), createSimulationList());
    }

    public void setFileList(List<File> fileList) {
        this.fileList = fileList;
    }

    public ModelType getModelType() {
        return modelType;
    }
}
