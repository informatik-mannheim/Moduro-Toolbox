package de.hs.mannheim.modUro.optimizer.conf.model;


import java.util.Collection;

public class ParameterDump {

    // todo: add date field, get ts like the current format of cc3dParamDump: startTime: 2016-11-15 15:04:41.931000
    private String startTime;
    private ParameterDumpExecConfig parameterDumpExecConfig;
    private  ParameterDumpModel parameterDumpModel;
    private Collection<ParameterDumpCellType> parameterDumpCellTypeList;


}
