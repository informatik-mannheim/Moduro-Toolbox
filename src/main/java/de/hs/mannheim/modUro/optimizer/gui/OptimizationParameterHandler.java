
package de.hs.mannheim.modUro.optimizer.gui;


import de.hs.mannheim.modUro.optimizer.compucell.process.CompuCellExecutionHelper;
import de.hs.mannheim.modUro.optimizer.compucell.process.CompuCellExecutionHelperImpl;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


public class OptimizationParameterHandler {

    /**
     * This method sets a GUI interface for parameter-input
     * and handles some parameters for further optimization.
     * After entering the parameters, the cc3d-folder opens for selecting a model
     */
    public void receiveOptInputParametersFromGui() throws IOException {

        Hashtable paramInputHashTable = new Hashtable();

        System.out.println("set input parameters");
        Stage optStage = new Stage();
        // todo: welche parameter?
        optStage.setTitle("Set optimization conditions ");
        //Todo: set more parameter fields
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Scene scene = new Scene(grid, 500, 480);
        optStage.setScene(scene);

        Label paramSetInitialStandardDeviation = new Label("Initial Standard Deviation:");
        grid.add(paramSetInitialStandardDeviation, 0, 1);

        TextField paramSetInitialSandardDeviationTextField = new TextField();
        final String validStandardDeviationRegex = "0(\\.\\d+)?|1\\.0";

        paramSetInitialSandardDeviationTextField.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue.booleanValue() && !paramSetInitialSandardDeviationTextField.getText()
                    .matches(validStandardDeviationRegex)) {
                paramSetInitialSandardDeviationTextField.setText("0.3");
            }

        });
        grid.add(paramSetInitialSandardDeviationTextField, 1, 1);

        /*
        Label paramSetSeed = new Label("Seed:");
        grid.add(paramSetSeed, 0, 2);

        TextField paramSetSeedTextField = new TextField();
        final String validSeedRegex = "(?:\\b|-)([1-9]{1,2}[0]?|100)\\b";

        paramSetSeedTextField.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue.booleanValue() && !paramSetSeedTextField.getText()
                    .matches(validSeedRegex)) {
                paramSetSeedTextField.setText("default-parameter");
            }

        });
        */

        // filechooser accessing this button so we initialize it here
        Button executeCc3DButton = new Button("Start");

        // disable button until all parameters are valid and project file is valid
        executeCc3DButton.setDisable(true);

        // FILECHOOSER INIT
        FileChooser cc3dProjectFileChooser = new FileChooser();
        cc3dProjectFileChooser.setTitle("Select CompuCell3D Project");
        cc3dProjectFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".cc3d", "*.cc3d"));

        // SELECT PROJECT BUTTON INIT
        Button selectCc3DProjectButton = new Button("Select CompuCell3D Project");
        // inner class so we can pass the targetFile between the fileChooser and other instances.
        class TargetCc3DProjectFile {
            private File targetFile;

            public TargetCc3DProjectFile() {
            }


            public File getTargetFile() {
                return targetFile;
            }

            public void setTargetFile(File targetFile) {
                this.targetFile = targetFile;
            }
        }
        final TargetCc3DProjectFile targetCc3DProjectFile = new TargetCc3DProjectFile();

        selectCc3DProjectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //put the TextFiel inputs to the HashTable by handling the StartButton
                paramInputHashTable.put(paramSetInitialStandardDeviation,paramSetInitialSandardDeviationTextField.getCharacters());

                targetCc3DProjectFile.setTargetFile(null);
                executeCc3DButton.setDisable(true);

                System.out.println("Select CompuCell3D Project");
                File cc3DPojectFile = cc3dProjectFileChooser.showOpenDialog(optStage);

                if (cc3DPojectFile == null
                        || !cc3DPojectFile.exists()
                        || cc3DPojectFile.isDirectory()
                        || !cc3DPojectFile.getName().endsWith(".cc3d")
                        ) {
                    System.err.println("Target file is invalid. Cannot start cd3d.");
                    return;
                }

                System.out.println("Target file is: " + cc3DPojectFile.getAbsolutePath());
                targetCc3DProjectFile.setTargetFile(cc3DPojectFile);
                // todo: add info text with path to scale so the user knows the file he has selected
                executeCc3DButton.setDisable(false);
            }
        });
        grid.add(selectCc3DProjectButton, 1, 4);
        grid.add(executeCc3DButton, 2, 4);

        // EXECUTION BUTTON INIT
        executeCc3DButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                optStage.close();
                System.out.println("Starting cc3d button activated. tartet project file is: "
                        + targetCc3DProjectFile.getTargetFile().getAbsolutePath());

                System.out.println("building target process");

                CompuCellExecutionHelper cc3DExecutionHelper = new CompuCellExecutionHelperImpl();
                ProcessBuilder compuCellProcessBuilder =
                        cc3DExecutionHelper.getCompuCellProcessBuilder(targetCc3DProjectFile.getTargetFile());
                compuCellProcessBuilder.inheritIO();
                try {
                    System.out.println("executing target process");
                    compuCellProcessBuilder.start();
                    compuCellProcessBuilder.redirectOutput(new File("C:\\test.txt"));
                } catch (IOException e) {
                    System.err.println("error while executing cc3d-Process: " + e.getMessage());
                }
            }
        });
        optStage.show();
    }

    /**
     * This method uses the cc3d-parameter-dump-files for
     * researching information about the last iteration data and uses it
     * for further optimization process.
     *
     * @return Array of doubles with model-own optimization parameters
     * <p>
     * todo: list of extracted parameters
     * @throws IOException
     */
    public double[] extractParameterDumpValues() throws IOException {

        // loading ParameterDump.dat
        // todo: harter pfad (garnicht erst anfangen mit sowas)
        File paramDump = new File("C://Users//Station//Desktop//ParameterDump.dat");

        //File paramDump = new File("ParameterDump.dat");
        BufferedReader parameterDumpFileReader = new BufferedReader(new FileReader(paramDump));
        MultiMap parameterDumpKeyValuePairs = new MultiValueMap();

        List<String> paramDumpKeyList = new ArrayList<String>();
        String currentLine = "";
        while((currentLine = parameterDumpFileReader.readLine()) != null){

            if (currentLine.isEmpty()) {
                continue;
            }
            if(!currentLine.contains("adhEnergy")&&
                    !currentLine.contains("adhFactor")&&
                    !currentLine.contains("growthVolumePerDay")&&
                    !currentLine.contains("nutrientRequirement")&&
                    !currentLine.contains("surFit")&&
                    !currentLine.contains("volFit")){
                continue;
            }
            String parts[] = currentLine.split(":",-1);
            parameterDumpKeyValuePairs.put(parts[0],parts[1]);
            parts[1] = parts[1].trim();
            paramDumpKeyList.add(parts[1]);
            System.out.println(parts[0] +" -> "+ parts[1]);
        }
        String [] stringArrayToOptimize =paramDumpKeyList.toArray(new String[0]);
        System.out.println(stringArrayToOptimize);

        //parse to double[]
        double[] doubleArrayToOptimize = new double[stringArrayToOptimize.length];
        for (int i = 0; i<stringArrayToOptimize.length; i++) {
            doubleArrayToOptimize[i] = Double.valueOf(stringArrayToOptimize[i]);
            System.out.println(doubleArrayToOptimize[i]);
        }

        return doubleArrayToOptimize;
    }

 }