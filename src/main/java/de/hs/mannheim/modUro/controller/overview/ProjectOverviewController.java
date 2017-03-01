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
package de.hs.mannheim.modUro.controller.overview;

import de.hs.mannheim.modUro.model.Project;
import de.hs.mannheim.modUro.model.overview.ProjectOverview;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * ProjectOverviewController controls ProjectOverviewView.
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class ProjectOverviewController {

    //Reference to ProjectOverview
    private ProjectOverview projectOverview;

    @FXML
    private Label numberOfSimulation;
    @FXML
    private Label numberOfSteadyStateSimulation;
    @FXML
    private Label abortedSimulation;
    @FXML
    private Label completedSimulation;
    @FXML
    private Label moduroModels;

    public void init(Project project) {

        this.projectOverview = new ProjectOverview(project);
        setLabel();
    }

    /**
     * Sets Project Details to Label.
     */
    private void setLabel() {
        this.numberOfSimulation.setText(String.valueOf(projectOverview.getNumberOfSimulations()));
        this.numberOfSteadyStateSimulation.setText((String.valueOf(projectOverview.getNumberOfSteadyStateSimulation())));
        this.abortedSimulation.setText((String.valueOf(projectOverview.getNumberOfAbortedSimulations())));
        this.completedSimulation.setText((String.valueOf(projectOverview.getNumberOfCompletedSimulations())));
        this.moduroModels.setText(projectOverview.getKindsOfModelTypes().size() + "");
    }
}
