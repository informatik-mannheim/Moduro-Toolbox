package de.hs.mannheim.modUro.config;

/**
 * Configuration for Reading Images.
 * Percentage of Image, which should be read.7
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public enum ImageReader {

    FIRST_IMAGE(0.2),
    SECOND_IMAGE(0.5),
    THIRD_IMAGE(0.8);

    private final double percentage;

    ImageReader(double percentage) { this.percentage = percentage; }

    public double getPercentage() {return percentage;}

}
