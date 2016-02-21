package de.hs.mannheim.modUro.creator;

import de.hs.mannheim.modUro.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * JUnit Test for MetricTypeCreatorTest.
 * Test data: first project (Project1)-> first Modeltype (CM-IN-DAE)-> first simulation (CM-IN-DAE_cc3d_01_15_2015_12_53_49)
 * -> first MetricType (FitnessArrangement.dat)
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class MetricTypeCreatorTest {

    final double DELTA = 1e-15;

    MainModel mainModel;
    Project project;
    List<Project> projectList;
    ModelType modelType;
    Simulation simulation;
    MetricType metricType;

    @Before
    public void setUp() {
        mainModel = new MainModel("/setting/Setting.xml");
        projectList = mainModel.getProjectData();
        project = projectList.get(0);
        modelType = project.getModelTypeList().get(0);
        simulation = modelType.getSimulations().get(0);
        metricType = simulation.getMetricType().get(0);
    }

    @Test
    public void metricTypeName() {
        Assert.assertEquals("Name of metrictype should be 'FitnessArrangement' and not: " + metricType.getName(), "FitnessArrangement", metricType.getName());
    }

    @Test
    public void metricData() {
        double[][] metricData = new double[][]{
                { 0.5, 3.5 },
                { 1.0, 0.5,},
                { 1.5, 0.5}
        };

        Assert.assertArrayEquals("Metric data does not equals.", metricData, metricType.getMetricData());
    }

    @Test
    public void meanOfSimulation() {
        Assert.assertEquals("Mean of simulation should be '1.5' and not: " + metricType.getMean(),1.5, metricType.getMean(), DELTA);
    }

    @Test
    public void stdDevOfSimulation(){
        Assert.assertEquals("Standard deviation of simulation should be '1.7320508075688772' and not: " + metricType.getDeviation(),1.7320508075688772, metricType.getDeviation(), DELTA);
    }

}
