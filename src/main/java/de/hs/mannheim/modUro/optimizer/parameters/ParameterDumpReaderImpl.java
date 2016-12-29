package de.hs.mannheim.modUro.optimizer.parameters;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import de.hs.mannheim.modUro.optimizer.conf.model.ParameterDump;
import de.hs.mannheim.modUro.optimizer.conf.model.ParameterDumpCellType;
import de.hs.mannheim.modUro.optimizer.conf.model.ParameterDumpExecConfig;
import de.hs.mannheim.modUro.optimizer.conf.model.ParameterDumpModel;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;


public class ParameterDumpReaderImpl implements ParameterDumpReader {
    private final String KEY_VALUE_SEPERATOR_CHAR = ":";
    private final String PARAMETER_DUMP_PARSED_MASTER_KEY_KEY = "masterKey";
    private final String BLOCK_TYPE_CELLTYPE_MASTER_KEY_VALUE = "CellType";
    private final String BLOCK_TYPE_EXECCONFIG_MASTER_KEY_VALUE = "ExecConfig";

    private final int CONDITION_NUMBER_OF_CELLTYPES = 6;
    private final int CONDITION_NUMBER_OF_EXCEC_CONFIG = 1;
    private final int CONDITION_NUMBER_OF_MODEL_CONFIG = 1;

    @Override
    public ParameterDump parseParamDump(File parameterDumpFile) {
        try {
            System.out.println("entering parseParamDump");
            System.out.println("target file is: " + parameterDumpFile.getAbsolutePath());

            ParameterDump parameterDumpResult = new ParameterDump();
            Collection<Map<String, String>> parameterDumpBlockCollection = getParameterDumpBlocks(parameterDumpFile);
            System.out.println("Number of Blocks for parameterDump file: " + parameterDumpBlockCollection.size());

            parameterDumpResult.setParameterDumpCellTypeList(extractCellTypes(parameterDumpBlockCollection));
            parameterDumpResult.setParameterDumpExecConfig(extractExecConfig(parameterDumpBlockCollection));
            parameterDumpResult.setParameterDumpModel(extractModel(parameterDumpBlockCollection));
            System.out.println("Extracted all data from " + parameterDumpFile.getAbsolutePath());
            return parameterDumpResult;
        } catch (IllegalAccessException e) {
            System.err.println("Could not deserialize ParameterDumpFile: " + parameterDumpFile.getAbsolutePath());
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            System.out.println("quit parseParamDump");
        }
    }

    private ParameterDumpExecConfig extractExecConfig(Collection<Map<String, String>>
                                                              parameterDumpBlockCollection) throws IllegalAccessException {
        Collection<Map<String, String>> paramDumpExecConfigMapCollection = Collections2.filter(parameterDumpBlockCollection, new Predicate<Map<String, String>>() {
            @Override
            public boolean apply(Map<String, String> parameterDumpBlockMap) {
                String masterKeyValue = parameterDumpBlockMap.get(PARAMETER_DUMP_PARSED_MASTER_KEY_KEY);
                return StringUtils.equalsIgnoreCase(BLOCK_TYPE_EXECCONFIG_MASTER_KEY_VALUE, masterKeyValue);
            }
        });
        if (paramDumpExecConfigMapCollection.size() != CONDITION_NUMBER_OF_EXCEC_CONFIG) {
            throw new RuntimeException("Invalid count of Elements for " + BLOCK_TYPE_EXECCONFIG_MASTER_KEY_VALUE);
        }

        System.out.println("found ExecConfig entry in ParameterDump");
        for (Map<String, String> excecConfigMap : paramDumpExecConfigMapCollection) {
            System.out.println("Iterating ExecConfig Block");
            return new ParameterDumpExecConfig(excecConfigMap);
        }

        throw new RuntimeException("No Excecconfig could be created. No Entry was parsed.");
    }

    private Collection<ParameterDumpCellType> extractCellTypes(Collection<Map<String, String>>
                                                                       parameterDumpBlockCollection) throws IllegalAccessException {
        Collection<Map<String, String>> cellTypeBlocks = Collections2.filter(
                parameterDumpBlockCollection, new Predicate<Map<String, String>>() {
                    @Override
                    public boolean apply(Map<String, String> parameterDumpBlockMap) {
                        if (!parameterDumpBlockMap.keySet().contains(PARAMETER_DUMP_PARSED_MASTER_KEY_KEY)) {
                            throw new RuntimeException("Missing key 'masterkey' in ParameterDump Block: ");
                        }

                        String masterKeyValue = parameterDumpBlockMap.get(PARAMETER_DUMP_PARSED_MASTER_KEY_KEY);
                        return StringUtils.equalsIgnoreCase(BLOCK_TYPE_CELLTYPE_MASTER_KEY_VALUE, masterKeyValue);
                    }
                });

        System.out.println("Count of CellTypes found in ParameterDump: " + cellTypeBlocks.size());
        if (cellTypeBlocks.size() != CONDITION_NUMBER_OF_CELLTYPES) {
            throw new RuntimeException("Count of CellTypes does not match " + CONDITION_NUMBER_OF_CELLTYPES);
        }

        System.out.println("Deserializing CellTypes");
        Collection<ParameterDumpCellType> deSerializedCellTypes = new ArrayList<>();

        for (Map<String, String> cellTypeBlock : cellTypeBlocks) {
            ParameterDumpCellType cellType = new ParameterDumpCellType(cellTypeBlock);
            System.out.println("deserialized cellType");
            deSerializedCellTypes.add(cellType);
        }
        System.out.println("deserialized all Celltypes");
        return deSerializedCellTypes;
    }


    private ParameterDumpModel extractModel(Collection<Map<String, String>>
                                                    parameterDumpBlockCollection) throws IllegalAccessException {
        System.out.println("Filtering Blocks: " + StringUtils.join(Arrays.asList(BLOCK_TYPE_CELLTYPE_MASTER_KEY_VALUE,
                BLOCK_TYPE_EXECCONFIG_MASTER_KEY_VALUE), ","));

        Collection<Map<String, String>> modelDataMapCollection =
                Collections2.filter(parameterDumpBlockCollection, new Predicate<Map<String, String>>() {
            @Override
            public boolean apply(Map<String, String> parameterDumpBlockMap) {
                String masterKeyValue = parameterDumpBlockMap.get(PARAMETER_DUMP_PARSED_MASTER_KEY_KEY);
                return !((StringUtils.equalsIgnoreCase(masterKeyValue, BLOCK_TYPE_CELLTYPE_MASTER_KEY_VALUE)
                        || (StringUtils.equalsIgnoreCase(masterKeyValue, BLOCK_TYPE_EXECCONFIG_MASTER_KEY_VALUE))));
            }
        });

        if (modelDataMapCollection.size() != CONDITION_NUMBER_OF_MODEL_CONFIG) {
            throw new RuntimeException("Invalid count of blocks for ModelConfig: " + modelDataMapCollection.size());
        }

        for (Map<String, String> modelDataMap : modelDataMapCollection) {
            return new ParameterDumpModel(modelDataMap);
        }

        throw new RuntimeException("Could not load ModelConfiguration of ParameterDump");
    }

    private Collection<Map<String, String>> getParameterDumpBlocks(File parameterDumpDatFile) {
        System.out.println("splitting ParameterDump file.");
        if (parameterDumpDatFile == null || !parameterDumpDatFile.exists()) {
            throw new RuntimeException("Could not load ParameterDump file at: " + parameterDumpDatFile);
        }

        final List<String> parameterDumpLinesCollection = readParameterDumpFile(parameterDumpDatFile.getPath());
        System.out.println(String.format("ParameterDump: {}", StringUtils.join(parameterDumpLinesCollection, ";")));
        if (parameterDumpLinesCollection.size() == 0) {
            System.err.println("No valid parameterdump.dat input could be loaded. aborting");
            return null;
        }

        String[] parameterDumpLines = parameterDumpLinesCollection.toArray(new String[]{});
        //printParamEntries(parameterDumpLines);
        System.out.println("getting indices of mainKey which introduce datablocks");
        final Collection<Integer> indicesOfParentEntries = getParameterDumpParentEntriesIndices(parameterDumpLines);
        System.out.println(String.format("Indices of ParentElements: %s", indicesOfParentEntries.toString()));
        Collection<Map<String, String>> parameterDumpParsedBlocksCollection = new ArrayList<>();
        for (Integer parentEntryIndice : indicesOfParentEntries) {
            System.out.println("getting all parameters which belong to entry: " + parameterDumpLines[parentEntryIndice]);
            final Collection<String> parameterDumpDataBlock = getParameterDumpEntriesForBlock(parentEntryIndice,
                    parameterDumpLines);
            // printParamEntries(parameterDumpDataBlock);
            final Map<String, String> parameterDumpDataBlockHashMap = convertParameterDumpBlockToHashmap(
                    parameterDumpDataBlock);
            parameterDumpParsedBlocksCollection.add(parameterDumpDataBlockHashMap);
        }
        return parameterDumpParsedBlocksCollection;
    }

    private Boolean isMasterKey(String[] parameterDumpLine) {
        return parameterDumpLine.length == 1
                && parameterDumpLine[0] != null
                && !parameterDumpLine[0].equals("");
    }

    /**
     * returns a collection of Integer which represent indices. each indice represents an line in the parameterDump.dat
     * array, which indicates the begin of a so called "block".
     * <p>
     * A block is a collection of parameters which represent an object that will be used to configure cc3d. e.g.
     * "CellType" or "ExecConfig"
     *
     * @param parameterDumpLines
     * @return collection of indices which represents the beginning of a data block of the parameterDumpBlock
     */
    public Collection<Integer> getParameterDumpParentEntriesIndices(String[] parameterDumpLines) {
        Collection<Integer> mainKeyIndices = new ArrayList<Integer>();
        for (int i = 0; i < parameterDumpLines.length; i++) {

            if (parameterDumpLines[i] == null) {
                System.err.println("index " + i + " of parameterdump file is null");
            }

            final String[] splittedLine = parameterDumpLines[i].split(KEY_VALUE_SEPERATOR_CHAR);
            if (isMasterKey(splittedLine)) {
                mainKeyIndices.add(i);
            }
        }

        return mainKeyIndices;
    }

    /***
     * pushes all lines in a parameterdump file to a Collection. Starting from an given array, until the file has no more
     * lines or until an empty row has been found. Does also include the "master key", the value which indicates the
     * start of a parameter dump block (which should be the found in the parameterDumpLines array at the
     * indexOfMainKey-indice).
     *
     * @param indexOfMainKey - the index from which the method will start to
     * @param parameterDumpLines
     * @return Collect
     */
    public Collection<String> getParameterDumpEntriesForBlock(Integer indexOfMainKey, String[] parameterDumpLines) {

        if (indexOfMainKey == null || indexOfMainKey < 0 || indexOfMainKey > parameterDumpLines.length - 1) {
            System.err.println("could not extract parameters from entrie block, since given index is not valid: [" +
                    indexOfMainKey + "]. length of parameterDumpLines is " + parameterDumpLines.length);
            return new ArrayList<>();
        }

        Collection<String> mainKeyValues = new ArrayList<String>();
        for (int i = indexOfMainKey; i < parameterDumpLines.length; i++) {
            if (parameterDumpLines[i] == null || parameterDumpLines[i].equals("")) {
                System.out.println("index " + i + " has an empty row. Block is complete");
                return mainKeyValues;
            }

            System.out.println("Adding value " + parameterDumpLines[i] + "to collection.");
            mainKeyValues.add(parameterDumpLines[i]);
        }
        System.out.println("Last entry reached. returning collection");
        return mainKeyValues;
    }

    /***
     * converts a string which contains lines of an parameterDump.dat datablock into a map.
     *
     * @param parameterDumpValueBlock - collection of lines of a parameterdumpfileblock which belong together
     * @return a map which contains the values left of the splitting char ":" as the key and the value on the right side
     *         of the : as the value for the key
     */
    public Map<String, String> convertParameterDumpBlockToHashmap(Collection<String> parameterDumpValueBlock) {
        Map<String, String> keyValuePairResults = new HashMap<String, String>();

        System.out.println("parameter dump block contains " + parameterDumpValueBlock.size() + " elements.");
        for (String keyValueString : parameterDumpValueBlock) {
            final String[] keyValuePair = keyValueString.split(KEY_VALUE_SEPERATOR_CHAR);
            if (keyValuePair.length == 0) {
                System.err.println("error splitting line value: " + keyValueString);
                return new HashMap<>();
            }

            if (keyValuePair.length == 1) {
                System.out.println("Masterkey found");
                keyValuePairResults.put("masterKey", keyValuePair[0].trim());
                continue;
            }

            keyValuePairResults.put(keyValuePair[0].trim(), keyValuePair[1].trim());
        }
        System.out.println("hashmap contains " + keyValuePairResults.size() + " elements");
        return keyValuePairResults;
    }

    private List<String> readParameterDumpFile(String filename) {
        List<String> result = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists() || file.isDirectory()) {
            System.err.println("not a valid input file.");
            return result;
        }
        try {
            // using utf8 charset
            result = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return result;
        }
        return result;

    }
}
