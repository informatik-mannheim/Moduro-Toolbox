package de.hs.mannheim.modUro.creator;

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
 * JUnit Test for SimulationCreatorTest.
 * Test data: first project (Project1)-> first Modeltype (CM-IN-DAE)-> first simulation (CM-IN-DAE_cc3d_01_15_2015_12_53_49)
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class SimulationCreatorTest {
    final double DELTA = 1e-15;

    MainModel mainModel;
    Project project;
    List<Project> projectList;
    ModelType modelType;
    Simulation simulation;
    Simulation simulation2;

    //Test data for testing conditions
    ModelType modelType2;
    Simulation simulation1;

    @Before
    public void setUp() {
        mainModel = new MainModel("/setting/Setting.xml");
        projectList = mainModel.getProjectData();
        project = projectList.get(0);
        modelType = project.getModelTypeList().get(0);
        simulation = modelType.getSimulations().get(0);
        simulation2 = modelType.getSimulations().get(1);

        modelType2 = project.getModelTypeList().get(1);
        simulation1 = modelType2.getSimulations().get(0);
    }

    @Test
    public void nameOfFirstSimulationInSimulationList() {
        Assert.assertEquals("Simulation name should be 'CM-IN-DAE_cc3d_01_15_2015_12_53_49' and not: " + simulation.getSimulationName(), "CM-IN-DAE_cc3d_01_15_2015_12_53_49", simulation.getSimulationName());
    }

    @Test
    public void modeltypeOfSimulation() {
        Assert.assertEquals("Modeltype name should be 'CM-IN-DAE' and not: " + simulation.getModelType(), "CM-IN-DAE", simulation.getModelType());
    }

    @Test
    public void durationOfSimulation() {
        //?????

        Assert.assertEquals("Duration of simulation should be '108.0' and not: " + simulation.getDuration(), 108.0, simulation.getDuration(), DELTA);
    }

    @Test
    public void startTimeOfSimulation() {
        String dateInString = "2015-01-15 12:53:48";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime simulationTime = LocalDateTime.parse(dateInString, formatter);

        Assert.assertEquals("Starttime of Simulation should be '" + simulationTime +"' and not: " + simulation.getStartTime(), simulationTime, simulation.getStartTime());
    }

    @Test
    public void startTimeMissingInSecondModelTypeFirstSimulation() {
        LocalDateTime dateTime = LocalDateTime.now();

        Assert.assertEquals("Starttime of Simulation should be '" + dateTime +"' and not: " + simulation1.getStartTime(), dateTime, simulation1.getStartTime());
    }

    @Test
    public void countMetricTypeOfSimulation() {
        Assert.assertEquals("Simulation should have '3' MetricTypes and not: " + simulation.getMetricType().size(), 3, simulation.getMetricType().size());
    }

    @Test
    public void simulationIsNotCompleted() {
        //Def.
        //toTime == Fitnesplot[letzte Zeit], maxTime ==  700
        //isCompleted = toTime >= maxTime

        //toTime of Simulation is = 108.0
        //108.0 < 700 -> isCompleted == false
        Assert.assertEquals("Simulation should 'not be completed'.", false, simulation.isCompleted());
    }

    @Test
    public void simulationIsInSteadyState() {
        //Def.
        //toTime == Fitnesplot[letzte Zeit], minTime == 40
        //isInSteadyState = toTime >= minTime

        //toTime of Simulation is = 108.0
        //108.0 > 40
        Assert.assertEquals("Simulation should 'be in steady state'.", true, simulation.isInSteadyState());
    }

    @Test
    public void simulationIsNotAborted() {
        //Def.
        //lastFitness == Fitnesplot[letzte Fitness], isInSteadyState
        //isAborted = lastFitness < 0.05 && isInSteadyState

        //lastFitness == 0.8079322563, isInSteadyState == true
        Assert.assertEquals("Simulation should 'not be aborted'.", false, simulation.isAborted());
    }

    @Test
    public void imagesOfSimulation() {
        File firstImageFile = new File("C:\\Users\\adminM\\Desktop\\jUnitTestSimulation\\Projekt1\\node2\\CM-IN-DAE_cc3d_01_18_2015_17_30_26\\CM-IN-DAE_cc3d_0002300.png");
        File secondImageFile = new File("C:\\Users\\adminM\\Desktop\\jUnitTestSimulation\\Projekt1\\node2\\CM-IN-DAE_cc3d_01_18_2015_17_30_26\\CM-IN-DAE_cc3d_0005600.png");
        File thirdImageFile = new File("C:\\Users\\adminM\\Desktop\\jUnitTestSimulation\\Projekt1\\node2\\CM-IN-DAE_cc3d_01_18_2015_17_30_26\\CM-IN-DAE_cc3d_0008900.png");
        List<File> imageFiles = new ArrayList<File>(){{add(firstImageFile); add(secondImageFile); add(thirdImageFile);}};

        Assert.assertEquals("Simulation Images are wrong.", imageFiles, simulation2.getImages());
    }
}
