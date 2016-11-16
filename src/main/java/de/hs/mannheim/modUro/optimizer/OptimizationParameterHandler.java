
package de.hs.mannheim.modUro.optimizer;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Station on 16.11.2016.
 */
public class OptimizationParameterHandler {

    /**
     * This method sets a GUI interface for parameter-input
     * and handles the parameters for further optimization.
     * After entering the parameters, the cc3d-folder opens for selecting a model
     */
    // todo: Refactor
    // todo: Liest parameter aus und verwaltet Teile der Logik //Liest Parameter ein!
    public void receiveOptInputParametersFromGui() throws IOException {
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

        Label param1 = new Label("Param1:");
        grid.add(param1, 0, 1);

        TextField param1TextField = new TextField();
        param1TextField.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            // todo: extract regex as constant
            if (!newValue.booleanValue() && !param1TextField.getText().matches("[1-5](\\.[0-9]{1,2}){0,1}|6(\\.0{1,2}){0,1}")) {
                param1TextField.setText("default-parameter");
            }

        });
        grid.add(param1TextField, 1, 1);
        // todo: params richtig benennen
        Label param2 = new Label("Param2:");
        grid.add(param2, 0, 2);

        TextField param2TextField = new TextField();
        param2TextField.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            // todo: extract regex as constant
            if (!newValue.booleanValue() && !param2TextField.getText().matches("[1-5](\\.[0-9]{1,2}){0,1}|6(\\.0{1,2}){0,1}")) {
                param2TextField.setText("default-parameter");
            }

        });
        grid.add(param2TextField, 1, 2);
        Label param3 = new Label("Param3:");
        grid.add(param3, 0, 3);

        TextField param3TextField = new TextField();
        param3TextField.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue.booleanValue() && !param3TextField.getText().matches("[1-5](\\.[0-9]{1,2}){0,1}|6(\\.0{1,2}){0,1}")) {
                param3TextField.setText("default-parameter");
            }

        });
        grid.add(param3TextField, 1, 3);

        // todo: file-select statt explorer aufmachen

        // todo: Button-Text sagt nichts darüber aus, was als nächstes passieren wird
        Button okParam = new Button("Hit me!");
        grid.add(okParam, 2, 4);
        // todo: da ggf. mal schauen ob es so gut ist, direkt thread.sleep aufzurufen (solangs nicht blockiert mir wurst)
        okParam.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //TODO: save added parameters and quit this window
                optStage.close();

                OptimizationProcessHelper openMyModel = new OptimizationProcessHelper();
                openMyModel.openModel();

                //TODO: if model has opened, start th eparameterDump-stuff
                //TODO: this threading is only temporary
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
        //TODO: set correct absolute path
        //File inputFile = new File("ParameterDump.dat");

        // loading ParameterDump.dat
        // todo: harter pfad (garnicht erst anfangen mit sowas)
        File inputFile = new File("C://Users//Station//Desktop//ParameterDump.dat");

        BufferedReader parameterDumpFileReader = new BufferedReader(new FileReader(inputFile));
        String currentParamKeyValueSet;
        List<String> parameterDumpProperties = new ArrayList<String>();
        List<String> parameterDumpValues = new ArrayList<String>();

        // todo: Lösungsansatz
        // 1) werte in HashMap<String, String> speichern
        // 2) line auslesen und split :
        // 3) key:value dh res[0] wird zum key , res[1] zum value.
        // wenn Wert nicht vorhanden - StringUtils.EMPTY nehmen (entspricht "")
        // 4) hashmap.put(res[0],res.length>1?res[1].trim() :  StringUtils.EMPTY)
        // dann hast du eine Hashmap, deren KeyValues man direkt auslesen kann

        // bsp flip2DimRatio: 0.5 -> hashmap.put("flip2DimRatio", " 0.5".trim();
        // hashmap.get("flip2DimRatio") -> "0.5"
        // Danach iterieren über alle values in der Liste. Mit StringUtils checken "isnNotBlank" und "isNumeric".
        // wenn numeric -> in Double, oder was auch immer, parsen.

        // profit
        HashMap<String, String> parameterDumpKeyValues = new HashMap<>();


        String currentLine;
        while ((currentLine = parameterDumpFileReader.readLine()) != null) {
            if(currentLine.isEmpty()){
                continue;
            }
            String[] res = currentLine.split(":", -1);
            parameterDumpProperties.add(res[0]);
            parameterDumpValues.add(res[1]);
        }

        List<String> tmpParameterArray = new ArrayList<String>();
        for (String object: parameterDumpValues) {
            String trimmedString= object.trim();
            if(object.isEmpty()){
             //   object.remove();
            }

            // todo: Apache StringUtils -> is numeric()==true hätte das gleiche getan
            // todo: Unit-tests mit 2e-05

            //if(trimmedString.isNumeric()==true){
            //    trimmedString.remove();
            //tmpParameterArray.add(trimmedString)
           // }
            if(trimmedString.matches("-?\\d+([.]{1}\\d+)?")) {
                System.out.println(trimmedString);
                tmpParameterArray.add(trimmedString);
            }
        }

        List<Double> arrayToOptimize = new ArrayList<Double>();
        for(String object:tmpParameterArray){
            double value = Double.parseDouble(object);
            arrayToOptimize.add(value);
        }
        for(String object:tmpParameterArray){
            //parse to double??
            double value = Double.parseDouble(object);
            arrayToOptimize.add(value);
        }
        double[] targetArrayToOptimize = new double[arrayToOptimize.size()];
        for (int i = 0; i < targetArrayToOptimize.length; i++) {
            targetArrayToOptimize[i] = arrayToOptimize.get(i);                // java 1.5+ style (outboxing)
        }

        return targetArrayToOptimize;


    }
}