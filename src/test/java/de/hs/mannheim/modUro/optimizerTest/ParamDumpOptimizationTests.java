package de.hs.mannheim.modUro.optimizerTest;


import de.hs.mannheim.modUro.optimizer.cmaes.CmaesOptimizer;
import de.hs.mannheim.modUro.optimizer.cmaes.CmaesOptimizerImpl;
import de.hs.mannheim.modUro.optimizer.cmaes.functions.CmaFunction;
import de.hs.mannheim.modUro.optimizer.conf.model.ParameterDump;
import de.hs.mannheim.modUro.optimizer.parameters.ParameterDumpReaderImpl;
import fr.inria.optimization.cmaes.fitness.IObjectiveFunction;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class ParamDumpOptimizationTests {

    @Test
    public void parameterDumpParserTest() {
        ParameterDumpReaderImpl parameterDumpReaderImpl = new ParameterDumpReaderImpl();

        File parameterTestDumpFile = new File(this.getClass()
                .getClassLoader().getResource("example_ParameterDump.dat").getFile());
        if (parameterTestDumpFile == null || !parameterTestDumpFile.exists()) {
            Assert.assertFalse(false);
        }

        ParameterDump parameterDump = parameterDumpReaderImpl.parseParamDump(parameterTestDumpFile);
        Assert.assertEquals(6,parameterDump.getParameterDumpCellTypeList().size());
        CmaesOptimizer cmaesOptimizerImpl = new CmaesOptimizerImpl();
        ParameterDump optimizedParameterDump = cmaesOptimizerImpl.optimizeParameterDump(parameterDump, new CmaFunction.UroFunction());
    }

    @Test
    public void doubleParseEStringTest() {
        final String eTestValue = "2e-05";
        Double result = Double.parseDouble(eTestValue);
        Assert.assertEquals(Double.isNaN(result), false);
    }

    @Test
    public void optimizerCalcTest(){
        CmaesOptimizer cmaesOptimizerImpl = new CmaesOptimizerImpl();
        IObjectiveFunction uroFunc = new CmaFunction.Rosenbrock();
        double [] testParams = {1.2,3.4,5.5,8,8};
        System.out.println(cmaesOptimizerImpl.calculateOptimum(uroFunc,testParams));
    }
}
