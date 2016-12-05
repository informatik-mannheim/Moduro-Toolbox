package de.hs.mannheim.modUro.optimizer.conf;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;

/**
 * Created by Station on 04.12.2016.
 */
public class ParameterDumpReader {

    static List<Double> doubleParams = new ArrayList<Double>();

    public double[] parseParamDump() {
        String paramDumpExample = "Foo:\nValue1: MeinWert1\nValue2:MeinWert2\n\nFoo2:\nValue1: MeinWert1\n";
        // ubu
        //String pathToParameterDumpFile = "/home/Station/Dokumente/ParameterDump.dat";
        String pathToParameterDumpFile = "ParameterDump.dat";
        ParameterDumpReader parameterDumpReader = new ParameterDumpReader();

        // final List<String> strings = parameterDumpReader.readFile("/home/rolli/Dokumente/ParameterDump.txt");
        parameterDumpReader.splitParameterDumpTest(pathToParameterDumpFile);
        double[] doubleArrayToOptimize = new double[doubleParams.size()];
        return doubleArrayToOptimize;

    }


    String paramDump = "Foo:\nValue1: MeinWert1\nValue2:MeinWert2\n\nFoo2:\nValue1: MeinWert1\n";
    private final String KEY_VALUE_SEPERATOR_CHAR = ":";
    private final String KEY_ENTRIES_SEPERATOR_CHAR = "\n";

    public void splitParamDumpTest() {
        // erst mal alle leerzeichen raus
        paramDump = paramDump.trim();

        final String[] paramDumpSplitByNewLine = paramDump.split(KEY_ENTRIES_SEPERATOR_CHAR);
        // jetzt ist jede Spalte für sich im array
        printParamEntries(paramDumpSplitByNewLine);

        printSeperatingLine();
        // iterieren über jede Spalte und nachsehen, was drin steht
        for (String s : paramDumpSplitByNewLine) {
            final String[] keyValueSplit = s.split(":");
            System.out.println(
                    "[" + keyValueSplit[0] + "]" + " hat " + (keyValueSplit.length > 1 ? "einen" : "keinen") + " wert");

            if (isMasterKey(keyValueSplit)) {
                System.out.println("[" + keyValueSplit[0] + "] ist ein master key");

                // todo: hier- alles was danach kommt, bis zur leerzeile gehört zusammen
                Map<String, String> masterKeyHashmap = new HashMap<String, String>();
                masterKeyHashmap.put("masterKey", keyValueSplit[0]);

            }

        }

    }

    public void splitParameterDumpTest(String pathToParameterDumpDatFile) {

        final List<String> parameterDumpLinesCollection = readFile(pathToParameterDumpDatFile);

        if (parameterDumpLinesCollection.size() == 0) {
            System.err.println("No valid parameterdump.dat input could be loaded. aborting");
            return;
        }

        // hier eine convertierung zu string array, weil zuvor immer mit einem array gearbeitet wurde. Bitte glatt ziehen (am besten array in der
        // readfile zurückgeben)

        String[] parameterDumpLines = parameterDumpLinesCollection.toArray(new String[]{});

        // die files.readlines methode hat bereits alle lines in eine Liste gehauen. darum kein aufruf mehr von
        // "getparameterlines"

        printParamEntries(parameterDumpLines);
        System.out.println("getting indices of mainKey which introduce datablocks");
        final Collection<Integer> indicesOfParentEntries = getParameterDumpParentEntriesIndices(parameterDumpLines);

        for (Integer parentEntryIndice : indicesOfParentEntries) {
            System.out.println("getting all parameters which belong to entry: " + parameterDumpLines[parentEntryIndice]);
            final Collection<String> parameterDumpDataBlock = getParameterDumpEntriesForBlock(parentEntryIndice,
                    parameterDumpLines);
            printParamEntries(parameterDumpDataBlock);
            final Map<String, String> parameterDumpDataBlockHashMap = convertParameterDumpBlockToHashmap(
                    parameterDumpDataBlock);
            printParamEntries(parameterDumpDataBlockHashMap);
        }

    }

    private void printParamEntries(String[] entries) {
        printSeperatingLine();
        System.out.println("printing array values: ");
        for (String s : entries) {
            System.out.println("[" + s.trim() + "]");
        }
        printSeperatingLine();
    }

    private void printParamEntries(Collection<String> entries) {
        printSeperatingLine();
        for (String entry : entries) {
            System.out.println(entry);
        }
        printSeperatingLine();
    }

    private void printParamEntries(Map<String, String> entries) {
        for (String key : entries.keySet()) {
            System.out.println("[" + key + "]: [" + entries.get(key) + "]");
        }
    }

    private Boolean isMasterKey(String[] parameterDumpLine) {
        return parameterDumpLine.length == 1
                && parameterDumpLine[0] != null
                && !parameterDumpLine[0].equals("");
    }

    private String[] getParameterDumpLines(String parameterDumpString) {
        String trimmedString = parameterDumpString.trim();
        return trimmedString.split(KEY_ENTRIES_SEPERATOR_CHAR);
    }

    /**
     * returns a collection of Integer which represent indices. each indice represents an line in the parameterDump.dat
     * array, which indicates the begin of a so called "block".
     *
     * A block is a collection of parameters which represent an object that will be used to configure cc3d. e.g.
     * "CellType" or "ExecConfig"
     *
     * @param parameterDumpLines
     *
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
            return new ArrayList<String>();
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
                return new HashMap<String, String>();
            }

            if (keyValuePair.length == 1) {
                System.out.println("Masterkey found");
                keyValuePairResults.put("masterKey", keyValuePair[0].trim());
                continue;
            }

            keyValuePairResults.put(keyValuePair[0].trim(), keyValuePair[1].trim());
        }
        System.out.println("hashmap contains " + keyValuePairResults.size() + " elements");

        for(Map.Entry<String, String> hashmapIterator: keyValuePairResults.entrySet()){
            if(hashmapIterator.getKey().contains("volFit")){
                double paramEntry = Double.parseDouble(hashmapIterator.getValue());
                //now put this double to an arraylist
                doubleParams.add(paramEntry);
            }
            System.out.println("Key " + hashmapIterator.getKey());
            System.out.println("Value " + hashmapIterator.getValue());
        }

        return keyValuePairResults;
    }

    private void printSeperatingLine() {
        System.out.println("#############################################################");
    }

    public List<String> readFile(String filename) {
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
