package de.hs.mannheim.modUro.optimizer.conf.model;


public class ParameterDumpCellType implements ParameterDumpEntry {

    private Double apoptosisTimeInDays;
    private Double consumPerCell;

    // will be parsed only
    private String descendants;

    private Boolean divides;
    private Boolean frozen;
    private Double growthVolumePerDay;
    private Integer id;
    private Integer maxDiameter;
    private Double maxVol;
    private Integer minDiameter;
    private Double minVol;
    private ParameterDumpCellTypeName parameterDumpCellTypeName;
    private Double necrosisProb;
    private Double nutrientRequirement;
    private Double surFit;
    private Double volFit;

    // todo: impl
    @Override
    public Boolean isComplete() {
        return null;
    }
}

