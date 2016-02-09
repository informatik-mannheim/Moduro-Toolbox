package de.hs.mannheim.modUro.controller.dialog;

import de.hs.mannheim.modUro.model.dialog.SettingFile;
import de.hs.mannheim.modUro.model.dialog.SettingModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.util.Callback;

/**
 * Created by m.pathmanathan on 18.12.2015.
 */
public class SettingController {

    //Reference to SettingModel
    private SettingModel settingModel;

    @FXML
    private TreeTableView<SettingFile> treeTable;

    ObservableList<SettingFile> setting;

    @FXML
    private void initialize() {

        settingModel = new SettingModel();
        setting = FXCollections.observableArrayList(settingModel.getSettings());

        createTable();
    }

    /**
     * Creates Tree Table View.
     */
    private void createTable() {

        TreeItem<SettingFile> root = new TreeItem<>(new SettingFile());
        TreeItem<SettingFile> treeItem;

        for (SettingFile settingFileItem: setting) {
            treeItem = new TreeItem<>(settingFileItem);
            root.getChildren().add(treeItem);
        }


        treeTable.setRoot(root);
        root.setExpanded(true);
        treeTable.setShowRoot(false);

        TreeTableColumn<SettingFile, String> columnProject = new TreeTableColumn<SettingFile, String>("ProjectName");
        columnProject.setCellValueFactory(new TreeItemPropertyValueFactory<SettingFile, String>("name"));

        TreeTableColumn<SettingFile, String> columnNode = new TreeTableColumn<SettingFile, String>("Node");
        columnNode.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<SettingFile, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<SettingFile, String> param) {
                return new SimpleStringProperty(param.getValue().getValue().getNode().get(0).getName());
            }
        });

        TreeTableColumn<SettingFile, String> columnPath = new TreeTableColumn<SettingFile, String>("Path");
        columnPath.setCellValueFactory(new TreeItemPropertyValueFactory<SettingFile, String>("path"));


        treeTable.getColumns().addAll(columnProject, columnNode, columnPath);
        treeTable.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
    };

}
