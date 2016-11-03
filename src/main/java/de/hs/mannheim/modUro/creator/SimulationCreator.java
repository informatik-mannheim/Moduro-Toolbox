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

import de.hs.mannheim.modUro.config.*;
import de.hs.mannheim.modUro.model.MetricType;
import de.hs.mannheim.modUro.model.Simulation;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * SimulationCreator helps to create a Simulation by passing a path.
 * The dir must be set via setDir(). Then createSimulation()
 * creates a simulation object.
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class SimulationCreator {

    //Input dir of a simulation
    private File dir;

    //Simulation Instance
    private Simulation simulation;

    MetricTypeCreator metricTypeCreator = new MetricTypeCreator();

    //Data for Plotting
    private double minTime = DataPlot.MIN_TIME.getValue();
    private double maxTime = DataPlot.MAX_TIME.getValue();

    /**
     * Constructor
     */
    public SimulationCreator() {
    }

    /**
     * Creates an ID for simulation.
     *
     * @return
     */
    private int createSimulationId() {
        int id = 0;
        return id;
    }

    /**
     * Creates Name for Simulation with File Name.
     *
     * @return
     */
    private String createsSimulationName() {
        String name = dir.getName();
        return name;
    }

    /**
     * Creates name of Model Type of simulation.
     *
     * @return
     */
    private String createModelTypeName() {
        String name;
        String[] tokenValue = dir.getName().split(RegEx.MODEL_TYPE_REG_EX.getName());
        name = tokenValue[0];
        return name;
    }

    /**
     * Gets time value of FitnessPlot first column in matrix.
     *
     * @return
     */
    private double[][] defaultFitnessTable;

    private void calcDefaultFitnessTable() {
        defaultFitnessTable = new double[1][2]; // Empty table so far.

        File[] files = dir.listFiles(
                (parent, name) ->
                        name.equals(FileName.DEFAULT_FITNESS_FILE.getName())
        );
        if (files.length == 0) {
            System.err.println("Dir " + dir + " does not contain a " +
                    FileName.DEFAULT_FITNESS_FILE.getName());
            return;
        }
        File fitnessPlotFile = files[0];

        String line;
        int row = 0;

        List<Double> times = new ArrayList<>();
        List<Double> fitness = new ArrayList<>();
        BufferedReader buffer = null;
        try {
            buffer = new BufferedReader(new FileReader(fitnessPlotFile.getAbsolutePath()));
            while ((line = buffer.readLine()) != null) {
                String[] vals = line.trim().split(" ");
                times.add(Double.parseDouble(vals[0]));
                fitness.add(Double.parseDouble(vals[1]));
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        defaultFitnessTable = new double[row][2];
        for (int i = 0; i < row; i++) {
            defaultFitnessTable[i][0] = times.get(i);
            defaultFitnessTable[i][1] = fitness.get(i);
        }
    }


    /**
     * Calculates duration of metric data.
     *
     * @return
     */
    private double calculateDuration() {
        return defaultFitnessTable[defaultFitnessTable.length - 1][0];
    }

    /**
     * Create the time of the simulation.
     *
     * @return
     */
    private LocalDateTime createTime() {
        String dateInString = null;
        String timeInString = null;
        LocalDateTime simulationTime = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        File parameterDumpFile = dir.listFiles((parent, name) -> (name.endsWith(FileEnding.METRIC_DATA_FILE.getFileEnding()) && name.contains(FileName.PARAMETER_DUMP.getName())))[0];
        try {
            BufferedReader in = new BufferedReader(new FileReader(parameterDumpFile.getAbsolutePath()));    //reading files in specified directory
            String line;
            while ((line = in.readLine()) != null)    //dir reading
            {
                if (line.contains("startTime")) {
                    String[] values = line.split(" ");
                    dateInString = values[1] + " ";
                    timeInString = values[2].split("\\.")[0];

                    simulationTime = LocalDateTime.parse(dateInString.concat(timeInString), formatter);
                } else {
                    simulationTime = LocalDateTime.now();
                }
                in.close();
            }

            //}catch( IOException ioException ) {}
        } catch (Exception exception) {
        }
        return simulationTime;
    }

    /**
     * Creates List of Metrictypes of the simulation.
     *
     * @return
     */
    private List<MetricType> createMetricTypeList() {
        File[] txtFiles = dir.listFiles((parent, name) -> (name.endsWith(FileEnding.METRICTYPE_FILE.getFileEnding()) && !name.equals(FileName.PARAMETER_DUMP.getName())));
        List<MetricType> metricTypeList = new ArrayList<>();

        for (File file : txtFiles) {
            metricTypeCreator.setFile(file);
            metricTypeCreator.createMetricType();
            metricTypeList.add(metricTypeCreator.getMetricType());
        }
        return metricTypeList;
    }

    /**
     * Checks if simulation is Completed.
     *
     * @return
     */
    private boolean isCompleted() {
        boolean isDone = false;
        double toTime = defaultFitnessTable[defaultFitnessTable.length - 1][0];
        if (toTime >= maxTime) {
            isDone = true;
        }
        return isDone;
    }

    /**
     * Checks if simulation is Aborted.
     *
     * @return
     */
    private boolean isAborted() {
        double lastFitness = defaultFitnessTable[defaultFitnessTable.length - 1][1];
        boolean isAborted = false;

        if (lastFitness < 0.05 && isInSteadyState()) {
            isAborted = true;
        }
        return isAborted;
    }

    /**
     * Checks if simulation is in SteadyState.
     *
     * @return
     */
    private boolean isInSteadyState() {

        boolean isInSteadyState = false;
        double toTime = defaultFitnessTable[defaultFitnessTable.length - 1][0];
        if (toTime >= minTime) {
            isInSteadyState = true;
        }
        return isInSteadyState;
    }

    /**
     * Sets the directory path of simulation.
     *
     * @return
     */
    private File dirPath() {
        File dirPath = dir;
        return dirPath;
    }

    /**
     * Read images of the simulation.
     *
     * @return
     */
    private List<File> images() {
        List<File> imagePath = new ArrayList<>();

        File[] images = dir.listFiles((parent, name) -> name.endsWith(FileEnding.IMAGEFILE.getFileEnding()));

        if (images.length != 0) {
            int count = images.length;

            double firstImage = count * (ImageReader.FIRST_IMAGE.getPercentage());
            imagePath.add(0, images[((int) firstImage)]);

            double secondImage = count * (ImageReader.SECOND_IMAGE.getPercentage());
            imagePath.add(1, images[((int) secondImage)]);

            double thirdImage = count * (ImageReader.THIRD_IMAGE.getPercentage());
            imagePath.add(2, images[((int) thirdImage)]);
        }

        return imagePath;
    }

    /**
     * Creates simulation.
     */
    public void createSimulation() {
        calcDefaultFitnessTable();
        simulation = new Simulation(/*createSimulationId(),*/
                createsSimulationName(),
                createModelTypeName(),
                calculateDuration(),
                createTime(),
                createMetricTypeList(),
                isCompleted(),
                isAborted(),
                isInSteadyState(),
                dirPath(),
                images());
    }

    public File getDir() {
        return dir;
    }

    /**
     * Set the directory that contains the CC3D log files.
     *
     * @param dir
     */
    public void setDir(File dir) {
        this.dir = dir;
    }

    public Simulation getSimulation() {
        return simulation;
    }
}
