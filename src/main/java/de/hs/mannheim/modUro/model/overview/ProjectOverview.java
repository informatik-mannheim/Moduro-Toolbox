package de.hs.mannheim.modUro.model.overview;

import de.hs.mannheim.modUro.model.ModelType;
import de.hs.mannheim.modUro.model.Project;
import de.hs.mannheim.modUro.model.Simulation;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for Project Overview Model.
 * @author  Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class ProjectOverview {

    private Project project;

    private int numberOfSimulations;
    private int numberOfSteadyStateSimulation;
    private int numberOfAbortedSimulations;
    private int numberOfCompletedSimulations;
    private List<String> kindsOfModelTypes;

    /**
     * Constructor.
     * @param project
     */
    public ProjectOverview(Project project){
        this.project = project;
        countSimulations();
        countSteadyStateSimulation();
        countCompletedSimulations();
        countAbortedSimulations();
        listKindsOfModelType();
    }

    /**
     * Counts number of Simulations.
     */
    private void countSimulations(){
        int count = 0;
        for (ModelType modelTypeItem: project.getModelTypeList()) {
            count = count + modelTypeItem.getSimulations().size();
        }
        this.numberOfSimulations = count;
    }

    /**
     * Counts number of completed simulations.
     */
    private void countCompletedSimulations() {
        int count = 0;
        for (ModelType modelTypeItem: project.getModelTypeList()) {
            for (Simulation simulationItem: modelTypeItem.getSimulations()) {
                if(simulationItem.isCompleted()) {
                    count++;
                }
            }
        }
        this.numberOfCompletedSimulations = count;
    }

    /**
     * Counts number of aborted simulations.
     */
    private void countAbortedSimulations() {
        int count = 0;
            for (ModelType modelTypeItem: project.getModelTypeList()) {
                for (Simulation simulationItem: modelTypeItem.getSimulations()) {
                    if(simulationItem.isInSteadyState()) {
                        count++;
                    }
                }
            }
        this.numberOfSteadyStateSimulation = count;
    }

    private void countSteadyStateSimulation() {
        int count = 0;
        for (ModelType modelTypeItem: project.getModelTypeList()) {
            for (Simulation simulationItem: modelTypeItem.getSimulations()) {
                if(simulationItem.isAborted()) {
                    count++;
                }
            }
        }
        this.numberOfAbortedSimulations = count;
    }

    /**
     * Lists kinds of Modeltypes
     */
    private void listKindsOfModelType() {
        this.kindsOfModelTypes = new ArrayList<>();
            for (ModelType modelTypeItem: project.getModelTypeList()) {
                    this.kindsOfModelTypes.add(modelTypeItem.getName());
            }
    }

    /*Getter*/
    public Project getProject() {
        return project;
    }

    public int getNumberOfSimulations() {
        return numberOfSimulations;
    }

    public int getNumberOfAbortedSimulations() {
        return numberOfAbortedSimulations;
    }

    public int getNumberOfCompletedSimulations() {
        return numberOfCompletedSimulations;
    }

    public int getNumberOfSteadyStateSimulation() {
        return numberOfSteadyStateSimulation;
    }

    public List<String> getKindsOfModelTypes() {
        return kindsOfModelTypes;
    }


}
