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

import static de.hs.mannheim.modUro.config.FileName.*;
import static de.hs.mannheim.modUro.config.FileEnding.*;
import static de.hs.mannheim.modUro.config.FitnessName.ARRANGEMENT_FITNESS;
import static de.hs.mannheim.modUro.config.FitnessName.TOTAL_FITNESS;
import static de.hs.mannheim.modUro.config.FitnessName.VOLUME_FITNESS;

import de.hs.mannheim.modUro.config.DataPlot;
import de.hs.mannheim.modUro.config.ImageReader;
import de.hs.mannheim.modUro.config.RegEx;
import de.hs.mannheim.modUro.config.ToolboxLogger;
import de.hs.mannheim.modUro.reader.CellCycleStat;
import de.hs.mannheim.modUro.reader.CelltimesReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class for a Simulation.
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 * @author Markus Gumbel (m.gumbel@hs-mannheim.de)
 */
public class Simulation {

    private File dir;                       //Directory of this simulation

    private String simulationID;
    private String modelType;               //modelType of this simulation
    private long seed;
    private LocalDateTime startTime;

    private List<StatisticValues> metricTypes;    //list of the metric types, which this simulation have
    private List<File> images;              //Filepath of Images

    // Data for Plotting
    private double minTime = DataPlot.MIN_TIME.getValue();
    private double maxTime = DataPlot.MAX_TIME.getValue();

    private double[][] defaultFitnessTable;

    /**
     * Create a simulation run based on the information found
     * in the given directory.
     *
     * @param dir
     */
    public Simulation(File dir) {
        this.dir = dir;
        parseParameterDump();
        this.simulationID = createSimulationId();
        // The name is retrieved from the directory. I.e. the simulation
        // should exists alone.
        this.modelType = createModelTypeName();
        metricTypes = createDataSeriesList();
        this.metricTypes.sort((e1, e2) -> e1.getName().compareTo(e2.getName()));
        this.images = createImages();
    }

    /*Getter and Setter*/
    public String getSimulationID() {
        return simulationID;
    }

    public String getSimulationName() {
        return dir.getName();
    }

    public String getSimulationSeed() {
        return seed + "";
    }

    public String getModelType() {
        return modelType;
    }

    public List<StatisticValues> getMetricTypes() {
        return metricTypes;
    }

    /**
     * Calculates duration of metric data.
     *
     * @return
     */
    public double getDuration() {
        return defaultFitnessTable[defaultFitnessTable.length - 1][0];
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Checks if simulation is Completed.
     *
     * @return
     */
    public boolean isCompleted() {
        double toTime = defaultFitnessTable[defaultFitnessTable.length - 1][0];
        return toTime >= maxTime;
    }

    /**
     * Checks if simulation is in state "Aborted".
     *
     * @return
     */
    public boolean isAborted() {
        double lastFitness = defaultFitnessTable[defaultFitnessTable.length - 1][1];
        // return lastFitness < 0.05;
        return lastFitness < 0.05 && isInSteadyState();
    }

    /**
     * Checks if simulation is in SteadyState.
     *
     * @return
     */
    public boolean isInSteadyState() {
        double toTime = defaultFitnessTable[defaultFitnessTable.length - 1][0];
        return toTime >= minTime;
    }

    public File getDir() {
        return dir;
    }

    public List<File> getImages() {
        return images;
    }

    /**
     * Quick and dirty. TODO
     *
     * @return
     */
    public CelltimesReader getCellTimesReader() {
        try {
            String file = dir.getAbsolutePath().toString() + "/Celltimes.daz";
            return new CelltimesReader(file, 0.5, 2.0);
        } catch (Exception e) {
            return null; // No cell times.
        }
    }

    /**
     * Creates an ID for simulation.
     *
     * @return
     */
    private String createSimulationId() {
        return Math.abs(seed * (long) startTime.hashCode()) + "";
    }

    /**
     * Creates name of Model Type of simulation.
     *
     * @return
     */
    private String createModelTypeName() {
        String[] tokenValue = dir.getName().split(RegEx.MODEL_TYPE_REG_EX.getName());
        return tokenValue[0];
    }

    private void parseParameterDump() {
        String dateInString = null;
        String timeInString = null;
        startTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        File parameterDumpFile = dir.listFiles((parent, name) -> (name.endsWith(METRIC_DATA_FILE.getFileEnding()) && name.contains(PARAMETER_DUMP.getName())))[0];
        try {
            BufferedReader in = new BufferedReader(new FileReader(parameterDumpFile.getAbsolutePath()));    //reading files in specified directory
            String line;
            while ((line = in.readLine()) != null)    //dir reading
            {
                if (line.contains("startTime")) {
                    String[] values = line.split(" ");
                    dateInString = values[1] + " ";
                    timeInString = values[2].split("\\.")[0];
                    startTime = LocalDateTime.parse(dateInString.concat(timeInString), formatter);
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
                        (name.endsWith(METRICTYPE_FILE.getFileEnding()) &&
                                !name.equals(PARAMETER_DUMP.getName()) &&
                                !name.equals("FitnessPlot.dat")) // TODO
        );
        List<StatisticValues> dataSeriesList = new ArrayList<>();

        for (File file : txtFiles) {
            TimeSeries timeSeries = new TimeSeries(file);
            dataSeriesList.add(timeSeries);
            if ((timeSeries.getName() + ".dat").
                    equals(DEFAULT_FITNESS_FILE.getName())) {
                defaultFitnessTable = timeSeries.getData();
            }
        }
        if (defaultFitnessTable == null) {
            ToolboxLogger.log.warning("Dir " + dir + " does not contain a " +
                    DEFAULT_FITNESS_FILE.getName());
        }
        // Now calculate the total fitness:
        TimeSeries total = calcTotalFitness(dataSeriesList);
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

    private TimeSeries calcTotalFitness(List<StatisticValues> dataSeriesList) {
        TimeSeries vol = null, arr = null;
        for (StatisticValues data : dataSeriesList) {
            if (data.getName().equals(VOLUME_FITNESS.getName())) {
                vol = (TimeSeries) data;
            }
            if (data.getName().equals(ARRANGEMENT_FITNESS.getName())) {
                arr = (TimeSeries) data;
            }
        }
        if (vol == null || arr == null) {
            return null; // No total fitness possible!
        }
        String name = TOTAL_FITNESS.getName();
        int size = Math.min(vol.getData().length, arr.getData().length);
        double[][] metricData = new double[size][2];
        for (int i = 0; i < metricData.length; i++) {
            metricData[i][0] = vol.getData()[i][0];
            metricData[i][1] = (vol.getData()[i][1] +
                    arr.getData()[i][1]) / 2;
        }
        return new TimeSeries(name, metricData);
    }

    /**
     * Read createImages of the simulation.
     *
     * @return
     */
    private List<File> createImages() {
        List<File> imagePath = new ArrayList<>();

        File[] images = dir.listFiles((parent, name) -> name.endsWith(IMAGEFILE.getFileEnding()));

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
}
