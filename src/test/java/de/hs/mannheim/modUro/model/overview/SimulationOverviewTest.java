package de.hs.mannheim.modUro.model.overview;

import de.hs.mannheim.modUro.model.MainModel;
import de.hs.mannheim.modUro.model.ModelType;
import de.hs.mannheim.modUro.model.Project;
import de.hs.mannheim.modUro.model.Simulation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * JUnit Test for SimulationOverviewTest.
 * Test data: 2nd project (Project2)-> 2nd Modeltype (PAS-IN-RA)-> first simulation (PAS-IN-RA_cc3d_12_04_2014_11_03_08)
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class SimulationOverviewTest {

    final double DELTA = 1e-15;

    MainModel mainModel;
    List<Project> projectList;
    Project project;
    ModelType modelType;
    Simulation simulation;

    SimulationOverview simulationOverview;

    @Before
    public void setUp() {
        mainModel = new MainModel("/setting/Setting.xml");
        projectList = mainModel.getProjectData();
        project = projectList.get(1);
        modelType = project.getModelTypeList().get(1);
        simulation = modelType.getSimulations().get(0);

        simulationOverview = new SimulationOverview(simulation);
    }

    @Test
    public void nameOfFirstSimulationInSimulationList() {
        Assert.assertEquals("Simulation name should be 'PAS-IN-RA_cc3d_12_04_2014_11_03_08' and not: " + simulationOverview.getSimulationName(), "PAS-IN-RA_cc3d_12_04_2014_11_03_08", simulationOverview.getSimulationName());
    }

    @Test
    public void modeltypeOfSimulation() {
        Assert.assertEquals("Modeltype name should be 'PAS-IN-RA' and not: " + simulationOverview.getModelType(), "PAS-IN-RA", simulationOverview.getModelType());
    }

    @Test
    public void durationOfSimulation() {
        //?????

        Assert.assertEquals("Duration of simulation should be '11.0' and not: " + simulationOverview.getDuration(), 11.0, simulationOverview.getDuration(), DELTA);
    }

    @Test
    public void startTimeOfSimulation() {
        String dateInString = "2014-12-04 11:03:08";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime simulationTime = LocalDateTime.parse(dateInString, formatter);

        Assert.assertEquals("Starttime of Simulation should be '" + simulationTime +"' and not: " + simulationOverview.getStartTime(), simulationTime, simulationOverview.getStartTime());
    }

    @Test
    public void listOfMetricTypesInSimulation() {
        List<String> metricTypeList = new ArrayList<String>(){{add("FitnessArrangement"); add("FitnessPlot"); add("FitnessVolume");}};

        Assert.assertEquals("Simulation should have other MetricType names.", metricTypeList, simulationOverview.getMetricTypesName());
    }

    @Test
    public void simulationIsNotCompleted() {
        //Def.
        //toTime == Fitnesplot[letzte Zeit], maxTime ==  700
        //isCompleted = toTime >= maxTime

        //toTime of Simulation is = 11.0
        //11.0 < 700 -> isCompleted == false
        Assert.assertEquals("Simulation should 'not be completed'.", false, simulationOverview.isCompleted());
    }

    @Test
    public void simulationIsNotInSteadyState() {
        //Def.
        //toTime == Fitnesplot[letzte Zeit], minTime == 40
        //isInSteadyState = toTime >= minTime

        //toTime of Simulation is = 11.0
        //11.0 < 40
        Assert.assertEquals("Simulation should 'not be in steady state'.", false, simulationOverview.isInSteadyState());
    }

    @Test
    public void simulationIsNotAborted() {
        //Def.
        //lastFitness == Fitnesplot[letzte Fitness], isInSteadyState
        //isAborted = lastFitness < 0.05 && isInSteadyState

        //lastFitness == 0.407548176606 , isInSteadyState == false
        Assert.assertEquals("Simulation should 'not be aborted'.", false, simulationOverview.isAborted());
    }

    @Test
    public void imagesOfSimulation() {
        File firstImageFile = new File("C:\\Users\\adminM\\Desktop\\jUnitTestSimulation\\Projekt2\\node1\\PAS-IN-RA_cc3d_12_04_2014_11_03_08\\PAS-IN-RA_cc3d_0003200.png");
        File secondImageFile = new File("C:\\Users\\adminM\\Desktop\\jUnitTestSimulation\\Projekt2\\node1\\PAS-IN-RA_cc3d_12_04_2014_11_03_08\\PAS-IN-RA_cc3d_0008000.png");
        File thirdImageFile = new File("C:\\Users\\adminM\\Desktop\\jUnitTestSimulation\\Projekt2\\node1\\PAS-IN-RA_cc3d_12_04_2014_11_03_08\\PAS-IN-RA_cc3d_0012700.png");
        List<File> imageFiles = new ArrayList<File>(){{add(firstImageFile); add(secondImageFile); add(thirdImageFile);}};

        Assert.assertEquals("Simulation Images are wrong.", imageFiles, simulationOverview.getImages());
    }

    @Test
    public void filePathOfSimulation() {
        File file = new File("C:\\Users\\adminM\\Desktop\\jUnitTestSimulation\\Projekt2\\node1\\PAS-IN-RA_cc3d_12_04_2014_11_03_08");

        Assert.assertEquals("Path of the Simulation should be " + file + " and not: " + simulationOverview.getDirectory(), file, simulationOverview.getDirectory());
    }
}