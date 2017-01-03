package de.hs.mannheim.modUro.optimizer.conf.model;


import de.hs.mannheim.modUro.optimizer.conf.model.ParameterDumpValue.ParameterDumpValueType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Map;

public class ParameterDumpCellType extends ParameterDumpBaseComponent implements ParameterDumpEntry {

    @ParameterDumpValue(key = "apoptosisTimeInDays", type = ParameterDumpValueType.DOUBLE)
    private Double apoptosisTimeInDays;

    @ParameterDumpValue(key = "consumPerCell", type = ParameterDumpValueType.DOUBLE)
    private Double consumPerCell;

    @ParameterDumpValue(key = "descendants", type = ParameterDumpValueType.STRING)
    private String descendants;

    @ParameterDumpValue(key = "divides", type = ParameterDumpValueType.BOOLEAN)
    private Boolean divides;

    @ParameterDumpValue(key = "frozen", type = ParameterDumpValueType.BOOLEAN)
    private Boolean frozen;

    @ParameterDumpValue(key = "growthVolumePerDay", type = ParameterDumpValueType.DOUBLE)
    private Double growthVolumePerDay;

    @ParameterDumpValue(key = "id", type = ParameterDumpValueType.INTEGER)
    private Integer id;

    @ParameterDumpValue(key = "maxDiameter", type = ParameterDumpValueType.INTEGER)
    private Integer maxDiameter;

    @ParameterDumpValue(key = "maxVol", type = ParameterDumpValueType.DOUBLE)
    private Double maxVol;

    @ParameterDumpValue(key = "minDiameter", type = ParameterDumpValueType.INTEGER)
    private Integer minDiameter;

    @ParameterDumpValue(key = "minVol", type = ParameterDumpValueType.DOUBLE)
    private Double minVol;

    @ParameterDumpValue(key = "name", type = ParameterDumpValueType.STRING)
    private String name;

    @ParameterDumpValue(key = "necrosisProb", type = ParameterDumpValueType.DOUBLE)
    private Double necrosisProb;

    @ParameterDumpValue(key = "nutrientRequirement", type = ParameterDumpValueType.DOUBLE)
    private Double nutrientRequirement;

    @ParameterDumpValue(key = "surFit", type = ParameterDumpValueType.DOUBLE)
    private Double surFit;

    @ParameterDumpValue(key = "volFit", type = ParameterDumpValueType.DOUBLE)
    private Double volFit;

    public ParameterDumpCellType(Map<String, String> parsedBlock) throws IllegalAccessException {
        super(parsedBlock);
    }

    public ParameterDumpCellType() {
        System.out.println("Called Default Constructor of ParameterDumpCellType()");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CellType:").append("\n");
        sb.append(super.toString());
        return sb.toString();
    }

    // todo: impl
    @Override
    public Boolean isComplete() {
        throw new NotImplementedException();
    }

    public Double getApoptosisTimeInDays() {
        return apoptosisTimeInDays;
    }

    public void setApoptosisTimeInDays(Double apoptosisTimeInDays) {
        this.apoptosisTimeInDays = apoptosisTimeInDays;
    }

    public Double getConsumPerCell() {
        return consumPerCell;
    }

    public void setConsumPerCell(Double consumPerCell) {
        this.consumPerCell = consumPerCell;
    }

    public String getDescendants() {
        return descendants;
    }

    public void setDescendants(String descendants) {
        this.descendants = descendants;
    }

    public Boolean getDivides() {
        return divides;
    }

    public void setDivides(Boolean divides) {
        this.divides = divides;
    }

    public Boolean getFrozen() {
        return frozen;
    }

    public void setFrozen(Boolean frozen) {
        this.frozen = frozen;
    }

    public Double getGrowthVolumePerDay() {
        return growthVolumePerDay;
    }

    public void setGrowthVolumePerDay(Double growthVolumePerDay) {
        this.growthVolumePerDay = growthVolumePerDay;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMaxDiameter() {
        return maxDiameter;
    }

    public void setMaxDiameter(Integer maxDiameter) {
        this.maxDiameter = maxDiameter;
    }

    public Double getMaxVol() {
        return maxVol;
    }

    public void setMaxVol(Double maxVol) {
        this.maxVol = maxVol;
    }

    public Integer getMinDiameter() {
        return minDiameter;
    }

    public void setMinDiameter(Integer minDiameter) {
        this.minDiameter = minDiameter;
    }

    public Double getMinVol() {
        return minVol;
    }

    public void setMinVol(Double minVol) {
        this.minVol = minVol;
    }


    public Double getNecrosisProb() {
        return necrosisProb;
    }

    public void setNecrosisProb(Double necrosisProb) {
        this.necrosisProb = necrosisProb;
    }

    public Double getNutrientRequirement() {
        return nutrientRequirement;
    }

    public void setNutrientRequirement(Double nutrientRequirement) {
        this.nutrientRequirement = nutrientRequirement;
    }

    public Double getSurFit() {
        return surFit;
    }

    public void setSurFit(Double surFit) {
        this.surFit = surFit;
    }

    public Double getVolFit() {
        return volFit;
    }

    public void setVolFit(Double volFit) {
        this.volFit = volFit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

