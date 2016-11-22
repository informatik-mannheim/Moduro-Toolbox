package de.hs.mannheim.modUro.optimizer.compucell.filewatch;


import org.apache.commons.io.FilenameUtils;

import java.io.File;

public class CompuCellWorkspaceWatcherImpl implements CompuCellWorkspaceWatcher {

    /**
     * Loads cc3d file.
     * Gets Target Workspace from Config (currently hard coded in OptimizationConfigurationConstants.class)
     * Finds current target project directory. Checks if ParameterDump.dat is available within this directory.
     * <p>
     * If available - ParameterDump will be passed as File.
     *
     * @param projectCc3DFile
     */
    @Override
    public void watchProject(File projectCc3DFile) {
        if (projectCc3DFile == null || !projectCc3DFile.exists() || projectCc3DFile.isDirectory()) {
            System.err.println(
                    "Invalid project file : " + projectCc3DFile == null ? "null" : projectCc3DFile.getAbsolutePath());
        }

        String projectName = FilenameUtils.removeExtension(projectCc3DFile.getName());
        System.out.println("Blank projectName is " + projectName);

        // todo: impl

    }
}
