package de.hs.mannheim.modUro.optimizer.compucell.process;


import de.hs.mannheim.modUro.optimizer.conf.CompuCellExecutionParameters;

import java.io.File;

public interface CompuCellExecutionHelper {
    ProcessBuilder getCompuCellProcessBuilder();
    ProcessBuilder getCompuCellProcessBuilder(File targetCc3DProjectFile);
    ProcessBuilder getCompuCellProcessBuilder(File targetCc3DProjectFile, CompuCellExecutionParameters... executionFlags);

}
