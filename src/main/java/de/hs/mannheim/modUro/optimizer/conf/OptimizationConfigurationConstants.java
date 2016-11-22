package de.hs.mannheim.modUro.optimizer.conf;


import java.io.File;

public class OptimizationConfigurationConstants {
    // todo: move all values to a configuration file
    public static final File COMPUCELL_DEFAULT_BIN_DIRECTORY = new File("C://Program Files (x86)//CompuCell3D//");
    public static final File COMPUCELL_RUN_COMPUCELL_BATCH_NAME = new File("compucell3d.bat");
    public static final File COMPUCELL_RUN_SCRIPT_BATCH_NAME = new File("runScript.bat");

    public static final String COMPUCELL_WORKSPACE_FILE_PARAM_DUMP_FILENAME = "ParameterDump.dat";

    // -i [filepath] is used in cmd to execute a specified project in cc3d
    // other relevant parameters are added as an enum in CompuCellExecutionParameters.class
    public static final String COMPUCELL_RUN_COMPUCELL_PARAM_FLAG_TARGET_PROJECT = "-i";
}
