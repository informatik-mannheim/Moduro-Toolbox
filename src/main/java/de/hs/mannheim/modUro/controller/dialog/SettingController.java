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
package de.hs.mannheim.modUro.controller.dialog;

import de.hs.mannheim.modUro.model.dialog.ProjectSetting;
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
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class SettingController {

    //Reference to SettingModel
    private SettingModel settingModel;

    @FXML
    private TreeTableView<ProjectSetting> treeTable;

    ObservableList<ProjectSetting> setting;

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

        TreeItem<ProjectSetting> root = new TreeItem<>(new ProjectSetting());
        TreeItem<ProjectSetting> treeItem;

        for (ProjectSetting projectSettingItem : setting) {
            treeItem = new TreeItem<>(projectSettingItem);
            root.getChildren().add(treeItem);
        }


        treeTable.setRoot(root);
        root.setExpanded(true);
        treeTable.setShowRoot(false);

        TreeTableColumn<ProjectSetting, String> columnProject = new TreeTableColumn<ProjectSetting, String>("ProjectName");
        columnProject.setCellValueFactory(new TreeItemPropertyValueFactory<ProjectSetting, String>("name"));

        TreeTableColumn<ProjectSetting, String> columnNode = new TreeTableColumn<ProjectSetting, String>("Node");
        columnNode.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ProjectSetting, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ProjectSetting, String> param) {
                return new SimpleStringProperty(param.getValue().getValue().getNode().get(0).getName());
            }
        });

        TreeTableColumn<ProjectSetting, String> columnPath = new TreeTableColumn<ProjectSetting, String>("Path");
        columnPath.setCellValueFactory(new TreeItemPropertyValueFactory<ProjectSetting, String>("path"));


        treeTable.getColumns().addAll(columnProject, columnNode, columnPath);
        treeTable.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
    }

    ;

}
