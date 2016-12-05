package de.hs.mannheim.modUro.optimizer.conf.model;


public class ParameterDumpModel implements ParameterDumpEntry{

    // in the parameterdump the header of the configuration which relates to the model-configuration is not static.
    // So we have to store the name of the model here. Alternative: read the "name" field and set it as the fileHeaderKey,
    // but this is not safe.
    private String parameterDumpFileHeaderKey;

    private Double adhEnergy;
    private Double adhFactor;
    private Integer cellID;
    private String cellLifeCycleLogger;
    private String energyMatrix;
    private String name;
    private String necrosisProbBasal;
    private String necrosisProbIntermediate;
    private String necrosisProbStem;
    private String necrosisProbUmbrella;

    // todo: impl
    @Override
    public Boolean isComplete() {
        return null;
    }
}
