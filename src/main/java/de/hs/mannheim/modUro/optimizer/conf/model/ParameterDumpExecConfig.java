package de.hs.mannheim.modUro.optimizer.conf.model;


import java.util.Map;

public class ParameterDumpExecConfig extends ParameterDumpBaseComponent implements ParameterDumpEntry {

    @ParameterDumpValue(key = "MCSperDay", type = ParameterDumpValue.ParameterDumpValueType.INTEGER)
    private Integer MCSperDay;
    @ParameterDumpValue(key = "SEED", type = ParameterDumpValue.ParameterDumpValueType.INTEGER)
    private Integer SEED;
    @ParameterDumpValue(key = "boundary_x", type = ParameterDumpValue.ParameterDumpValueType.STRING)
    private String boundary_x;
    @ParameterDumpValue(key = "debugOutputFrequency", type = ParameterDumpValue.ParameterDumpValueType.INTEGER)
    private Integer debugOutputFrequency;
    @ParameterDumpValue(key = "dimensions", type = ParameterDumpValue.ParameterDumpValueType.INTEGER)
    private Integer dimensions;
    @ParameterDumpValue(key = "flip2DimRatio", type = ParameterDumpValue.ParameterDumpValueType.DOUBLE)
    private Double flip2DimRatio;
    @ParameterDumpValue(key = "fluctuationAmplitude", type = ParameterDumpValue.ParameterDumpValueType.DOUBLE)
    private Double fluctuationAmplitude;
    @ParameterDumpValue(key = "initNutrientDiffusion", type = ParameterDumpValue.ParameterDumpValueType.BOOLEAN)
    private Boolean initNutrientDiffusion;
    @ParameterDumpValue(key = "latticeSizeInVoxel", type = ParameterDumpValue.ParameterDumpValueType.INTEGER)
    private Integer latticeSizeInVoxel;
    @ParameterDumpValue(key = "maxSteps", type = ParameterDumpValue.ParameterDumpValueType.INTEGER)
    private Integer maxSteps;
    @ParameterDumpValue(key = "neighborOrder", type = ParameterDumpValue.ParameterDumpValueType.INTEGER)
    private Integer neighborOrder;

    @ParameterDumpValue(key = "sampleIntervalInDays", type = ParameterDumpValue.ParameterDumpValueType.DOUBLE)
    private Double sampleIntervalInDays;
    @ParameterDumpValue(key = "sampleIntervalInMCS", type = ParameterDumpValue.ParameterDumpValueType.INTEGER)
    private Integer sampleIntervalInMCS;
    @ParameterDumpValue(key = "simDurationDays", type = ParameterDumpValue.ParameterDumpValueType.INTEGER)
    private Integer simDurationDays;
    @ParameterDumpValue(key = "voxelDensity", type = ParameterDumpValue.ParameterDumpValueType.DOUBLE)
    private Double voxelDensity;
    @ParameterDumpValue(key = "xDimension", type = ParameterDumpValue.ParameterDumpValueType.INTEGER)
    private Integer xDimension;
    @ParameterDumpValue(key = "xLength", type = ParameterDumpValue.ParameterDumpValueType.INTEGER)
    private Integer xLength;
    @ParameterDumpValue(key = "yDimension", type = ParameterDumpValue.ParameterDumpValueType.INTEGER)
    private Integer yDimension;
    @ParameterDumpValue(key = "yLength", type = ParameterDumpValue.ParameterDumpValueType.INTEGER)
    private Integer yLength;
    @ParameterDumpValue(key = "zDimension", type = ParameterDumpValue.ParameterDumpValueType.INTEGER)
    private Integer zDimension;
    @ParameterDumpValue(key = "zLength", type = ParameterDumpValue.ParameterDumpValueType.INTEGER)
    private Integer zLength;

    public ParameterDumpExecConfig(Map<String, String> parsedBlock) throws IllegalAccessException {
        super(parsedBlock);
    }

    public ParameterDumpExecConfig() {
        super();
        System.out.println("calling default constructor of ParameterDumpExecConfig");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ExecConfig:");
        sb.append(super.toString());
        return sb.toString();
    }

    public Integer getMCSperDay() {
        return MCSperDay;
    }

    public void setMCSperDay(Integer MCSperDay) {
        this.MCSperDay = MCSperDay;
    }

    public Integer getSEED() {
        return SEED;
    }

    public void setSEED(Integer SEED) {
        this.SEED = SEED;
    }

    public String getBoundary_x() {
        return boundary_x;
    }

    public void setBoundary_x(String boundary_x) {
        this.boundary_x = boundary_x;
    }

    public Integer getDebugOutputFrequency() {
        return debugOutputFrequency;
    }

    public void setDebugOutputFrequency(Integer debugOutputFrequency) {
        this.debugOutputFrequency = debugOutputFrequency;
    }

    public Integer getDimensions() {
        return dimensions;
    }

    public void setDimensions(Integer dimensions) {
        this.dimensions = dimensions;
    }

    public Double getFlip2DimRatio() {
        return flip2DimRatio;
    }

    public void setFlip2DimRatio(Double flip2DimRatio) {
        this.flip2DimRatio = flip2DimRatio;
    }

    public Double getFluctuationAmplitude() {
        return fluctuationAmplitude;
    }

    public void setFluctuationAmplitude(Double fluctuationAmplitude) {
        this.fluctuationAmplitude = fluctuationAmplitude;
    }


    public Integer getLatticeSizeInVoxel() {
        return latticeSizeInVoxel;
    }

    public void setLatticeSizeInVoxel(Integer latticeSizeInVoxel) {
        this.latticeSizeInVoxel = latticeSizeInVoxel;
    }

    public Integer getMaxSteps() {
        return maxSteps;
    }

    public void setMaxSteps(Integer maxSteps) {
        this.maxSteps = maxSteps;
    }

    public Integer getNeighborOrder() {
        return neighborOrder;
    }

    public void setNeighborOrder(Integer neighborOrder) {
        this.neighborOrder = neighborOrder;
    }


    public Integer getSampleIntervalInMCS() {
        return sampleIntervalInMCS;
    }

    public void setSampleIntervalInMCS(Integer sampleIntervalInMCS) {
        this.sampleIntervalInMCS = sampleIntervalInMCS;
    }

    public Integer getSimDurationDays() {
        return simDurationDays;
    }

    public void setSimDurationDays(Integer simDurationDays) {
        this.simDurationDays = simDurationDays;
    }

    public Double getVoxelDensity() {
        return voxelDensity;
    }

    public void setVoxelDensity(Double voxelDensity) {
        this.voxelDensity = voxelDensity;
    }

    public Integer getxDimension() {
        return xDimension;
    }

    public void setxDimension(Integer xDimension) {
        this.xDimension = xDimension;
    }

    public Integer getxLength() {
        return xLength;
    }

    public void setxLength(Integer xLength) {
        this.xLength = xLength;
    }

    public Integer getyDimension() {
        return yDimension;
    }

    public void setSampleIntervalInDays(Double sampleIntervalInDays) {
        this.sampleIntervalInDays = sampleIntervalInDays;
    }

    public void setyDimension(Integer yDimension) {
        this.yDimension = yDimension;
    }

    public Integer getyLength() {
        return yLength;
    }

    public void setyLength(Integer yLength) {
        this.yLength = yLength;
    }

    public Integer getzDimension() {
        return zDimension;
    }

    public void setzDimension(Integer zDimension) {
        this.zDimension = zDimension;
    }

    public Integer getzLength() {
        return zLength;
    }

    public void setzLength(Integer zLength) {
        this.zLength = zLength;
    }

    public Double getSampleIntervalInDays() {
        return sampleIntervalInDays;
    }

    @Override
    public Boolean isComplete() {
        return null;
    }

    public Boolean getInitNutrientDiffusion() {
        return initNutrientDiffusion;
    }

    public void setInitNutrientDiffusion(Boolean initNutrientDiffusion) {
        this.initNutrientDiffusion = initNutrientDiffusion;
    }
}
