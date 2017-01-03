package de.hs.mannheim.modUro.optimizerTest;


import de.hs.mannheim.modUro.optimizer.cmaes.CmaesOptimizer;
import de.hs.mannheim.modUro.optimizer.cmaes.CmaesOptimizerImpl;
import de.hs.mannheim.modUro.optimizer.cmaes.functions.CmaFunction;
import de.hs.mannheim.modUro.optimizer.conf.model.ParameterDump;
import de.hs.mannheim.modUro.optimizer.conf.model.ParameterDumpCellType;
import de.hs.mannheim.modUro.optimizer.parameters.ParameterDumpReaderImpl;
import de.hs.mannheim.modUro.optimizer.parameters.ParameterDumpWriter;
import de.hs.mannheim.modUro.optimizer.parameters.ParameterDumpWriterImpl;
import org.junit.Test;

import java.io.File;
import java.util.Collection;

public class ParameterDumpTests {

    @Test
    public void ParseAndWriteParameterDump() {
        ParameterDump parameterDump = loadParamDump();
        ParameterDumpWriter parameterDumpWriter = new ParameterDumpWriterImpl();
        File targetFile = new File("targetParameterDumpTest.dat");
        parameterDumpWriter.writeParameterDump(targetFile, parameterDump);
    }

    @Test
    public void CellTypesToStringTest() {
        ParameterDump parameterDump = loadParamDump();
        System.out.println(parameterDump.getParameterDumpExecConfig().toString());
        System.out.println(parameterDump.getParameterDumpModel().toString());
        Collection<ParameterDumpCellType> parameterDumpCellTypeList = parameterDump.getParameterDumpCellTypeList();
        for (ParameterDumpCellType cellType : parameterDumpCellTypeList) {
            System.out.println(cellType.toString());
        }
    }

    @Test
    public void ParameterDumpReadOptimizeWriteTest() {
        File targetParameterDump = new File("Test_Optimized_ParameterDump.dat");
        ParameterDump originParameterDump = loadParamDump();
        CmaesOptimizer cmaesOptimizer = new CmaesOptimizerImpl();
        ParameterDump optimizedParameterDump = cmaesOptimizer
                .optimizeParameterDump(originParameterDump, new CmaFunction.UroFunction());
        ParameterDumpWriter parameterDumpWriter = new ParameterDumpWriterImpl();
        parameterDumpWriter.writeParameterDump(targetParameterDump,optimizedParameterDump);
    }

    private ParameterDump loadParamDump() {
        String originFileString = this.getClass().getClassLoader().getResource("example_ParameterDump.dat").getFile();
        return new ParameterDumpReaderImpl().parseParamDump(new File(originFileString));
    }

}
