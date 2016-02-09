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
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class SimulationCreator {

    //Input file of a simulation
    private File file;

    //Simulation Instance
    private Simulation simulation;

    MetricTypeCreator metricTypeCreator = new MetricTypeCreator();

    //Data for Plotting
    private double minTime = DataPlot.MIN_TIME.getValue();
    private double maxTime = DataPlot.MAX_TIME.getValue();

    /**
     * Constructor
     */
    public SimulationCreator () {}

    /**
     * Creates an ID for simulation.
     * @return
     */
    private int createSimulationId() {
        int id = 0;
        return id;
    }

    /**
     * Creates Name for Simulation with File Name.
     * @return
     */
    private String createsSimulationName() {
        String name = file.getName();
        return name;
    }

    /**
     * Creates name of Model Type of simulation.
     * @return
     */
    private String createModelTypeName() {
        String name;
        String[] tokenValue = file.getName().split(RegEx.MODEL_TYPE_REG_EX.getName());
        name = tokenValue[0];
        return name;
    }

    /**
     * Gets time value of FitnessPlot first column in matrix.
     * @return
     */
    private double[] getTimeValuesOfFitnessPlot() {
        File fitnessPlotFile = file.listFiles((parent, name) -> (name.endsWith(FileEnding.METRICTYPE_FILE.getFileEnding()) && name.equals(FileName.TOTAL_FITNESS_FILE.getName())))[0];
        int lineNr = countLines(fitnessPlotFile);

        double[] matrix = new double[lineNr];

        String line;
        int row = 0;

        BufferedReader buffer = null;
        try {
            buffer = new BufferedReader(new FileReader(fitnessPlotFile.getAbsolutePath()));
            while ((line = buffer.readLine()) != null) {
                String[] vals = line.trim().split(" ");
                matrix[row] = Double.parseDouble(vals[0]);
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matrix;
    }

    /**
     * Gets fitness values of FitnessPlot second column in matrix.
     * @return
     */
    private double[] getFitnessValuesOfFitnessPlot() {
        File fitnessPlotFile = file.listFiles((parent, name) -> (name.endsWith(FileEnding.METRICTYPE_FILE.getFileEnding()) && name.equals(FileName.TOTAL_FITNESS_FILE.getName())))[0];
        int lineNr = countLines(fitnessPlotFile);

        double[] matrix = new double[lineNr];

        String line;
        int row = 0;

        BufferedReader buffer = null;
        try {
            buffer = new BufferedReader(new FileReader(fitnessPlotFile.getAbsolutePath()));
            while ((line = buffer.readLine()) != null) {
                String[] vals = line.trim().split(" ");
                matrix[row] = Double.parseDouble(vals[1]);
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matrix;
    }

    /**
     * Calculates duration of metric data.
     * @return
     */
    private double calculateDuration() {
        double duration = getTimeValuesOfFitnessPlot()[getTimeValuesOfFitnessPlot().length - 1];
        return duration;
    }

    /**
     * Counts the line in a txt file.
     * @return
     * @throws IOException
     */
    private int countLines(File file)  {
        int cnt = 0;
        try {
            LineNumberReader reader = new LineNumberReader(new FileReader(file.getAbsolutePath()));
            String lineRead = "";
            while ((lineRead = reader.readLine()) != null) {}
            cnt = reader.getLineNumber();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cnt;
    }

    /**
     * Create the time of the simulation.
     * @return
     */
    private LocalDateTime createTime() {
        String dateInString = null;
        String timeInString = null;
        LocalDateTime simulationTime = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        File parameterDumpFile = file.listFiles((parent, name) -> (name.endsWith(FileEnding.METRIC_DATA_FILE.getFileEnding()) && name.contains(FileName.METRIC_DATA_FILE.getName())))[0];
                try
                {
                    BufferedReader in = new BufferedReader(new FileReader(parameterDumpFile.getAbsolutePath()));	//reading files in specified directory
                    String line;
                    while ((line = in.readLine()) != null)	//file reading
                    {
                        if(line.contains("startTime")) {
                            String[] values = line.split(" ");
                            dateInString = values[1]+" ";
                            timeInString = values[2].split("\\.")[0];

                            simulationTime = LocalDateTime.parse(dateInString.concat(timeInString), formatter);
                        } else{
                            simulationTime = LocalDateTime.now();
                        }
                        in.close();
                    }

                //}catch( IOException ioException ) {}
                }catch( Exception exception ) {}
        return simulationTime;
    }

    /**
     * Creates List of Metrictypes of the simulation.
     * @return
     */
    public List<MetricType> createMetricTypeList() {
        File[] txtFiles = file.listFiles((parent, name) -> (name.endsWith(FileEnding.METRICTYPE_FILE.getFileEnding()) && !name.equals(FileName.METRIC_DATA_FILE.getName())));
        List<MetricType> metricTypeList = new ArrayList<>();

        for (File file: txtFiles) {
            metricTypeCreator.setFile(file);
            metricTypeCreator.createMetricType();
            metricTypeList.add(metricTypeCreator.getMetricType());
        }
        return metricTypeList;
    }

    /**
     * Checks if simulation is Completed.
     * @return
     */
    private boolean isCompleted() {
        boolean isDone = false;
        double toTime = getTimeValuesOfFitnessPlot()[getTimeValuesOfFitnessPlot().length - 1];
        if(toTime >= maxTime){
            isDone = true;
        }
        return isDone;
    }

    /**
     * Checks if simulation is Aborted.
     * @return
     */
    private boolean isAborted() {
        double lastFitness = getFitnessValuesOfFitnessPlot()[getFitnessValuesOfFitnessPlot().length-1];
        boolean isAborted = false;

        if(lastFitness < 0.05 && isInSteadyState()) {
            isAborted = true;
        }
        return isAborted;
    }

    /**
     * Checks if simulation is in SteadyState.
     * @return
     */
    private boolean isInSteadyState(){

        boolean isInSteadyState = false;
        double toTime = getTimeValuesOfFitnessPlot()[getTimeValuesOfFitnessPlot().length - 1];
        if(toTime >= minTime){
            isInSteadyState = true;
        }
        return isInSteadyState;
    }

    /**
     * Sets the directory path of simulation.
     * @return
     */
    private File dirPath() {
        File dirPath = file;
        return dirPath;
    }

    /**
     * Read images of the simulation.
     * @return
     */
    private List<File> images() {
        List<File> imagePath = new ArrayList<>();

        File[] images = file.listFiles((parent, name) -> name.endsWith(FileEnding.IMAGEFILE.getFileEnding()));

        if(images.length != 0) {
            int count = images.length;

            double firstImage = count * (ImageReader.FIRST_IMAGE.getPercentage());
            imagePath.add(0,images[((int) firstImage)] );

            double secondImage = count * (ImageReader.SECOND_IMAGE.getPercentage());
            imagePath.add(1, images[((int) secondImage)]);

            double thirdImage = count * (ImageReader.THIRD_IMAGE.getPercentage());
            imagePath.add(2,images[((int) thirdImage)]);
        }

        return imagePath;
    }

    /**
     * Creates simulation.
     */
    public void createSimulation(){
        simulation = new Simulation(/*createSimulationId(),*/ createsSimulationName(),createModelTypeName(),calculateDuration(),createTime(),createMetricTypeList(), isCompleted(), isAborted(), isInSteadyState(), dirPath(), images());
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) { this.file = file;}

    public Simulation getSimulation() {
        return simulation;
    }
}
