package de.hs.mannheim.modUro.optimizer.conf.model;


import java.util.Collection;

public class ParameterDump {

    // todo: add date field, get ts like the current format of cc3dParamDump: startTime: 2016-11-15 15:04:41.931000
    private String startTime;
    private ParameterDumpExecConfig parameterDumpExecConfig;
    private  ParameterDumpModel parameterDumpModel;
    private Collection<ParameterDumpCellType> parameterDumpCellTypeList;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public ParameterDumpExecConfig getParameterDumpExecConfig() {
        return parameterDumpExecConfig;
    }

    public void setParameterDumpExecConfig(ParameterDumpExecConfig parameterDumpExecConfig) {
        this.parameterDumpExecConfig = parameterDumpExecConfig;
    }

    public ParameterDumpModel getParameterDumpModel() {
        return parameterDumpModel;
    }

    public void setParameterDumpModel(ParameterDumpModel parameterDumpModel) {
        this.parameterDumpModel = parameterDumpModel;
    }

    public Collection<ParameterDumpCellType> getParameterDumpCellTypeList() {
        return parameterDumpCellTypeList;
    }

    public void setParameterDumpCellTypeList(Collection<ParameterDumpCellType> parameterDumpCellTypeList) {
        this.parameterDumpCellTypeList = parameterDumpCellTypeList;
    }

}
