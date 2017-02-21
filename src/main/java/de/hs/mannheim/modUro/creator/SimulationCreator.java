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
import de.hs.mannheim.modUro.controller.diagram.fx.ChartViewer;
import de.hs.mannheim.modUro.model.MetricType;
import de.hs.mannheim.modUro.model.Simulation;
import de.hs.mannheim.modUro.model.StatisticValues;
import de.hs.mannheim.modUro.reader.CellCycleStat;
import de.hs.mannheim.modUro.reader.CelltimesReader;
import de.hs.mannheim.modUro.reader.JCellcycletimeDiagram;

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

    private LocalDateTime simulationTime;
    private long seed;

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
    private String createSimulationId() {
        return Math.abs(seed * (long) simulationTime.hashCode()) + "";
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
            ToolboxLogger.log.warning("Dir " + dir + " does not contain a " +
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
        return simulationTime;
    }

    private void parseParameterDump() {
        String dateInString = null;
        String timeInString = null;
        simulationTime = LocalDateTime.now();
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
                }
                if (line.contains("SEED:")) {
                    String[] values = line.split(" ");
                    seed = Long.parseLong(values[1]);
                }
            }
            in.close();

            //}catch( IOException ioException ) {}
        } catch (Exception exception) {
        }
    }

    /**
     * Creates List of Metrictypes of the simulation.
     *
     * @return
     */
    private List<StatisticValues> createDataSeriesList() {
        // This is how fitness files are detected:
        File[] txtFiles = dir.listFiles(
                (parent, name) ->
                        (name.endsWith(FileEnding.METRICTYPE_FILE.getFileEnding()) &&
                                !name.equals(FileName.PARAMETER_DUMP.getName()) &&
                                !name.equals("FitnessPlot.dat")) // TODO
        );
        List<StatisticValues> dataSeriesList = new ArrayList<>();

        for (File file : txtFiles) {
            metricTypeCreator.setFile(file);
            metricTypeCreator.createMetricType();
            dataSeriesList.add(metricTypeCreator.getMetricType());
        }
        // Now calculate the total fitness:
        MetricType total = calcTotalFitness(dataSeriesList);
        if (total != null) {
            dataSeriesList.add(total);
        }
        // Now calc other metrices:
        // And this means cell cycle times.
        try {
            String file = dir.getAbsolutePath().toString() + "/Celltimes.daz";
            CelltimesReader ctr = new CelltimesReader(file, 0.5, 2.0);
            // TODO Q&D:
            CellCycleStat ccstats =
                    new CellCycleStat(ctr.getCellTypes(), ctr.getCycletimes());
            for (String cellType : ccstats.getCellTypes()) {
                dataSeriesList.add(ccstats.getStatValues(cellType));
            }
        } catch (Exception e) {
            // No cell times.
        }
        return dataSeriesList;
    }

    private MetricType calcTotalFitness(List<StatisticValues> dataSeriesList) {
        MetricType vol = null, arr = null;
        for (StatisticValues data : dataSeriesList) {
            if (data.getName().equals(FitnessName.VOLUME_FITNESS.getName())) {
                vol = (MetricType) data;
            }
            if (data.getName().equals(FitnessName.ARRANGEMENT_FITNESS.getName())) {
                arr = (MetricType) data;
            }
        }
        if (vol == null || arr == null) {
            return null; // No total fitness possible!
        }
        String name = FitnessName.TOTAL_FITNESS.getName();
        int size = Math.min(vol.getMetricData().length, arr.getMetricData().length);
        double[][] metricData = new double[size][2];
        for (int i = 0; i < metricData.length; i++) {
            metricData[i][0] = vol.getMetricData()[i][0];
            metricData[i][1] = (vol.getMetricData()[i][1] +
                    arr.getMetricData()[i][1]) / 2;
        }
        return new MetricType(name, metricData);
    }

    /**
     * Checks if simulation is Completed.
     *
     * @return
     */
    private boolean isCompleted() {
        double toTime = defaultFitnessTable[defaultFitnessTable.length - 1][0];
        return toTime >= maxTime;
    }

    /**
     * Checks if simulation is in state "Aborted".
     *
     * @return
     */
    private boolean isAborted() {
        double lastFitness = defaultFitnessTable[defaultFitnessTable.length - 1][1];
        // return lastFitness < 0.05;
        return lastFitness < 0.05 && isInSteadyState();
    }

    /**
     * Checks if simulation is in SteadyState.
     *
     * @return
     */
    private boolean isInSteadyState() {
        double toTime = defaultFitnessTable[defaultFitnessTable.length - 1][0];
        return toTime >= minTime;
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
        simulation = new Simulation(createSimulationId(),
                createsSimulationName(),
                createModelTypeName(),
                calculateDuration(),
                createTime(),
                createDataSeriesList(),
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
        parseParameterDump();
    }

    public Simulation getSimulation() {
        return simulation;
    }
}
