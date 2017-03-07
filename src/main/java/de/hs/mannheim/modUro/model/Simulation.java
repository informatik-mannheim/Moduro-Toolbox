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

import de.hs.mannheim.modUro.config.*;
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
    private boolean hasVideo = false;

    private double minTime = ToolboxParameter.params.getSteadystatetime();
    private double maxTime = ToolboxParameter.params.getEndtime();

    private double[][] defaultFitnessTable;
    private CelltimesReader ctr;

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
        this.hasVideo = createVideo();
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

    public boolean hasVideo() {
        return hasVideo;
    }

    /**
     * Refresh this simulation. For instance, the a video might
     * have been generated in the meantime.
     */
    public void refresh() {
        hasVideo = createVideo();
    }

    /**
     * Quick and dirty. TODO
     *
     * @return
     */
    public CelltimesReader getCellTimesReader() {
        return ctr;
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
            TimeSeries totalNorm = calcNormTotalFitness(total);
            dataSeriesList.add(totalNorm);
        }
        // Now calc other metrices:
        // And this means cell cycle times.
        try {
            String file = dir.getAbsolutePath().toString() + "/Celltimes.daz";
            ctr = new CelltimesReader(file, 0.5, 2.0);
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
        String name = TOTAL_FITNESS.getName() + " (orig.)";
        int size = Math.min(vol.getData().length, arr.getData().length);
        double[][] metricData = new double[size][2];
        for (int i = 0; i < metricData.length; i++) {
            metricData[i][0] = vol.getData()[i][0];
            metricData[i][1] = (vol.getData()[i][1] +
                    arr.getData()[i][1]) / 2;
        }
        return new TimeSeries(name, metricData);
    }

    private TimeSeries calcNormTotalFitness(TimeSeries fitness) {
        String name = TOTAL_FITNESS.getName();
        double maxTime = fitness.getMaxTime();
        double endTime = ToolboxParameter.params.getEndtime();
        if (maxTime < endTime) {
            // Apparently, the simulation was aborted.
            // How many time points up to 720.0 are missing?
            int m = fitness.size();
            int missingPoints = (2 * (int) endTime) - m;
            // Create bigger data array for all 1440 = 2 * 720 points ...
            double[][] newMetricData = new double[m + missingPoints][2];
            // and copy the values:
            for (int i = 0; i < m; i++) {
                newMetricData[i][0] = fitness.getData()[i][0];
                newMetricData[i][1] = fitness.getData()[i][1];
            }
            double fit;
            if (ToolboxParameter.params.getTotalfitnesstype().equals("LAST")) {
                fit = fitness.getData()[m - 1][1];
            } else {
                fit = 0;
            }
            // Now initialize the new data points:
            for (int i = 0; i < missingPoints; i++) {
                newMetricData[m + i][0] = maxTime + (double) (i + 1) / 2;
                newMetricData[m + i][1] = fit; // Unknown (bad) fitness.
            }
            return new TimeSeries(name, newMetricData);
        } else {
            // Just return the fitness as it is:
            return new TimeSeries(name, fitness.getData());
        }
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

            double firstImage = count * (ToolboxParameter.params.getFirstImage());
            imagePath.add(0, images[((int) firstImage)]);

            double secondImage = count * (ToolboxParameter.params.getSecondImage());
            imagePath.add(1, images[((int) secondImage)]);

            double thirdImage = count * (ToolboxParameter.params.getThirdImage());
            imagePath.add(2, images[((int) thirdImage)]);
        }

        return imagePath;
    }

    private boolean createVideo() {
        File[] files =
                dir.listFiles((file, name) -> name.endsWith("video.wmv"));
        return files.length == 1;
    }
}
