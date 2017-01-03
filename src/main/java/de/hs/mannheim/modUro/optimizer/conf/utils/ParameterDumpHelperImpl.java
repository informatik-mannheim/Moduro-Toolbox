package de.hs.mannheim.modUro.optimizer.conf.utils;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import de.hs.mannheim.modUro.optimizer.conf.model.ParameterDump;
import de.hs.mannheim.modUro.optimizer.conf.model.ParameterDumpCellType;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

public class ParameterDumpHelperImpl implements ParameterDumpHelper {

    private final String CELLTYPE_NAME_MEDIUM = "Medium";
    private final String CELLTYPE_NAME_BASAL_MEMBRANE = "BasalMembrane";
    private final String CELLTYPE_NAME_STEM = "Stem";
    private final String CELLTYPE_NAME_BASAL = "Basal";
    private final String CELLTYPE_NAME_INTERMEDIATE= "Intermediate";
    private final String CELLTYPE_NAME_UMBRElLA = "Umbrella";

    @Override
    public ParameterDumpCellType getCellTypeMedium(ParameterDump parameterDump) {
        return getFirst(parameterDump, CELLTYPE_NAME_MEDIUM);
    }

    @Override
    public ParameterDumpCellType getCellTypeBasalMembrane(ParameterDump parameterDump) {
        return getFirst(parameterDump, CELLTYPE_NAME_BASAL_MEMBRANE);
    }

    @Override
    public ParameterDumpCellType getCellTypeStem(ParameterDump parameterDump) {
        return getFirst(parameterDump, CELLTYPE_NAME_STEM);
    }

    @Override
    public ParameterDumpCellType getCellTypeBasal(ParameterDump parameterDump) {
        return getFirst(parameterDump, CELLTYPE_NAME_BASAL);
    }

    @Override
    public ParameterDumpCellType getCellTypeIntermediate(ParameterDump parameterDump) {
        return getFirst(parameterDump, CELLTYPE_NAME_INTERMEDIATE);
    }

    @Override
    public ParameterDumpCellType getCellTypeUmbrella(ParameterDump parameterDump) {
        return getFirst(parameterDump, CELLTYPE_NAME_UMBRElLA);
    }

    private ParameterDumpCellType getFirst(final ParameterDump parameterDump, final String cellTypeName) {
        Collection<ParameterDumpCellType> parameterDumpCellTypeList = parameterDump.getParameterDumpCellTypeList();
        Collection<ParameterDumpCellType> cellTypeFilterResultCollection
                = Collections2.filter(parameterDumpCellTypeList, new Predicate<ParameterDumpCellType>() {
            @Override
            public boolean apply(ParameterDumpCellType parameterDumpCellType) {
                return StringUtils.equalsIgnoreCase(cellTypeName.trim(), parameterDumpCellType.getName().trim());
            }
        });

        if (cellTypeFilterResultCollection.size() == 0) {
            throw new RuntimeException(String.format("Could not Find cellType %s in Parameterdump", cellTypeName));
        }

        System.out.println("Found " + cellTypeFilterResultCollection.size() + " items.");
        return cellTypeFilterResultCollection.iterator().next();
    }
}
