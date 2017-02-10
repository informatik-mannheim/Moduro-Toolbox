package de.hs.mannheim.modUro.config;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author Markus Gumbel (m.gumbel@hs-mannheim.de)
 */
public class ToolboxLogger {

    public final static Logger log;

    static {
        log = Logger.getLogger(ToolboxLogger.class.getName());
        log.setLevel(Level.ALL);

        try {
            FileHandler fileTxt = new FileHandler("toolbox-log.txt");

            // Create a TXT formatter:
            SimpleFormatter formatterTxt = new SimpleFormatter();
            fileTxt.setFormatter(formatterTxt);
            log.addHandler(fileTxt);
        } catch (Exception e) {
            System.err.println("Cannot create logging file toolbox-log.txt.");
        }
    }
}
