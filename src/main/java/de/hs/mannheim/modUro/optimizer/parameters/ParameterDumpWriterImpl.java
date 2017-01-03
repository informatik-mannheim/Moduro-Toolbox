package de.hs.mannheim.modUro.optimizer.parameters;


import de.hs.mannheim.modUro.optimizer.conf.model.ParameterDump;
import de.hs.mannheim.modUro.optimizer.conf.model.ParameterDumpCellType;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class ParameterDumpWriterImpl implements ParameterDumpWriter {

    private final String DEFAULT_NAME_PARAMETER_DUMP = "ParameterDump.dat";
    private final String NEWLINE = "\n";

    @Override
    public void writeParameterDump(File targetFile, ParameterDump parameterDump) {
        try {
            System.out.println("Start ParameterDumpWriterImpl");

            if (targetFile == null) {
                throw new RuntimeException("ParameterDump Target is missing.");
            }

            if (parameterDump == null) {
                throw new RuntimeException("ParameterDump file to write is missing");
            }

            System.out.println("Writing ParameterDump to: " + targetFile.getAbsolutePath());
            if (targetFile.isDirectory()) {
                System.out.println("No name Set for ParameterDump. Using Default name");
            }

            if (targetFile.isFile() && targetFile.exists()) {
                System.out.println("File already exists and will be overwritten: " + targetFile.getAbsolutePath());
            }

            String targetString = getParameterDumpString(parameterDump);

            try {
                System.out.println("Writing target String to :" + targetFile.getAbsolutePath());
                FileUtils.writeStringToFile(targetFile, targetString);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        } finally {
            System.out.println("Exit ParameterDumpWriterImpl()");
        }
    }

    private String getParameterDumpString(ParameterDump parameterDump) {
        StringBuilder sb = new StringBuilder();
        // todo: append startTime
        sb.append("startTime: ").append(NEWLINE);
        sb.append(NEWLINE);
        sb.append(parameterDump.getParameterDumpExecConfig().toString());
        sb.append(NEWLINE);
        sb.append(parameterDump.getParameterDumpModel().toString());
        sb.append(NEWLINE);

        ArrayList<ParameterDumpCellType> cellTypeList = new ArrayList(parameterDump.getParameterDumpCellTypeList());
        Collections.sort(cellTypeList, (o1, o2) -> o1.getId() < o2.getId() ? -1 : 1);
        for (ParameterDumpCellType cellType : cellTypeList) {
            sb.append(cellType.toString()).append(NEWLINE).append(NEWLINE);
        }

        return sb.toString();
    }

}
