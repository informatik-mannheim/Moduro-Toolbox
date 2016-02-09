package de.hs.mannheim.modUro.creator;

import de.hs.mannheim.modUro.model.MetricType;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import java.io.*;

/**
 * MetricTypeCreator helps to create a MetricType by passing a file txt.
 * @author Mathuraa Pathmanathan
 */
public class MetricTypeCreator {

    //Input file of a metric type txt file
    private File file;

    //MetricType Instance
    private MetricType metricType;

    // Get a DescriptiveStatistics instance (calculation of mean and stdDev)
    DescriptiveStatistics stats = new DescriptiveStatistics();

    /**
     * Constructor
     */
    public MetricTypeCreator () {}

    /**
     * Parses name of MetricType.
     * @return
     */
    private String nameOfMetricType() {
        String name = null;
        int pos = file.getName().lastIndexOf(".");  //searches the pos of last index of "."
        if(pos != -1) {
         name = file.getName().substring(0, pos);   //substring the Filename at the last "."
        }
        return name;
    }

    /**
     * Counts the line in a txt file.
     * @return
     * @throws IOException
     */
    private int countLines()  {
        int cnt = 0;
        try {
            LineNumberReader  reader = new LineNumberReader(new FileReader(file.getAbsolutePath()));
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
     * Reads and parse MetricData from File.
     */
    private double[][] readMetricData() {
        //get the number of lines in txt file
        int lineNr = countLines();
        //initialize matrix length with line length of file
        double[][] matrix = new double[lineNr][2];

        String line;
        int row = 0;
        int col1=0;
        int col2=1;

        BufferedReader buffer = null;
        try {
            buffer = new BufferedReader(new FileReader(file.getAbsolutePath()));
            while ((line = buffer.readLine()) != null) {
                String[] vals = line.trim().split(" ");

                matrix[row][col1] = Double.parseDouble(vals[0]);
                matrix[row][col2] = Double.parseDouble(vals[1]);

                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return matrix;
    }

    /**
     * Calculates mean of metric data.
     * @return
     */
    private double calculateMean() {
        double[] value = getMetricValues();

        // Add the data from the array
        for( int i = 0; i < value.length; i++) {
            stats.addValue(value[i]);
        }

        double mean = stats.getMean();
        stats.clear();

        return mean;
    }

    /**
     * Read only the values of Metric data.
     * @return
     */
    private double[] getMetricValues() {

        int lineNr = countLines();

        double[] matrix = new double[lineNr];

        String line;
        int row = 0;

        BufferedReader buffer = null;
        try {
            buffer = new BufferedReader(new FileReader(file.getAbsolutePath()));
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
     * Calculates deviation of metric data.
     * @return
     */
    private double calculateDeviation(){
        double[] value = getMetricValues();

        // Add the data from the array
        for( int i = 0; i < value.length; i++) {
            stats.addValue(value[i]);
        }

        double stdDev = stats.getStandardDeviation();
        stats.clear();

        return stdDev;
    }


    /**
     * Creates a MetricType Instance.
     */
    public void createMetricType() {
        this.metricType = new MetricType (nameOfMetricType(), readMetricData(), calculateMean(), calculateDeviation());
    }

    public File getFile() { return file;}

    /**
     * Sets a txt file of a MetricType.
     * @param file
     */
    public void setFile(File file) { this.file = file; }

    /**
     * Returns created MetricType.
     * @return
     */
    public MetricType getMetricType() { return metricType;}
}
