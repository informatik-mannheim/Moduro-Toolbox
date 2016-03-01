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
 *
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

        Assert.assertEquals("Duration of simulation should be '702.0' and not: " + simulationOverview.getDuration(), 702.0, simulationOverview.getDuration(), DELTA);
    }

    @Test
    public void startTimeOfSimulation() {
        String dateInString = "2014-12-04 11:03:08";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime simulationTime = LocalDateTime.parse(dateInString, formatter);

        Assert.assertEquals("Starttime of Simulation should be '" + simulationTime + "' and not: " + simulationOverview.getStartTime(), simulationTime, simulationOverview.getStartTime());
    }

    @Test
    public void listOfMetricTypesInSimulation() {
        List<String> metricTypeList = new ArrayList<String>() {{
            add("FitnessArrangement");
            add("FitnessPlot");
            add("FitnessVolume");
        }};

        Assert.assertEquals("Simulation should have other MetricType names.", metricTypeList, simulationOverview.getMetricTypesName());
    }

    @Test
    public void simulationIsCompleted() {
        //Def.
        //toTime == Fitnesplot[letzte Zeit], maxTime ==  700
        //isCompleted = toTime >= maxTime

        //toTime of Simulation is = 702.0
        //702.0 < 700 -> isCompleted == true
        Assert.assertEquals("Simulation should 'be completed'.", true, simulationOverview.isCompleted());
    }

    @Test
    public void simulationIsInSteadyState() {
        //Def.
        //toTime == Fitnesplot[letzte Zeit], minTime == 40
        //isInSteadyState = toTime >= minTime

        //toTime of Simulation is = 702.0
        //702.0 > 40
        Assert.assertEquals("Simulation should 'be in steady state'.", true, simulationOverview.isInSteadyState());
    }

    @Test
    public void simulationIsAborted() {
        //Def.
        //lastFitness == Fitnesplot[letzte Fitness], isInSteadyState
        //isAborted = lastFitness < 0.05 && isInSteadyState

        //lastFitness == 0.01 , isInSteadyState == true
        Assert.assertEquals("Simulation should 'be aborted'.", true, simulationOverview.isAborted());
    }

    @Test
    public void imagesOfSimulation() {
        File firstImageFile = new File("C:\\Users\\adminM\\Desktop\\Moduro-Toolbox\\src\\test\\resources\\Simulationdata\\Projekt2\\node1\\PAS-IN-RA_cc3d_12_04_2014_11_03_08\\PAS-IN-RA_cc3d_0009000.png");
        File secondImageFile = new File("C:\\Users\\adminM\\Desktop\\Moduro-Toolbox\\src\\test\\resources\\Simulationdata\\Projekt2\\node1\\PAS-IN-RA_cc3d_12_04_2014_11_03_08\\PAS-IN-RA_cc3d_0009600.png");
        File thirdImageFile = new File("C:\\Users\\adminM\\Desktop\\Moduro-Toolbox\\src\\test\\resources\\Simulationdata\\Projekt2\\node1\\PAS-IN-RA_cc3d_12_04_2014_11_03_08\\PAS-IN-RA_cc3d_0010100.png");
        List<File> imageFiles = new ArrayList<File>() {{
            add(firstImageFile);
            add(secondImageFile);
            add(thirdImageFile);
        }};

        Assert.assertEquals("Simulation Images are wrong.", imageFiles, simulationOverview.getImages());
    }

    @Test
    public void filePathOfSimulation() {
        File file = new File("C:\\Users\\adminM\\Desktop\\Moduro-Toolbox\\src\\test\\resources\\Simulationdata\\Projekt2\\node1\\PAS-IN-RA_cc3d_12_04_2014_11_03_08");

        Assert.assertEquals("Path of the Simulation should be " + file + " and not: " + simulationOverview.getDirectory(), file, simulationOverview.getDirectory());
    }
}