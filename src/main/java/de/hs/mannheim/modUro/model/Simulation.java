package de.hs.mannheim.modUro.model;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Model class for a Simulation.
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class Simulation {

    private static int simulationID = 0;    //unique simulation ID
    private String simulationName;          //name of simulation
    private String modelType;               //modelType of this simulation
    private double duration;                //duration of this simulation
    private LocalDateTime startTime;        //starting time of this simulation
    private List<MetricType> metricType;    //list of the metric types, which this simulation have
    private boolean isCompleted;            //simulation is done
    private boolean isAborted;              //simulation is cancelled
    private boolean isInSteadyState;        //simulation is in steadyState
    private File dir;                       //Directory of this simulation
    private List<File> images;              //Filepath of Images

    /**
     * Construcor
     * @param simulationID
     * @param simulationName
     * @param modelType
     * @param duration
     * @param startTime
     * @param metricType
     * @param dir
     * @param images
     */
    public Simulation(/*int simulationID,*/ String simulationName, String modelType, double duration, LocalDateTime startTime, List<MetricType> metricType, boolean isCompleted, boolean isAborted, boolean isInSteadyState, File dir, List<File> images) {
        this.simulationID = simulationID;
        this.simulationName = simulationName;
        this.modelType = modelType;
        this.duration = duration;
        this.startTime = startTime;
        this.metricType = metricType;
        this.isCompleted = isCompleted;
        this.isAborted = isAborted;
        this.isInSteadyState = isInSteadyState;
        this.dir = dir;
        this.images = images;

        simulationID++;
    }

    /*Getter and Setter*/
    public int getSimulationID() {
        return simulationID;
    }

    public String getSimulationName() {
        return simulationName;
    }

    public String getModelType() {
        return modelType;
    }

    public List<MetricType> getMetricType() { return metricType; }

    public double getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public boolean isAborted() { return isAborted; }

    public File getDir() {
        return dir;
    }

    public boolean isInSteadyState() { return isInSteadyState;}

    public List<File> getImages() {
        return images;
    }
}
