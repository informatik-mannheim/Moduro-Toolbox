package de.hs.mannheim.modUro.optimizerTest;


import de.hs.mannheim.modUro.optimizer.conf.model.ParameterDump;
import de.hs.mannheim.modUro.optimizer.parameters.ParameterDumpReaderImpl;
import fr.inria.optimization.cmaes.examples.CMAExample1;
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
        CMAExample1 cmaExample1 = new CMAExample1();


    }

    @Test
    public void doubleParseEStringTest() {
        final String eTestValue = "2e-05";
        Double result = Double.parseDouble(eTestValue);
        Assert.assertEquals(Double.isNaN(result), false);
    }
}
