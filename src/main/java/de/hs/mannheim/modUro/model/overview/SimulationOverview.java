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

import de.hs.mannheim.modUro.model.Simulation;
import de.hs.mannheim.modUro.model.TimeSeries;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for Simulation Overview Model.
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class SimulationOverview {

    private Simulation simulation;

    private String simulationID;
    private String simulationName;
    private String simulationSeed;
    private String modelType;
    private boolean isCompleted;
    private boolean isAborted;
    private boolean isInSteadyState;
    private LocalDateTime startTime;
    private double duration;
    private File directory;
    private List<String> metricTypesName;
    private List<TimeSeries> metricTypes;
    private List<File> images;

    /**
     * Constructor.
     *
     * @param simulation
     */
    public SimulationOverview(Simulation simulation) {
        this.simulation = simulation;
        this.simulationName = simulation.getSimulationName();
        this.simulationSeed = simulation.getSimulationSeed();
        this.simulationID = simulation.getSimulationID();
        this.modelType = simulation.getModelType();
        this.isCompleted = simulation.isCompleted();
        this.isAborted = simulation.isAborted();
        this.isInSteadyState = simulation.isInSteadyState();
        this.startTime = simulation.getStartTime();
        this.duration = simulation.getDuration();
        this.directory = simulation.getDir();
        this.metricTypesName = selectNameOfMetricType(simulation.getAllTimeSeries());
        this.metricTypes = simulation.getAllTimeSeries();
        this.images = simulation.getImages();
    }

    /**
     * Selects name of the metric types of a simulation.
     *
     * @param metricType
     * @return
     */
    private List<String> selectNameOfMetricType(List<TimeSeries> metricType) {
        List<String> metricTypeNameList = new ArrayList<>();

        for (TimeSeries metricTypeItem : metricType) {
            metricTypeNameList.add(metricTypeItem.getName());
        }
        return metricTypeNameList;
    }

    /*Getter*/
    public Simulation getSimulation() {
        return simulation;
    }

    public String getSimulationID() {
        return simulationID;
    }

    public String getSeed() {
        return simulationSeed;
    }

    public String getModelType() {
        return modelType;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public boolean isAborted() {
        return isAborted;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public double getDuration() {
        return duration;
    }

    public File getDirectory() {
        return directory;
    }

    public List<String> getMetricTypesName() {
        return metricTypesName;
    }

    public String getSimulationName() {
        return simulationName;
    }

    public boolean isInSteadyState() {
        return isInSteadyState;
    }

    public List<TimeSeries> getMetricTypes() {
        return metricTypes;
    }

    public List<File> getImages() {
        return images;
    }
}
