package de.hs.mannheim.modUro.optimizer.conf.model;


import java.util.Map;

// todo: Hier liegt aktuelle in Problem bei den zu verarbeitenden Parameterdump files vor.
// todo: Die Parameter für das Model unterscheiden sich aktuell in den vorliegenden Beispiel-Dateien.
// todo: Es ist nicht ersichtlich mit welcher Version welche ParameterDump erzeugt wurde, darum kann man nicht
// todo: sagen, ob es eine aktuelle Version gibt oder ob beide Versionen supported werden müssen

public class ParameterDumpModel extends ParameterDumpBaseComponent implements ParameterDumpEntry {

    @ParameterDumpValue(key = "adhEnergy", type = ParameterDumpValue.ParameterDumpValueType.DOUBLE)
    private Double adhEnergy;

    @ParameterDumpValue(key = "adhFactor", type = ParameterDumpValue.ParameterDumpValueType.DOUBLE)
    private Double adhFactor;

    @ParameterDumpValue(key = "cellID", type = ParameterDumpValue.ParameterDumpValueType.INTEGER)
    private Integer cellID;

    @ParameterDumpValue(key = "cellLifeCycleLogger", type = ParameterDumpValue.ParameterDumpValueType.STRING)
    private String cellLifeCycleLogger;

    @ParameterDumpValue(key = "energyMatrix", type = ParameterDumpValue.ParameterDumpValueType.STRING)
    private String energyMatrix;

    @ParameterDumpValue(key = "name", type = ParameterDumpValue.ParameterDumpValueType.STRING)
    private String name;

    // fixme: Parameter unterscheidet sich aktuell in den ParameterDumps: basalNecrosisProb und necrosisProbBasal
    @ParameterDumpValue(key = "basalNecrosisProb,necrosisProbBasal", type = ParameterDumpValue.ParameterDumpValueType.DOUBLE)
    private Double basalNecrosisProb;

    // fixme: Parameter unterscheidet sich aktuell in den ParameterDumps: intermediateNecrosisProb und necrosisProbIntermediate
    @ParameterDumpValue(key = "intermediateNecrosisProb,necrosisProbIntermediate", type = ParameterDumpValue.ParameterDumpValueType.DOUBLE)
    private Double intermediateNecrosisProb;

    // fixme: Parameter unterscheidet sich aktuell in den ParameterDumps: stemNecrosisProb und necrosisProbStem
    @ParameterDumpValue(key = "stemNecrosisProb,necrosisProbStem", type = ParameterDumpValue.ParameterDumpValueType.DOUBLE)
    private Double stemNecrosisProb;

    // fixme: Parameter unterscheidet sich aktuell in den ParameterDumps: umbrellaNecrosisProb und necrosisProbUmbrella
    @ParameterDumpValue(key = "umbrellaNecrosisProb,necrosisProbUmbrella", type = ParameterDumpValue.ParameterDumpValueType.DOUBLE)
    private Double umbrellaNecrosisProb;

    public ParameterDumpModel(Map<String, String> parsedBlock) throws IllegalAccessException {
        super(parsedBlock);
    }


    public ParameterDumpModel() {
        super();
        System.out.println("Calling default construcor of ParameterDumpModel");
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(": ").append("\n");
        sb.append(super.toString());
        return sb.toString();
    }
}
