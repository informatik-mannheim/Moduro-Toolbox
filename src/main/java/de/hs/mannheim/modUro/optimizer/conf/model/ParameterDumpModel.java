package de.hs.mannheim.modUro.optimizer.conf.model;


import java.util.Map;

public class ParameterDumpModel extends ParameterDumpBaseComponent implements ParameterDumpEntry{

    @ParameterDumpValue(key ="adhEnergy",type = ParameterDumpValue.ParameterDumpValueType.DOUBLE)
    private Double adhEnergy;
    @ParameterDumpValue(key ="adhFactor",type = ParameterDumpValue.ParameterDumpValueType.DOUBLE)
    private Double adhFactor;
    @ParameterDumpValue(key = "cellID", type = ParameterDumpValue.ParameterDumpValueType.INTEGER)
    private Integer cellID;
    @ParameterDumpValue(key = "cellLifeCycleLogger", type = ParameterDumpValue.ParameterDumpValueType.STRING)
    private String cellLifeCycleLogger;
    @ParameterDumpValue(key = "energyMatrix", type = ParameterDumpValue.ParameterDumpValueType.STRING)
    private String energyMatrix;
    @ParameterDumpValue(key = "name", type = ParameterDumpValue.ParameterDumpValueType.STRING)
    private String name;
    @ParameterDumpValue(key = "basalNecrosisProb", type = ParameterDumpValue.ParameterDumpValueType.DOUBLE)
    private Double basalNecrosisProb;
    @ParameterDumpValue(key = "intermediateNecrosisProb", type = ParameterDumpValue.ParameterDumpValueType.DOUBLE)
    private Double intermediateNecrosisProb;
    @ParameterDumpValue(key = "stemNecrosisProb", type = ParameterDumpValue.ParameterDumpValueType.DOUBLE)
    private Double stemNecrosisProb;
    @ParameterDumpValue(key = "umbrellaNecrosisProb", type = ParameterDumpValue.ParameterDumpValueType.DOUBLE)
    private Double umbrellaNecrosisProb;

    public ParameterDumpModel(Map<String, String> parsedBlock) throws IllegalAccessException {
        super(parsedBlock);
    }

    public Double getAdhEnergy() {
        return adhEnergy;
    }

    public void setAdhEnergy(Double adhEnergy) {
        this.adhEnergy = adhEnergy;
    }

    public Double getAdhFactor() {
        return adhFactor;
    }

    public void setAdhFactor(Double adhFactor) {
        this.adhFactor = adhFactor;
    }

    public Integer getCellID() {
        return cellID;
    }

    public void setCellID(Integer cellID) {
        this.cellID = cellID;
    }

    public String getCellLifeCycleLogger() {
        return cellLifeCycleLogger;
    }

    public void setCellLifeCycleLogger(String cellLifeCycleLogger) {
        this.cellLifeCycleLogger = cellLifeCycleLogger;
    }

    public String getEnergyMatrix() {
        return energyMatrix;
    }

    public void setEnergyMatrix(String energyMatrix) {
        this.energyMatrix = energyMatrix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getBasalNecrosisProb() {
        return basalNecrosisProb;
    }

    public void setBasalNecrosisProb(Double basalNecrosisProb) {
        this.basalNecrosisProb = basalNecrosisProb;
    }

    public Double getIntermediateNecrosisProb() {
        return intermediateNecrosisProb;
    }

    public void setIntermediateNecrosisProb(Double intermediateNecrosisProb) {
        this.intermediateNecrosisProb = intermediateNecrosisProb;
    }

    public Double getStemNecrosisProb() {
        return stemNecrosisProb;
    }

    public void setStemNecrosisProb(Double stemNecrosisProb) {
        this.stemNecrosisProb = stemNecrosisProb;
    }

    public Double getUmbrellaNecrosisProb() {
        return umbrellaNecrosisProb;
    }

    public void setUmbrellaNecrosisProb(Double umbrellaNecrosisProb) {
        this.umbrellaNecrosisProb = umbrellaNecrosisProb;
    }

    // todo: impl
    @Override
    public Boolean isComplete() {
        return null;
    }
}
