package de.hs.mannheim.modUro.optimizer.conf.model;


public class ParameterDumpExecConfig implements ParameterDumpEntry {

    private Integer MCSperDay;
    private Integer SEED;
    private String boundary_x;
    private Integer debugOutputFrequency;
    private Integer dimensions;
    private Double flip2DimRatio;
    private Double fluctuationAmplitude;
    private Double initNutrientDiffusion;
    private Integer latticeSizeInVoxel;
    private Integer maxSteps;
    private Integer neighborOrder;
    private Double sampleIntervalInDays;
    private Integer sampleIntervalInMCS;
    private Integer simDurationDays;
    private Double voxelDensity;
    private Integer xDimension;
    private Integer xLength;
    private Integer yDimension;
    private Integer yLength;
    private Integer zDimension;
    private Integer zLength;

    @Override
    public Boolean isComplete() {
        return null;
    }
}
