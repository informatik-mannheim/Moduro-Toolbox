package de.hs.mannheim.modUro.optimizer.gui;

import java.io.IOException;

/**This class extracts the optimized data from OptimizersOutput files to generate an new ParameterDump
 * Created by Station on 21.11.2016.
 */
public class SaveNewParamDump {

    public void extractNewValuesFromOutput() throws IOException {
        /*
        File optimizerOutput = new File("outcmaesfit.dat");
        BufferedReader readParamOutput = new BufferedReader(new FileReader(optimizerOutput));
        */

        double[] newTestUroParameters = {2.0, 0.25, 0, 0, 1.0, 1.0, 0, 0, 1.0, 1.0, 68.0678408278, 1.0, 0.5, 0.9, 62.8318530718, 1.0, 0.5, 0.9, 194.386045441, 1.0, 0.1, 0.9, 359.136400183, 1.0, 0.1, 0.9};

        OptimizationParameterHandler getOldParamsFromDump = new OptimizationParameterHandler();
        getOldParamsFromDump.extractParameterDumpValues();



    }

}
