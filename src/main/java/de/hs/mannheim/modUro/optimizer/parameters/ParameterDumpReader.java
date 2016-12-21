package de.hs.mannheim.modUro.optimizer.parameters;


import de.hs.mannheim.modUro.optimizer.conf.model.ParameterDump;

import java.io.File;

public interface ParameterDumpReader {

    ParameterDump parseParamDump(File parameterDumpFile);


    }
