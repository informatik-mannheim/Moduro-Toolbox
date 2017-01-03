package de.hs.mannheim.modUro.optimizer.cmaes;

import com.google.common.base.Predicate;
import com.google.common.collect.BiMap;
import com.google.common.collect.Collections2;
import com.google.common.collect.HashBiMap;
import de.hs.mannheim.modUro.optimizer.conf.model.ParameterDump;
import de.hs.mannheim.modUro.optimizer.conf.model.ParameterDumpCellType;
import de.hs.mannheim.modUro.optimizer.conf.model.ParameterDumpModel;
import de.hs.mannheim.modUro.optimizer.conf.utils.ParameterDumpHelper;
import de.hs.mannheim.modUro.optimizer.conf.utils.ParameterDumpHelperImpl;
import fr.inria.optimization.cmaes.CMAEvolutionStrategy;
import fr.inria.optimization.cmaes.fitness.IObjectiveFunction;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CmaesOptimizerImpl implements CmaesOptimizer {

    /***
     *
     * Folgende Parameter einer ParameterDump.dat müssen durch dem CmaesOptimizer optimiert werden:
     *
     * ExecConfig: NONE
     * SdBpaCdiInDa(Model):[adhEnergy,adhFactor]
     * CellType ("Medium"): NONE
     * CellType ("BasalMembrane"): NONE
     * CellType ("Stem"): [growthVolumePerDay(double),necrosisProb(double),nutrientRequirement(double),surFit(double),volFit(double)]
     *  CellType ("Basal"): [growthVolumePerDay(double),necrosisProb(double),nutrientRequirement(double),surFit(double),volFit(double)]
     * CellType ("Intermediate"): [growthVolumePerDay(double),necrosisProb(double),nutrientRequirement(double),surFit(double),volFit(double)]
     * CellType ("Umbrella"): [growthVolumePerDay(double),necrosisProb(double),nutrientRequirement(double),surFit(double),volFit(double)]
     **/

    private String cmaesPropertiesPathString;

    private Double modelAdhEnergy;
    private Double modelAdhFactor;

    private Double cellStemGrothVolumePerDay;
    private Double cellStemNecrosisProb;
    private Double cellStemnutrientRequirement;
    private Double cellStemSurFit;
    private Double cellStemVolFit;

    private Double cellBasalGrothVolumePerDay;
    private Double cellBasalNecrosisProb;
    private Double cellBasalnutrientRequirement;
    private Double cellBasalSurFit;
    private Double cellBasalVolFit;

    private Double cellIntermediateGrothVolumePerDay;
    private Double cellIntermediateNecrosisProb;
    private Double cellIntermediatenutrientRequirement;
    private Double cellIntermediateSurFit;
    private Double cellIntermediateVolFit;

    private Double cellUmbrellaGrothVolumePerDay;
    private Double cellUmbrellaNecrosisProb;
    private Double cellUmbrellaNutrientRequirement;
    private Double cellUmbrellaSurFit;
    private Double cellUmbrellaVolFit;

    private final String[] CMAES_PARAMETER_FIELD_NAMES_IN_REQUIRED_ORDER = {
            "modelAdhEnergy",
            "modelAdhFactor",
            "cellStemGrothVolumePerDay",
            "cellStemNecrosisProb",
            "cellStemnutrientRequirement",
            "cellStemSurFit",
            "cellStemVolFit",
            "cellBasalGrothVolumePerDay",
            "cellBasalNecrosisProb",
            "cellBasalnutrientRequirement",
            "cellBasalSurFit",
            "cellBasalVolFit",
            "cellIntermediateGrothVolumePerDay",
            "cellIntermediateNecrosisProb",
            "cellIntermediatenutrientRequirement",
            "cellIntermediateSurFit",
            "cellIntermediateVolFit",
            "cellUmbrellaGrothVolumePerDay",
            "cellUmbrellaNecrosisProb",
            "cellUmbrellaNutrientRequirement",
            "cellUmbrellaSurFit",
            "cellUmbrellaVolFit"};

    /***
     * Will extract values from a parameterDump and optimize those values using a given function.
     * Will return a new ParameterDump including optimized values.
     * @param originParameterDump
     * @param cmaFunction
     * @return
     */
    @Override
    public ParameterDump optimizeParameterDump(ParameterDump originParameterDump, IObjectiveFunction cmaFunction,
                                               String propertiesFileName) {
        this.cmaesPropertiesPathString = propertiesFileName;
        try {
            System.out.println("Entering optimizeParameterDump()");
            if (cmaFunction == null) {
                throw new RuntimeException("CmaFunction is invalid. No function found to optimize ParameterDump. Aborting.");
            }

            if (originParameterDump == null) {
                throw new RuntimeException("ParameterDump is null. Aborting.");
            }

            System.out.println("Extracting Values of ParameterDump to fields");
            extractParameterDumpValuesToFields(originParameterDump);
            System.out.println("Finished extracting values of ParameterDump to fields.");

            final double[] cmaFunctionParameters = getCmaFunctionParameters();
            double[] optimizedParameters = calculateOptimum(cmaFunction, cmaFunctionParameters);
            // this is to proove the reflective method works properly
            //double[] optimizedManually = getCmaFunctionParameterManually();
            System.out.println("Converting optimized parameters to ParameterDump");
            ParameterDump optimizedParameterDump = transformToParameterDump(originParameterDump, optimizedParameters);
            System.out.println("Done converting.");
            return optimizedParameterDump;
        } finally {
            System.out.println("exit optimizeParameterDump()");
        }
    }

    private double[] getCmaFunctionParameters() {
        System.out.println("Loading parameters for cmaes optimization based on fields parsed from ParameterDump.dat.");
        BiMap<Integer, Field> indexFieldBiMap = generateBiFieldIndexHashmap();
        List<Double> fieldValuesList = new ArrayList<>();
        for (Field field : indexFieldBiMap.values()) {
            try {
                field.setAccessible(true);
                Object valueOfField = field.get(this);
                if (!(valueOfField instanceof Double)) {
                    throw new RuntimeException(String.format("%s not instance of Double", field.getName()));
                }
                fieldValuesList.add((Double) valueOfField);
                field.setAccessible(false);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        // cms optimizer requires primitive type
        // source: http://stackoverflow.com/questions/6018267/how-to-cast-from-listdouble-to-double-in-java
        return fieldValuesList.stream().mapToDouble(d -> d).toArray();
    }

    private BiMap<Integer, Field> generateBiFieldIndexHashmap() {
        BiMap<Integer, Field> fieldsWithIndexMapResult = HashBiMap.create();

        Integer fieldNameIndex = 0;
        for (String fieldName : CMAES_PARAMETER_FIELD_NAMES_IN_REQUIRED_ORDER) {
            System.out.println("Adding field " + fieldNameIndex + " as index " + fieldNameIndex);
            try {
                fieldsWithIndexMapResult.put(fieldNameIndex, this.getClass().getDeclaredField(fieldName));
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
            fieldNameIndex++;
        }

        System.out.println("done generating indice map of required deserilazation fields.");
        return fieldsWithIndexMapResult;
    }

    private ParameterDump transformToParameterDump(ParameterDump aParameterDump, double[] optimizedParameters) {
        System.out.println("Generating optimized ParameterDump, based on optimized Parameters");

        if (aParameterDump == null) {
            throw new RuntimeException("Origin ParameterDump is missing");
        }

        if (optimizedParameters == null) {
            throw new RuntimeException("OptimizedParameters are missing");
        }

        BiMap<Integer, Field> fieldIndexHashmap = generateBiFieldIndexHashmap();
        if (optimizedParameters.length != fieldIndexHashmap.values().size()) {
            throw new RuntimeException(
                    String.format("OptimizedParameters size: %s doesn't match with required size: %s",
                            optimizedParameters.length, fieldIndexHashmap.size()));
        }

        // set optimized values as new field values.
        extractOptimizedParametersToFields(optimizedParameters, fieldIndexHashmap);

        // create parameterDump from field values
        ParameterDumpHelper parameterDumpHelper = new ParameterDumpHelperImpl();
        Collection<ParameterDumpCellType> newParameterDumpCellTypes =
        getParameterDumpCellTypesByFields(aParameterDump, parameterDumpHelper);
        aParameterDump.setParameterDumpCellTypeList(newParameterDumpCellTypes);
        ParameterDumpModel parameterDumpModel = aParameterDump.getParameterDumpModel();
        parameterDumpModel.setAdhEnergy(modelAdhEnergy);
        parameterDumpModel.setAdhFactor(modelAdhFactor);
        return aParameterDump;
    }

    private void extractOptimizedParametersToFields(double[] optimizedParameters, BiMap<Integer, Field> fieldIndexHashmap) {
        for (Field field : fieldIndexHashmap.values()) {
            field.setAccessible(true);
            try {
                double valueToSet = optimizedParameters[fieldIndexHashmap.inverse().get(field)];
                System.out.println(String.format("Setting value of Field %s from %f to %f",
                        field.getName(), field.get(this),valueToSet));
                field.set(this, valueToSet);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
            field.setAccessible(false);
        }
    }

    /***
     * Builds Collection of CellTypes based on the current fields of CmaesOptimizerImpl.
     * Make sure you have run the optimization methods before calling this method.
     * @param newParameterDump
     * @param parameterDumpHelper
     */
    private Collection<ParameterDumpCellType> getParameterDumpCellTypesByFields(ParameterDump newParameterDump, ParameterDumpHelper parameterDumpHelper) {
        System.out.println("Generating CellTypes based on current field values");
        ParameterDumpCellType cellTypeStem = parameterDumpHelper.getCellTypeStem(newParameterDump);
        ParameterDumpCellType cellTypeBasal = parameterDumpHelper.getCellTypeBasal(newParameterDump);
        ParameterDumpCellType cellTypeIntermediate = parameterDumpHelper.getCellTypeIntermediate(newParameterDump);
        ParameterDumpCellType cellTypeUmbrella = parameterDumpHelper.getCellTypeUmbrella(newParameterDump);

        // won't be updated
        ParameterDumpCellType cellTypeBasalMembrane = parameterDumpHelper.getCellTypeBasalMembrane(newParameterDump);
        ParameterDumpCellType cellTypeMedium = parameterDumpHelper.getCellTypeMedium(newParameterDump);

        cellTypeStem.setGrowthVolumePerDay(cellStemGrothVolumePerDay);
        cellTypeStem.setNecrosisProb(cellStemNecrosisProb);
        cellTypeStem.setNutrientRequirement(cellStemnutrientRequirement);
        cellTypeStem.setSurFit(cellStemSurFit);
        cellTypeStem.setVolFit(cellStemVolFit);

        cellTypeBasal.setGrowthVolumePerDay(cellBasalGrothVolumePerDay);
        cellTypeBasal.setNecrosisProb(cellBasalNecrosisProb);
        cellTypeBasal.setNutrientRequirement(cellBasalnutrientRequirement);
        cellTypeBasal.setSurFit(cellBasalSurFit);
        cellTypeBasal.setVolFit(cellBasalVolFit);

        cellTypeIntermediate.setGrowthVolumePerDay(cellIntermediateGrothVolumePerDay);
        cellTypeIntermediate.setNecrosisProb(cellIntermediateNecrosisProb);
        cellTypeIntermediate.setNutrientRequirement(cellIntermediatenutrientRequirement);
        cellTypeIntermediate.setSurFit(cellIntermediateSurFit);
        cellTypeIntermediate.setVolFit(cellIntermediateVolFit);

        cellTypeUmbrella.setGrowthVolumePerDay(cellUmbrellaGrothVolumePerDay);
        cellTypeUmbrella.setNecrosisProb(cellUmbrellaNecrosisProb);
        cellTypeUmbrella.setNutrientRequirement(cellUmbrellaNutrientRequirement);
        cellTypeUmbrella.setSurFit(cellUmbrellaSurFit);
        cellTypeUmbrella.setVolFit(cellUmbrellaVolFit);

        return Arrays.asList(cellTypeStem, cellTypeBasal, cellTypeIntermediate, cellTypeUmbrella,
                cellTypeBasalMembrane, cellTypeMedium);
    }


    @Override
    public ParameterDump optimizeParameterDump(ParameterDump originParameterDump, IObjectiveFunction cmaFunction) {
        System.out.println("Running Optimization with default parameters.");
        return this.optimizeParameterDump(originParameterDump, cmaFunction, StringUtils.EMPTY);
    }

    private void extractParameterDumpValuesToFields(ParameterDump originParameterDump) {
        try {
            System.out.println("Loading ParameterDump data...");
            modelAdhEnergy = originParameterDump.getParameterDumpModel().getAdhEnergy();
            modelAdhFactor = originParameterDump.getParameterDumpModel().getAdhFactor();

            // CELL TYPE STEM START
            Collection<ParameterDumpCellType> stemCellTypeCollection =
                    Collections2.filter(originParameterDump.getParameterDumpCellTypeList(),
                            new Predicate<ParameterDumpCellType>() {
                                @Override
                                public boolean apply(ParameterDumpCellType parameterDumpCellType) {
                                    return StringUtils.equalsIgnoreCase(parameterDumpCellType.getName(), "Stem");
                                }
                            });
            ParameterDumpCellType cellTypeStem = (ParameterDumpCellType) stemCellTypeCollection.toArray()[0];
            cellStemGrothVolumePerDay = cellTypeStem.getGrowthVolumePerDay();
            cellStemNecrosisProb = cellTypeStem.getNecrosisProb();
            cellStemnutrientRequirement = cellTypeStem.getNutrientRequirement();
            cellStemSurFit = cellTypeStem.getSurFit();
            cellStemVolFit = cellTypeStem.getVolFit();
            // CELL TYPE STEM END

            // CELL TYPE UMBRELLA START
            Collection<ParameterDumpCellType> umbrellaCellTypeCollection = Collections2.filter(originParameterDump.getParameterDumpCellTypeList(), new Predicate<ParameterDumpCellType>() {
                @Override
                public boolean apply(ParameterDumpCellType parameterDumpCellType) {
                    return StringUtils.equalsIgnoreCase(parameterDumpCellType.getName(), "Umbrella");
                }
            });
            ParameterDumpCellType cellTypeUmbrella = (ParameterDumpCellType) umbrellaCellTypeCollection.toArray()[0];
            cellUmbrellaGrothVolumePerDay = cellTypeUmbrella.getGrowthVolumePerDay();
            cellUmbrellaNecrosisProb = cellTypeUmbrella.getNecrosisProb();
            cellUmbrellaNutrientRequirement = cellTypeUmbrella.getNutrientRequirement();
            cellUmbrellaSurFit = cellTypeUmbrella.getSurFit();
            cellUmbrellaVolFit = cellTypeUmbrella.getVolFit();
            // CELL TYPE UMBRELLA END

            // CELL TYPE BASAL START
            Collection<ParameterDumpCellType> basalCellTypeCollection = Collections2.filter(originParameterDump.getParameterDumpCellTypeList(), new Predicate<ParameterDumpCellType>() {
                @Override
                public boolean apply(ParameterDumpCellType parameterDumpCellType) {
                    return StringUtils.equalsIgnoreCase(parameterDumpCellType.getName(), "Basal");
                }
            });
            ParameterDumpCellType cellTypeBasal = (ParameterDumpCellType) umbrellaCellTypeCollection.toArray()[0];
            cellBasalGrothVolumePerDay = cellTypeBasal.getGrowthVolumePerDay();
            cellBasalNecrosisProb = cellTypeBasal.getNecrosisProb();
            cellBasalnutrientRequirement = cellTypeBasal.getNutrientRequirement();
            cellBasalSurFit = cellTypeBasal.getSurFit();
            cellBasalVolFit = cellTypeBasal.getVolFit();
            // CELL TYPE BASAL END

            // CELL TYPE INTERMEDIATE START
            Collection<ParameterDumpCellType> intermediateCellTypeCollection = Collections2.filter(originParameterDump.getParameterDumpCellTypeList(), new Predicate<ParameterDumpCellType>() {
                @Override
                public boolean apply(ParameterDumpCellType parameterDumpCellType) {
                    return StringUtils.equalsIgnoreCase(parameterDumpCellType.getName(), "Intermediate");
                }
            });
            ParameterDumpCellType cellTypeIntermediate = (ParameterDumpCellType) intermediateCellTypeCollection.toArray()[0];
            cellIntermediateGrothVolumePerDay = cellTypeIntermediate.getGrowthVolumePerDay();
            cellIntermediateNecrosisProb = cellTypeIntermediate.getNecrosisProb();
            cellIntermediatenutrientRequirement = cellTypeIntermediate.getNutrientRequirement();
            cellIntermediateSurFit = cellTypeIntermediate.getSurFit();
            cellIntermediateVolFit = cellTypeIntermediate.getVolFit();
            // CELL TYPE INTERMEDIATE END

        } finally {
            System.out.println("exit extractParameterDumpValuesToFields");
        }
    }

    public double[] calculateOptimum(IObjectiveFunction optimizeFunction, double[] originParameters) {
        try {
            System.out.println("Enter calculateOptimum()");
            System.out.println("Calculating Optimum value by function: " + optimizeFunction.getClass().getName());
            CMAEvolutionStrategy cma = new CMAEvolutionStrategy();
            cma.readProperties();

            System.out.println("Properties loaded.");
            //set dimensions
            cma.setDimension(originParameters.length);
            //set typicalX
            //cma.setInitialX(originParameters);
            cma.setTypicalX(originParameters);

            cma.setInitialStandardDeviation(0.2); // also a mandatory setting
            cma.options.stopFitness = 1e-14;       // optional setting
            System.out.println("Now entering initializing. No parameter entry possible.");
            double[] fitness = cma.init();  // new double[cma.parameters.getPopulationSize()];
            System.out.println("Initializing finished");
            // initial output to files
            cma.writeToDefaultFilesHeaders(0); // 0 == overwrites old files

            // iteration loop
            while (cma.stopConditions.getNumber() == 0) {

                // --- core iteration step ---
                double[][] pop = cma.samplePopulation(); // get a new population of solutions
                for (int i = 0; i < pop.length; ++i) {    // for each candidate solution i
                    // a simple way to handle constraints that define a convex feasible domain
                    // (like box constraints, i.e. variable boundaries) via "blind re-sampling"
                    // assumes that the feasible domain is convex, the optimum is
                    while (!optimizeFunction.isFeasible(pop[i]))     //   not located on (or very close to) the domain boundary,
                        pop[i] = cma.resampleSingle(i);    //   initialX is feasible and initialStandardDeviations are
                    //   sufficiently small to prevent quasi-infinite looping here
                    // compute fitness/objective value
                    fitness[i] = optimizeFunction.valueOf(pop[i]); // fitfun.valueOf() is to be minimized
                }
                cma.updateDistribution(fitness);         // pass fitness array to update search distribution
                // --- end core iteration step ---

                // output to files and console
                cma.writeToDefaultFiles();
                int outmod = 150;
                if (cma.getCountIter() % (15 * outmod) == 1)
                    cma.printlnAnnotation(); // might write file as well
                if (cma.getCountIter() % outmod == 1)
                    cma.println();
            }
            // evaluate mean value as it is the best estimator for the optimum
            cma.setFitnessOfMeanX(optimizeFunction.valueOf(cma.getMeanX())); // updates the best ever solution

            // final output
            cma.writeToDefaultFiles(1);
            cma.println();
            cma.println("Terminated due to");
            for (String s : cma.stopConditions.getMessages())
                cma.println("  " + s);
            cma.println("best function value " + cma.getBestFunctionValue()
                    + " at evaluation " + cma.getBestEvaluationNumber());

            // we might return cma.getBestSolution() or cma.getBestX()
            for (int iterator = 0; iterator < originParameters.length; iterator++) {
                System.out.println("This is the best x for dimension " + iterator + ": " + cma.getBestX()[iterator]);
            }
            //System.out.println("This is the best x for every dimension"+ cma.getBestX());
            return cma.getBestX();
        } finally {
            System.out.println("exit calculateOptimum");
        }
    }

    public String getCmaesPropertiesPathString() {
        return cmaesPropertiesPathString;
    }

    public void setCmaesPropertiesPathString(String cmaesPropertiesPathString) {
        this.cmaesPropertiesPathString = cmaesPropertiesPathString;
    }

    public Double getModelAdhEnergy() {
        return modelAdhEnergy;
    }

    public void setModelAdhEnergy(Double modelAdhEnergy) {
        this.modelAdhEnergy = modelAdhEnergy;
    }

    public Double getModelAdhFactor() {
        return modelAdhFactor;
    }

    public void setModelAdhFactor(Double modelAdhFactor) {
        this.modelAdhFactor = modelAdhFactor;
    }

    public Double getCellStemGrothVolumePerDay() {
        return cellStemGrothVolumePerDay;
    }

    public void setCellStemGrothVolumePerDay(Double cellStemGrothVolumePerDay) {
        this.cellStemGrothVolumePerDay = cellStemGrothVolumePerDay;
    }

    public Double getCellStemNecrosisProb() {
        return cellStemNecrosisProb;
    }

    public void setCellStemNecrosisProb(Double cellStemNecrosisProb) {
        this.cellStemNecrosisProb = cellStemNecrosisProb;
    }

    public Double getCellStemnutrientRequirement() {
        return cellStemnutrientRequirement;
    }

    public void setCellStemnutrientRequirement(Double cellStemnutrientRequirement) {
        this.cellStemnutrientRequirement = cellStemnutrientRequirement;
    }

    public Double getCellStemSurFit() {
        return cellStemSurFit;
    }

    public void setCellStemSurFit(Double cellStemSurFit) {
        this.cellStemSurFit = cellStemSurFit;
    }

    public Double getCellStemVolFit() {
        return cellStemVolFit;
    }

    public void setCellStemVolFit(Double cellStemVolFit) {
        this.cellStemVolFit = cellStemVolFit;
    }

    public Double getCellBasalGrothVolumePerDay() {
        return cellBasalGrothVolumePerDay;
    }

    public void setCellBasalGrothVolumePerDay(Double cellBasalGrothVolumePerDay) {
        this.cellBasalGrothVolumePerDay = cellBasalGrothVolumePerDay;
    }

    public Double getCellBasalNecrosisProb() {
        return cellBasalNecrosisProb;
    }

    public void setCellBasalNecrosisProb(Double cellBasalNecrosisProb) {
        this.cellBasalNecrosisProb = cellBasalNecrosisProb;
    }

    public Double getCellBasalnutrientRequirement() {
        return cellBasalnutrientRequirement;
    }

    public void setCellBasalnutrientRequirement(Double cellBasalnutrientRequirement) {
        this.cellBasalnutrientRequirement = cellBasalnutrientRequirement;
    }

    public Double getCellBasalSurFit() {
        return cellBasalSurFit;
    }

    public void setCellBasalSurFit(Double cellBasalSurFit) {
        this.cellBasalSurFit = cellBasalSurFit;
    }

    public Double getCellBasalVolFit() {
        return cellBasalVolFit;
    }

    public void setCellBasalVolFit(Double cellBasalVolFit) {
        this.cellBasalVolFit = cellBasalVolFit;
    }

    public Double getCellIntermediateGrothVolumePerDay() {
        return cellIntermediateGrothVolumePerDay;
    }

    public void setCellIntermediateGrothVolumePerDay(Double cellIntermediateGrothVolumePerDay) {
        this.cellIntermediateGrothVolumePerDay = cellIntermediateGrothVolumePerDay;
    }

    public Double getCellIntermediateNecrosisProb() {
        return cellIntermediateNecrosisProb;
    }

    public void setCellIntermediateNecrosisProb(Double cellIntermediateNecrosisProb) {
        this.cellIntermediateNecrosisProb = cellIntermediateNecrosisProb;
    }

    public Double getCellIntermediatenutrientRequirement() {
        return cellIntermediatenutrientRequirement;
    }

    public void setCellIntermediatenutrientRequirement(Double cellIntermediatenutrientRequirement) {
        this.cellIntermediatenutrientRequirement = cellIntermediatenutrientRequirement;
    }

    public Double getCellIntermediateSurFit() {
        return cellIntermediateSurFit;
    }

    public void setCellIntermediateSurFit(Double cellIntermediateSurFit) {
        this.cellIntermediateSurFit = cellIntermediateSurFit;
    }

    public Double getCellIntermediateVolFit() {
        return cellIntermediateVolFit;
    }

    public void setCellIntermediateVolFit(Double cellIntermediateVolFit) {
        this.cellIntermediateVolFit = cellIntermediateVolFit;
    }

    public Double getCellUmbrellaGrothVolumePerDay() {
        return cellUmbrellaGrothVolumePerDay;
    }

    public void setCellUmbrellaGrothVolumePerDay(Double cellUmbrellaGrothVolumePerDay) {
        this.cellUmbrellaGrothVolumePerDay = cellUmbrellaGrothVolumePerDay;
    }

    public Double getCellUmbrellaNecrosisProb() {
        return cellUmbrellaNecrosisProb;
    }

    public void setCellUmbrellaNecrosisProb(Double cellUmbrellaNecrosisProb) {
        this.cellUmbrellaNecrosisProb = cellUmbrellaNecrosisProb;
    }

    public Double getCellUmbrellaNutrientRequirement() {
        return cellUmbrellaNutrientRequirement;
    }

    public void setCellUmbrellaNutrientRequirement(Double cellUmbrellaNutrientRequirement) {
        this.cellUmbrellaNutrientRequirement = cellUmbrellaNutrientRequirement;
    }

    public Double getCellUmbrellaSurFit() {
        return cellUmbrellaSurFit;
    }

    public void setCellUmbrellaSurFit(Double cellUmbrellaSurFit) {
        this.cellUmbrellaSurFit = cellUmbrellaSurFit;
    }

    public Double getCellUmbrellaVolFit() {
        return cellUmbrellaVolFit;
    }

    public void setCellUmbrellaVolFit(Double cellUmbrellaVolFit) {
        this.cellUmbrellaVolFit = cellUmbrellaVolFit;
    }

    @Deprecated
    private double[] getCmaFunctionParameterManually() {
        // this method has been replaced by getCmaFunctionParameters()
        System.err.println("entering getCmaFunctionParameterManually(). This is for test purposes only.");
        return new double[]{
                modelAdhEnergy,
                modelAdhFactor,
                cellStemGrothVolumePerDay,
                cellStemNecrosisProb,
                cellStemnutrientRequirement,
                cellStemSurFit,
                cellStemVolFit,
                cellBasalGrothVolumePerDay,
                cellBasalNecrosisProb,
                cellBasalnutrientRequirement,
                cellBasalSurFit,
                cellBasalVolFit,
                cellIntermediateGrothVolumePerDay,
                cellIntermediateNecrosisProb,
                cellIntermediatenutrientRequirement,
                cellIntermediateSurFit,
                cellIntermediateVolFit,
                cellUmbrellaGrothVolumePerDay,
                cellUmbrellaNecrosisProb,
                cellUmbrellaNutrientRequirement,
                cellUmbrellaSurFit,
                cellUmbrellaVolFit,
        };
    }

}

