package de.hs.mannheim.modUro.optimizer.compucell.process;

import de.hs.mannheim.modUro.optimizer.conf.CompuCellExecutionParameters;
import de.hs.mannheim.modUro.optimizer.conf.OptimizationConfigurationConstants;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CompuCellExecutionHelperImpl implements CompuCellExecutionHelper {
    private final String compucellTargetPath;

    public CompuCellExecutionHelperImpl() {
        this.compucellTargetPath =
                OptimizationConfigurationConstants.COMPUCELL_DEFAULT_BIN_DIRECTORY.getAbsolutePath()
                        + File.separator
                        + OptimizationConfigurationConstants.COMPUCELL_RUN_COMPUCELL_BATCH_NAME;
    }

    @Override
    public ProcessBuilder getCompuCellProcessBuilder() {
        System.out.println("Building process with parameter: " + compucellTargetPath);
        final ProcessBuilder cc3dProc =
                new ProcessBuilder(
                        compucellTargetPath
                );

        return cc3dProc;
    }

    @Override
    public ProcessBuilder getCompuCellProcessBuilder(File targetCc3DProjectFile) {

        if (targetCc3DProjectFile == null || !targetCc3DProjectFile.exists()) {
            System.err.println(
                    "targetproject file does not exist: " +
                            targetCc3DProjectFile == null ? "null"
                            : targetCc3DProjectFile.getAbsolutePath()
            );
            return null;
        }

        // put parameters:
        // https://examples.javacodegeeks.com/core-java/lang/processbuilder/java-lang-processbuilder-example/
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OptimizationConfigurationConstants.COMPUCELL_RUN_COMPUCELL_PARAM_FLAG_TARGET_PROJECT,
                targetCc3DProjectFile.getAbsolutePath());

        final ProcessBuilder cc3dProc =
                new ProcessBuilder("cmd.exe", "/C", compucellTargetPath, "-i" ,targetCc3DProjectFile.getAbsolutePath())
                        .inheritIO();
        return cc3dProc;
    }

    @Override
    public ProcessBuilder getCompuCellProcessBuilder(File targetCc3DProjectFile,
                                                     CompuCellExecutionParameters... executionFlags) {
        //todo: impl
        System.err.println("execution flags will be ignored when starting compuCell. Will use default settings");
        return getCompuCellProcessBuilder(targetCc3DProjectFile);
    }
}
