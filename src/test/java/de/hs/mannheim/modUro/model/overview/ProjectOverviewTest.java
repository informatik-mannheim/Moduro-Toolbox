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
import de.hs.mannheim.modUro.model.Project;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * JUnit Test for ProjectOverviewTest.
 * Test data: 2nd project (Project2)
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class ProjectOverviewTest {

    MainModel mainModel;
    List<Project> projectList;
    Project project;

    ProjectOverview projectOverview;

    @Before
    public void setUp() {
        mainModel = new MainModel("src/test/resources/setting/Setting.xml");
        projectList = mainModel.getProjectData();
        project = projectList.get(1);

        projectOverview = new ProjectOverview(project);
    }

    @Test
    public void numberOfSimulationsTest() {
        Assert.assertEquals("Project should have '2' simulations and not: " + projectOverview.getNumberOfSimulations(), 2, projectOverview.getNumberOfSimulations());
    }

    @Test
    public void numberOfSteadyStateSimulation() {
        //Def.
        //minTime = 40
        //steadystate = toTime > minTime
        //PAS-IN-DAE_cc3d_01_15_2015_12_46_01 -> toTime:4.5  => notInSteadyState
        //PAS-IN-RA_cc3d_12_04_2014_11_03_08  -> toTime:702.0 => InSteadyState      //manuell in der Datei geänderter toTime Wert

        Assert.assertEquals("Project should have '1' in steady state simulations and not: " + projectOverview.getNumberOfSteadyStateSimulation(), 1, projectOverview.getNumberOfSteadyStateSimulation());
    }

    @Test
    public void numberOfAbortedSimulations() {
        //Def.
        //isAborted = lastFitness <0.05 && isInSteadyState
        //PAS-IN-DAE_cc3d_01_15_2015_12_46_01 -> notInSteadyState && lastFitness=0.673270419438 => isNotAborted
        //PAS-IN-RA_cc3d_12_04_2014_11_03_08  -> inSteadyState && lastFitness=0.01 => isAborted   //manuell in der Datei geänderter toTime Wert

        Assert.assertEquals("Project should have '1' aborted simulations and not: " + projectOverview.getNumberOfAbortedSimulations(), 1, projectOverview.getNumberOfAbortedSimulations());
    }

    @Test
    public void numberOfCompletedSimulations() {
        //Def.
        //maxTime = 700
        //isCompleted = toTime >= maxTime
        //PAS-IN-DAE_cc3d_01_15_2015_12_46_01 -> toTime:4.5  => notCompleted
        //PAS-IN-RA_cc3d_12_04_2014_11_03_08  -> toTime:702.0 => completed

        Assert.assertEquals("Project should have '1' completed simulations and not: " + projectOverview.getNumberOfCompletedSimulations(), 1, projectOverview.getNumberOfCompletedSimulations());
    }

    @Test
    public void listOfModeltypesInProject() {
        List<String> modelTypeList = new ArrayList<String>() {{
            add("PAS-IN-DAE");
            add("PAS-IN-RA");
        }};

        Assert.assertEquals("Project should have other modeltypes.", modelTypeList, projectOverview.getKindsOfModelTypes());
    }
}
