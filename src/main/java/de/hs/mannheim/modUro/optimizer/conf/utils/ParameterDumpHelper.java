package de.hs.mannheim.modUro.optimizer.conf.utils;


import de.hs.mannheim.modUro.optimizer.conf.model.ParameterDump;
import de.hs.mannheim.modUro.optimizer.conf.model.ParameterDumpCellType;

public interface ParameterDumpHelper {

    ParameterDumpCellType getCellTypeMedium(ParameterDump parameterDump);
    ParameterDumpCellType getCellTypeBasalMembrane(ParameterDump parameterDump);
    ParameterDumpCellType getCellTypeStem(ParameterDump parameterDump);
    ParameterDumpCellType getCellTypeBasal(ParameterDump parameterDump);
    ParameterDumpCellType getCellTypeIntermediate(ParameterDump parameterDump);
    ParameterDumpCellType getCellTypeUmbrella(ParameterDump parameterDump);
}
