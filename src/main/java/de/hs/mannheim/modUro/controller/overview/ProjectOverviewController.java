package de.hs.mannheim.modUro.controller.overview;

import de.hs.mannheim.modUro.model.Project;
import de.hs.mannheim.modUro.model.overview.ProjectOverview;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * ProjectOverviewController controls ProjectOverviewView.
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
    private Label modelType;

    public void init(Project project){

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
        this.modelType.setText(projectOverview.getKindsOfModelTypes().toString());
    }
}
