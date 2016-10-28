/*
Copyright 2016 the original author or authors.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package de.hs.mannheim.modUro.controller;

import de.hs.mannheim.modUro.MainApp;
import de.hs.mannheim.modUro.controller.diagram.BoxAndWhiskerPlotController;
import de.hs.mannheim.modUro.controller.diagram.ModeltypeDiagramController;
import de.hs.mannheim.modUro.controller.diagram.SimulationDiagramController;
import de.hs.mannheim.modUro.controller.overview.ModeltypeOverviewController;
import de.hs.mannheim.modUro.controller.overview.ProjectOverviewController;
import de.hs.mannheim.modUro.controller.overview.SimulationOverviewController;
import de.hs.mannheim.modUro.model.MainModel;
import de.hs.mannheim.modUro.model.ModelType;
import de.hs.mannheim.modUro.model.Project;
import de.hs.mannheim.modUro.model.Simulation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * Controller controls the MainLayout.
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class MainController {

    @FXML
    private TreeView<String> projectTree;
    @FXML
    private Tab overviewTab;
    @FXML
    private Tab diagramTab;
    @FXML
    private Label toolboxversion;

    // List with projectData
    private ObservableList<Project> projectData;

    //RedIconForTreeItem
    Image red = new Image(getClass().getResourceAsStream("/images/red.png"));

    //References to other Controllers
    ProjectOverviewController projectOverviewController = new ProjectOverviewController();
    SimulationOverviewController simulationOverviewController = new SimulationOverviewController();
    ModeltypeOverviewController modeltypeOverviewController = new ModeltypeOverviewController();

    BoxAndWhiskerPlotController boxAndWhiskerPlotController = new BoxAndWhiskerPlotController();
    SimulationDiagramController simulationDiagramController = new SimulationDiagramController();
    ModeltypeDiagramController modeltypeDiagramController = new ModeltypeDiagramController();

    //Reference to the MainModel
    MainModel mainModel;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        mainModel = new MainModel("moduro-toolbox-settings.xml");
        //get the Project Data from Main Model
        this.projectData = FXCollections.observableArrayList(mainModel.getProjectData());

        //create the Tree with Project data items.
        createTree();

        //Listener for selected items in the TreeView.
        projectTree.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<TreeItem<String>>() {
                    @Override
                    public void changed(
                            ObservableValue<? extends TreeItem<String>> observable,
                            TreeItem<String> old_val, TreeItem<String> new_val) {
                        TreeItem<String> selectedItem = new_val;

                        // Callback may occur even when there is no
                        // Tree item selected:
                        if (selectedItem == null) {
                            return;
                        }
                        //Checks if selected items is child from "ProjectRoot" TreeItem.
                        //If yes, then it is a project-selection.
                        if (selectedItem.getParent() != null &&
                                "ProjectRoot".equals(selectedItem.getParent().getValue())) {
                            addViewToOverview("/FXML/overview/ProjectOverview.fxml", selectedItem);
                            addViewToDiagram("/FXML/diagram/BoxWhiskerPlot.fxml", selectedItem);

                            //Checks if selected items is a leaf
                            //If yes, then it is a simulation-selection.
                        } else if (selectedItem.isLeaf()) {
                            addViewToOverview("/FXML/overview/SimulationOverview.fxml", selectedItem);
                            addViewToDiagram("/FXML/diagram/SimulationDiagram.fxml", selectedItem);

                        } else {  //otherwise, it is a de.hs.mannheim.modUro.model-selection.
                            addViewToOverview("/FXML/overview/ModeltypeOverview.fxml", selectedItem);
                            addViewToDiagram("/FXML/diagram/ModeltypeDiagram.fxml", selectedItem);
                        }
                    }
                });
    }

    /**
     * Add View to Overview-Tab.
     *
     * @param path
     */
    private void addViewToOverview(String path, TreeItem<String> selectedItem) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        try {
            overviewTab.setContent(loader.load()); //set FXML file to overviewTab.

            if (loader.getController() instanceof ProjectOverviewController) {
                //if controller is ProjectOverview, then load ProjectOverview FXML
                projectOverviewController = loader.getController();
                int indexOfProject = 0;  //index for selected project
                for (Project project : projectData) {
                    if (project.getName().equals(selectedItem.getValue())) {
                        indexOfProject = projectData.indexOf(project);
                    }
                }
                //load the overview with selected project.
                projectOverviewController.init(projectData.get(indexOfProject));
            }

            if (loader.getController() instanceof SimulationOverviewController) {
                //if controller is SimulationOverview, then load SimulationOverview FXML
                simulationOverviewController = loader.getController();

                //checking, which simulation is selected and load the simulation to the overview
                for (Project project : projectData) {
                    for (ModelType modelType : project.getModelTypeList()) {
                        for (Simulation simulation : modelType.getSimulations()) {
                            if (selectedItem.getValue().equals(simulation.getSimulationName())) {
                                simulationOverviewController.init(projectData.get(projectData.indexOf(project)).getModelTypeList().get(project.getModelTypeList().indexOf(modelType)).getSimulations().get(modelType.getSimulations().indexOf(simulation)));
                            }
                        }
                    }

                }
            }

            if (loader.getController() instanceof ModeltypeOverviewController) {
                modeltypeOverviewController = loader.getController();

                //checking, which modelType is selected and load the modelType to the overview
                for (Project project : projectData) {
                    for (ModelType modelType : project.getModelTypeList()) {
                        if (selectedItem.getValue().equals(modelType.getName())) {
                            modeltypeOverviewController.init(projectData.get(projectData.indexOf(project)).getModelTypeList().get(project.getModelTypeList().indexOf(modelType)));
                        }
                    }

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add View to Diagram-Tab.
     *
     * @param path
     */
    private void addViewToDiagram(String path, TreeItem<String> selectedItem) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        try {
            diagramTab.setContent(loader.load()); //set FXML file to overviewTab.

            if (loader.getController() instanceof BoxAndWhiskerPlotController) {
                //if controller is BoxAndWhiskerPlot, then load BoxAndWhiskerPlot FXML
                boxAndWhiskerPlotController = loader.getController();
                int indexOfProject = 0;  //index for selected project
                for (Project project : projectData) {
                    if (project.getName().equals(selectedItem.getValue())) {
                        indexOfProject = projectData.indexOf(project);
                    }
                }
                //load the overview with selected project.
                boxAndWhiskerPlotController.init(projectData.get(indexOfProject));
            }

            if (loader.getController() instanceof SimulationDiagramController) {
                //if controller is SimulationDiagram, then load SimulationDiagram FXML
                simulationDiagramController = loader.getController();

                //checking, which simulation is selected and load the simulation to the overview
                for (Project project : projectData) {
                    for (ModelType modelType : project.getModelTypeList()) {
                        for (Simulation simulation : modelType.getSimulations()) {
                            if (selectedItem.getValue().equals(simulation.getSimulationName())) {
                                simulationDiagramController.init(projectData.get(projectData.indexOf(project)).getModelTypeList().get(project.getModelTypeList().indexOf(modelType)).getSimulations().get(modelType.getSimulations().indexOf(simulation)));
                            }
                        }
                    }
                }
            }

            if (loader.getController() instanceof ModeltypeDiagramController) {
                modeltypeDiagramController = loader.getController();

                //checking, which modelType is selected and load the modelType to the overview
                for (Project project : projectData) {
                    for (ModelType modelType : project.getModelTypeList()) {
                        if (selectedItem.getValue().equals(modelType.getName())) {
                            modeltypeDiagramController.init(projectData.get(projectData.indexOf(project)).getModelTypeList().get(project.getModelTypeList().indexOf(modelType)));
                        }
                    }

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates ProjectTree with Project Data.
     */
    private void createTree() {
        TreeItem<String> root = new TreeItem<String>("ProjectRoot");        //set root
        root.setExpanded(true);                                             //root is expanded
        projectTree.setShowRoot(false);                                     //root is not shown, tree begins with first project
        this.projectTree.setRoot(root);

        TreeItem<String> project;       //project: child of root
        TreeItem<String> model;         //de.hs.mannheim.modUro.model: child of project
        TreeItem<String> simulation;    //simulation: child of de.hs.mannheim.modUro.model

        for (Project projectItem : projectData) {
            //treeitem: project
            project = makeBranch(projectItem.getName(), root); //all project in the projectData will be set as child of the root

            //treeitem: modeltypes
            for (ModelType modelTypeItem : projectItem.getModelTypeList()) {
                model = makeBranch(modelTypeItem.getName(), project);

                //treeitem: simulation
                for (Simulation simulationItem : modelTypeItem.getSimulations()) {
                    if (simulationItem.getSimulationName().contains(modelTypeItem.getName())) { //category check of simulation to its modeltype
                        if (simulationItem.isAborted()) {
                            simulation = makeBranchWithIcon(simulationItem.getSimulationName(), model); //simulation-title will be set as child of the de.hs.mannheim.modUro.model-root
                        } else {
                            simulation = makeBranch(simulationItem.getSimulationName(), model); //simulation-title will be set as child of the de.hs.mannheim.modUro.model-root
                        }
                    }
                }
            }
        }
    }

    /**
     * Makes a Branch.
     *
     * @param title
     * @param parent
     * @return
     */
    private TreeItem<String> makeBranch(String title, TreeItem<String> parent) {
        TreeItem<String> item = new TreeItem<String>(title);
        item.setExpanded(true);
        parent.getChildren().add(item);
        return item;
    }

    /**
     * Makes a Branch with red Icon for aborted simulation.
     *
     * @param title
     * @param parent
     * @return
     */
    private TreeItem<String> makeBranchWithIcon(String title, TreeItem<String> parent) {

        TreeItem<String> item = new TreeItem<String>(title, new ImageView(red));
        item.setExpanded(true);
        parent.getChildren().add(item);
        return item;
    }

    /**
     * Loads Setting Dialog FXMl and opens it in a new Window.
     *
     * @param actionEvent
     */
    public void showSettingsDialog(ActionEvent actionEvent) {
        AnchorPane pane = new AnchorPane();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/dialog/Setting.fxml"));

        try {
            pane = (AnchorPane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Show the scene containing the root layout.
        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.setTitle("Settings");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method shows the dialog of the toolbox-view.
     * @param actionEvent
     */
    public void showEditDialog(ActionEvent actionEvent) {
        TreeItem<String> item = projectTree.getSelectionModel().getSelectedItem();
        String simId = item.getValue();
        Simulation sim = null;
        // TODO associate data with Tree selection model.
        for (Project project : projectData) {
            for (ModelType modelType : project.getModelTypeList()) {
                for (Simulation simulation : modelType.getSimulations()) {
                    if (simId.equals(simulation.getSimulationName())) {
                        sim = simulation;
                    }
                }
            }
        }
        if (sim != null) {
            String dir = sim.getDir().toString();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete files");
            alert.setHeaderText("Remove folder and all files in the following folder?");
            alert.setContentText(dir);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    System.out.println("Delete " + dir);
                    File dirFile = new File(dir);
                    deleteFolder(dirFile);
                }
            });
            projectData.clear();
            initialize();
         // that does not appear
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setHeaderText("Not a simulation run.");
            alert.setContentText("Please select a single simulation run.\n" +
                    "Models or entire projects cannot be deleted.");
            alert.showAndWait();
            //todo: throw exception or log it
        }
    }

    /**
     * This method deletes one selected folder
     * @param folder: folder-directory
     */
    private void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) { //some JVMs return null for empty dirs
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

    public void handleRefreshButton(ActionEvent actionEvent) {
        //get at first the selected Item in the tree
        TreeItem<String> lastSelected = projectTree.getSelectionModel().getSelectedItem();
        this.projectData.clear();
        initialize();
        //set after refresh the last selectedItem
        projectTree.getSelectionModel().select(lastSelected);
    }

    /**
     * This window opens if the "optimizer"-button is pressed
     * @param clickOptimize
     */
    public void handleOptimizeButton(ActionEvent clickOptimize){
        Stage optStage = new Stage();
        optStage.setTitle("optimusPrime");

        //set grid pane
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        //add scenetitle
        Text scenetitle = new Text("Welcome to optimusPrime");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        //add some buttons
        Button selectModel = new Button("Select a model");
        grid.add(selectModel, 1,1);

        Button startCc3d = new Button("Just start CC3D");
        grid.add(startCc3d, 1,2);

        Scene scene = new Scene(grid, 500, 275);
        optStage.setScene(scene);

        optStage.show();
    }

    /**
     * Loads About Dialog FXMl and opens it in a new Window.
     *
     * @param actionEvent
     */
    public void showAboutDialog(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Moduro-Toolbox " + MainApp.VERSION);
        alert.setContentText("Credits\n" +
                "Mathuraa Pathmanathan (mathuraa@hotmail.de)\n" +
                "Markus Gumbel (m.gumbel@hs-mannheim.de)\n" +
                "\n" +
                "This is free software under the Apache 2 license."
        );
        alert.showAndWait();
        /*
        AnchorPane pane = new AnchorPane();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/dialog/About.fxml"));

        try {
            pane = (AnchorPane) loader.load();
            toolboxversion.setText(MainApp.VERSION);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Show the scene containing the root layout.
        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.setTitle("About");
        stage.setScene(scene);
        stage.show();
        */
    }
}