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

import de.hs.mannheim.modUro.config.FileName;
import de.hs.mannheim.modUro.config.RegEx;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A moduro model.
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 * @author Markus Gumbel (m.gumbel@hs-mannheim.de)
 */
public class ModuroModel {

    private String name;
    private List<Simulation> simulations;
    // List of all directories that contain this model:
    private List<File> dirList;

    public ModuroModel(List<File> dirList) {
        this.dirList = dirList;
        this.name = createModelTypeName();
        this.simulations = createSimulationList();
    }

    public String getName() {
        return name;
    }

    public List<Simulation> getSimulations() {
        return simulations;
    }

    /**
     * Creates name of Modeltype.
     *
     * @return
     */
    private String createModelTypeName() {
        String[] tokenValue =
                dirList.get(0).getName().split(RegEx.MODEL_TYPE_REG_EX.getName());
        return tokenValue[0];
    }

    /**
     * Creates SimulationList of the modeltype.
     *
     * @return
     */
    private List<Simulation> createSimulationList() {
        List<Simulation> simulationList = new ArrayList<>();

        for (File file : dirList) {
            if (file.isDirectory() && directoryContainsMetricDataFile(file)) {
                Simulation simulation = new Simulation(file);
                simulationList.add(simulation);
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
}
