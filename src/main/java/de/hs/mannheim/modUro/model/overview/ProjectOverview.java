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
package de.hs.mannheim.modUro.model.overview;

import de.hs.mannheim.modUro.model.ModuroModel;
import de.hs.mannheim.modUro.model.Project;
import de.hs.mannheim.modUro.model.Simulation;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for Project Overview Model.
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class ProjectOverview {

    private Project project;

    private int numberOfSimulations;
    private int numberOfSteadyStateSimulation;
    private int numberOfAbortedSimulations;
    private int numberOfCompletedSimulations;
    private List<String> kindsOfModelTypes;

    /**
     * Constructor.
     *
     * @param project
     */
    public ProjectOverview(Project project) {
        this.project = project;
        countSimulations();
        countSteadyStateSimulation();
        countCompletedSimulations();
        countAbortedSimulations();
        listKindsOfModelType();
    }

    /**
     * Counts number of Simulations.
     */
    private void countSimulations() {
        int count = 0;
        for (ModuroModel moduroModelItem : project.getModuroModelList()) {
            count = count + moduroModelItem.getSimulations().size();
        }
        this.numberOfSimulations = count;
    }

    /**
     * Counts number of completed simulations.
     */
    private void countCompletedSimulations() {
        int count = 0;
        for (ModuroModel moduroModelItem : project.getModuroModelList()) {
            for (Simulation simulationItem : moduroModelItem.getSimulations()) {
                if (simulationItem.isCompleted()) {
                    count++;
                }
            }
        }
        this.numberOfCompletedSimulations = count;
    }

    /**
     * Counts number of aborted simulations.
     */
    private void countAbortedSimulations() {
        int count = 0;
        for (ModuroModel moduroModelItem : project.getModuroModelList()) {
            for (Simulation simulationItem : moduroModelItem.getSimulations()) {
                if (simulationItem.isInSteadyState()) {
                    count++;
                }
            }
        }
        this.numberOfSteadyStateSimulation = count;
    }

    private void countSteadyStateSimulation() {
        int count = 0;
        for (ModuroModel moduroModelItem : project.getModuroModelList()) {
            for (Simulation simulationItem : moduroModelItem.getSimulations()) {
                if (simulationItem.isAborted()) {
                    count++;
                }
            }
        }
        this.numberOfAbortedSimulations = count;
    }

    /**
     * Lists kinds of Modeltypes
     */
    private void listKindsOfModelType() {
        this.kindsOfModelTypes = new ArrayList<>();
        for (ModuroModel moduroModelItem : project.getModuroModelList()) {
            this.kindsOfModelTypes.add(moduroModelItem.getName());
        }
    }

    /*Getter*/
    public Project getProject() {
        return project;
    }

    public int getNumberOfSimulations() {
        return numberOfSimulations;
    }

    public int getNumberOfAbortedSimulations() {
        return numberOfAbortedSimulations;
    }

    public int getNumberOfCompletedSimulations() {
        return numberOfCompletedSimulations;
    }

    public int getNumberOfSteadyStateSimulation() {
        return numberOfSteadyStateSimulation;
    }

    public List<String> getKindsOfModelTypes() {
        return kindsOfModelTypes;
    }


}
