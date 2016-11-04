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

import de.hs.mannheim.modUro.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.DecimalFormat;
import java.util.List;

/**
 * JUnit Test for ModeltypeOverviewTest.
 * Test data: 2nd project (Project2)->
 * 2nd Modeltype (PAS-IN-RA)->
 * first simulation (PAS-IN-RA_cc3d_12_04_2014_11_03_08)
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class ModeltypeOverviewTest {

    MainModel mainModel;
    List<Project> projectList;
    Project project;
    ModelType modelType;

    ModeltypeOverview modeltypeOverview;
    StatisticValues stats;
    DecimalFormat f = new DecimalFormat("#0.00");

    @Before
    public void setUp() {
        mainModel = new MainModel("src/test/resources/setting/Setting.xml");
        projectList = mainModel.getProjectData();
        project = projectList.get(1);
        modelType = project.getModelTypeList().get(1);

        modeltypeOverview = new ModeltypeOverview(modelType);
    }

    @Test
    public void numberOfSimulationsTest() {
        Assert.assertEquals("Modeltype should have '1' simulations and not: " + modeltypeOverview.getNumberOfSimulations(), 1, modeltypeOverview.getNumberOfSimulations());
    }

    @Test
    public void numberOfSteadyStateSimulation() {
        //Def.
        //minTime = 40
        //steadystate = toTime > minTime
        //PAS-IN-RA_cc3d_12_04_2014_11_03_08 -> toTime:702.0 => inSteadyState //manuell in der Datei geänderter toTime Wert

        Assert.assertEquals("Modeltype should have '1' in steady state simulations and not: " + modeltypeOverview.getNumberOfSteadyStateSimulation(), 1, modeltypeOverview.getNumberOfSteadyStateSimulation());
    }

    @Test
    public void numberOfAbortedSimulations() {
        //Def.
        //isAborted = lastFitness <0.05 && isInSteadyState
        //PAS-IN-RA_cc3d_12_04_2014_11_03_08 -> notInSteadyState && lastFitness=0.407548176606 => isAborted     //manuell in der Datei geänderter toTime und lastFitness

        Assert.assertEquals("Modeltype should have '1' aborted simulations and not: " + modeltypeOverview.getNumberOfAbortedSimulations(), 1, modeltypeOverview.getNumberOfAbortedSimulations());
    }

    @Test
    public void numberOfCompletedSimulations() {
        //Def.
        //maxTime = 700
        //isCompleted = toTime >= maxTime
        //PAS-IN-RA_cc3d_12_04_2014_11_03_08 -> toTime:11.0 => isCompleted

        Assert.assertEquals("Modeltype should have '1' completed simulations and not: " + modeltypeOverview.getNumberOfCompletedSimulations(), 1, modeltypeOverview.getNumberOfCompletedSimulations());
    }

    @Test
    public void statisticValueOfFitnessPlot() {
        String mean = f.format(modeltypeOverview.getStatisticValues().get(1).getMean());
        String stdDev = f.format(modeltypeOverview.getStatisticValues().get(1).getStdDev());

        Assert.assertEquals("Name of Metrictype should be 'FitnessPlot' and not: " + modeltypeOverview.getStatisticValues().get(1).getMetricTypeName(), "FitnessPlot", modeltypeOverview.getStatisticValues().get(1).getMetricTypeName());
        Assert.assertEquals("Mean of FitnessPlot should be '0,37' and not: " + mean, "0,38", mean);
        Assert.assertEquals("Standard Deviation of FitnessPlot should be '0,00' and not: " + stdDev, "0,00", stdDev);
    }
}
