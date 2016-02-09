package de.hs.mannheim.modUro.config;

/**
 * RegEx Class for Parsing. Use RegEx.
 * @author  Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public enum RegEx {

    //RegEx for parse de.hs.mannheim.modUro.model type in dir; e.x. CM-IN-RA_cc3d_01_15_2015_15_21_21 --> CM-IN-RA
    //MODEL_TYPE_REG_EX("(([A-Z]{2,4}-?){2,})");
    MODEL_TYPE_REG_EX("_");

    private final String name;

    RegEx(String name) { this.name = name; }

    public String getName() {return name;}
}
