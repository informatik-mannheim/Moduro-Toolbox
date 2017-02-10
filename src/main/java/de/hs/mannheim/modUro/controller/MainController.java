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
import de.hs.mannheim.modUro.config.FitnessName;
import de.hs.mannheim.modUro.config.ToolboxLogger;
import de.hs.mannheim.modUro.controller.diagram.BoxAndWhiskerPlotController;
import de.hs.mannheim.modUro.controller.diagram.ModeltypeDiagramController;
import de.hs.mannheim.modUro.controller.diagram.SimulationDiagramController;
import de.hs.mannheim.modUro.controller.overview.ModeltypeOverviewController;
import de.hs.mannheim.modUro.controller.overview.ProjectOverviewController;
import de.hs.mannheim.modUro.controller.overview.SimulationOverviewController;
import de.hs.mannheim.modUro.fx.ModuroTreeItem;
import de.hs.mannheim.modUro.model.*;
import de.hs.mannheim.modUro.model.overview.ModeltypeOverview;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
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
    private void addViewToOverview(String path, TreeItem<String> sItem) {
        ModuroTreeItem selectedItem = (ModuroTreeItem) sItem;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        try {
            overviewTab.setContent(loader.load()); //set FXML file to overviewTab.

            if (loader.getController() instanceof ProjectOverviewController) {
                //if controller is ProjectOverview, then load ProjectOverview FXML
                projectOverviewController = loader.getController();
                projectOverviewController.init((Project) selectedItem.getObject());
            }

            if (loader.getController() instanceof SimulationOverviewController) {
                //if controller is SimulationOverview, then load SimulationOverview FXML
                simulationOverviewController = loader.getController();
                simulationOverviewController.init((Simulation) selectedItem.getObject());
            }

            if (loader.getController() instanceof ModeltypeOverviewController) {
                modeltypeOverviewController = loader.getController();
                modeltypeOverviewController.init((ModelType) selectedItem.getObject());
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
    private void addViewToDiagram(String path, TreeItem<String> sItem) {
        ModuroTreeItem selectedItem = (ModuroTreeItem) sItem;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        try {
            diagramTab.setContent(loader.load()); //set FXML file to overviewTab.

            if (loader.getController() instanceof BoxAndWhiskerPlotController) {
                //if controller is BoxAndWhiskerPlot, then load BoxAndWhiskerPlot FXML
                boxAndWhiskerPlotController = loader.getController();
                // Load the overview with selected project:
                boxAndWhiskerPlotController.init((Project) selectedItem.getObject());
            }

            if (loader.getController() instanceof SimulationDiagramController) {
                //if controller is SimulationDiagram, then load SimulationDiagram FXML
                simulationDiagramController = loader.getController();
                simulationDiagramController.init((Simulation) selectedItem.getObject());
            }

            if (loader.getController() instanceof ModeltypeDiagramController) {
                modeltypeDiagramController = loader.getController();
                modeltypeDiagramController.init((ModelType) selectedItem.getObject());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates ProjectTree with Project Data.
     */
    private void createTree() {
        ToolboxLogger.log.fine("Creating FX tree...");
        int counter = 0;
        int stepSize = 25;
        ModuroTreeItem root = new ModuroTreeItem("ProjectRoot", null);        //set root
        root.setExpanded(true);                                             //root is expanded
        projectTree.setShowRoot(false);                                     //root is not shown, tree begins with first project
        this.projectTree.setRoot(root);

        ModuroTreeItem project;       //project: child of root
        ModuroTreeItem model;         //de.hs.mannheim.modUro.model: child of project
        ModuroTreeItem simulation;    //simulation: child of de.hs.mannheim.modUro.model

        for (Project projectItem : projectData) {
            //treeitem: project
            // All projects in the projectData will be set as child of the root:
            project = makeBranch(projectItem.getName(), projectItem, root, null);

            //treeitem: modeltypes
            for (ModelType modelTypeItem : projectItem.getModelTypeList()) {
                // TODO Model item: add total fitness as info
                ModeltypeOverview modeltypeOverview = new ModeltypeOverview(modelTypeItem);
                double meanFitness = modeltypeOverview.getStatisticValues().
                        get(FitnessName.TOTAL_FITNESS.getName()).getMean();
                String meanFitnessS = String.format("%.2f", meanFitness);
                String modelLabel = modelTypeItem.getName() + " (" + meanFitnessS + ")";
                model = makeBranch(modelLabel, modelTypeItem, project, null);

                //treeitem: simulation
                for (Simulation simulationItem : modelTypeItem.getSimulations()) {
                    Node node = null;
                    if (simulationItem.isAborted()) {
                        node = new ImageView(red);
                    }
                    simulation = makeBranch(simulationItem.getSimulationName(), simulationItem,
                            model, node);
                    counter++;
                    if (counter % stepSize == 0) {
                        ToolboxLogger.log.fine("Added " + counter +
                                " items to the tree so far.");
                    }
                }
            }
        }
        ToolboxLogger.log.fine("Creating FX tree done. " + counter + " items.");
    }

    /**
     * Makes a tree branch.
     *
     * @param title
     * @param parent
     * @return
     */
    private ModuroTreeItem makeBranch(String title, Object o,
                                      TreeItem<String> parent, Node node) {
        ModuroTreeItem item = new ModuroTreeItem(title, o, node);
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
                    ToolboxLogger.log.info("Delete " + dir);
                    File dirFile = new File(dir);
                    deleteFolder(dirFile);
                }
            });
            projectData.clear();
            initialize();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setHeaderText("Not a simulation run.");
            alert.setContentText("Please select a single simulation run.\n" +
                    "Models or entire projects cannot be deleted.");
            alert.showAndWait();
        }
    }

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