package de.hs.mannheim.modUro.config;

/**
 * Configuration for Files.
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public enum FileEnding {

    IMAGEFILE(".png"),           //Image file ending
    METRIC_DATA_FILE(".dat"),    //FitnessPlot.dat -> contains metric data
    METRICTYPE_FILE(".dat");     //Metrictype files

    private final String fileEnding;

    FileEnding(String fileEnding) { this.fileEnding = fileEnding; }

    public String getFileEnding() {return fileEnding;}
}
