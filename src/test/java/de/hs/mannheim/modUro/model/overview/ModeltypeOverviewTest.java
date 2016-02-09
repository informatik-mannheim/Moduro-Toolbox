package de.hs.mannheim.modUro.model.overview;

import de.hs.mannheim.modUro.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.DecimalFormat;
import java.util.List;

/**
 * JUnit Test for ModeltypeOverviewTest.
 * Test data: 2nd project (Project2)-> 2nd Modeltype (PAS-IN-RA)-> first simulation (PAS-IN-RA_cc3d_12_04_2014_11_03_08)
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
        mainModel = new MainModel("/setting/Setting.xml");
        projectList = mainModel.getProjectData();
        project = projectList.get(1);
        modelType = project.getModelTypeList().get(1);

        modeltypeOverview = new ModeltypeOverview(modelType);
    }

    @Test
    public void numberOfSimulationsTest() {
        Assert.assertEquals("Modeltype should have '6' simulations and not: " + modeltypeOverview.getNumberOfSimulations(),6, modeltypeOverview.getNumberOfSimulations());
    }

    @Test
    public void numberOfSteadyStateSimulation() {
        //Def.
        //minTime = 40
        //steadystate = toTime > minTime
        //PAS-IN-RA_cc3d_12_04_2014_11_03_08 -> toTime:11.0 => notInSteadyState
        //PAS-IN-RA_cc3d_12_04_2014_11_07_38 -> toTime:10.0 => notInSteadyState
        //PAS-IN-RA_cc3d_12_04_2014_11_17_16 -> toTime:5.5  => notInSteadyState
        //PAS-IN-RA_cc3d_12_04_2014_11_18_07 -> toTime:2.0  => notInSteadyState
        //PAS-IN-RA_cc3d_12_04_2014_14_37_30 -> toTime:8.5  => notInSteadyState
        //PAS-IN-RA_cc3d_12_07_2014_19_38_30 -> toTime:100  => inSteadyState     //manuell in der Datei geänderter toTime Wert

        Assert.assertEquals("Modeltype should have '1' in steady state simulations and not: " + modeltypeOverview.getNumberOfSteadyStateSimulation(),1, modeltypeOverview.getNumberOfSteadyStateSimulation());
    }

    @Test
    public void numberOfAbortedSimulations() {
        //Def.
        //isAborted = lastFitness <0.05 && isInSteadyState
        //PAS-IN-RA_cc3d_12_04_2014_11_03_08 -> notInSteadyState && lastFitness=0.407548176606 => isNotAborted
        //PAS-IN-RA_cc3d_12_04_2014_11_07_38 -> notInSteadyState && lastFitness=0.42091454367  => isNotAborted
        //PAS-IN-RA_cc3d_12_04_2014_11_17_16 -> notInSteadyState && lastFitness=0.343574898698 => isNotAborted
        //PAS-IN-RA_cc3d_12_04_2014_11_18_07 -> notInSteadyState && lastFitness=0.279540750852 => isNotAborted
        //PAS-IN-RA_cc3d_12_04_2014_14_37_30 -> notInSteadyState && lastFitness=0.747226637591 => isNotAborted
        //PAS-IN-RA_cc3d_12_07_2014_19_38_30 -> inSteadyState    && lastFitness=0.01 => isAborted        //manuell in der Datei geänderter toTime und lastFitness

        Assert.assertEquals("Modeltype should have '1' aborted simulations and not: " + modeltypeOverview.getNumberOfAbortedSimulations(),1, modeltypeOverview.getNumberOfAbortedSimulations());
    }

    @Test
    public void numberOfCompletedSimulations() {
        //Def.
        //maxTime = 700
        //isCompleted = toTime >= maxTime
        //PAS-IN-RA_cc3d_12_04_2014_11_03_08 -> toTime:11.0 => notCompleted
        //PAS-IN-RA_cc3d_12_04_2014_11_07_38 -> toTime:10.0 => notCompleted
        //PAS-IN-RA_cc3d_12_04_2014_11_17_16 -> toTime:5.5  => notCompleted
        //PAS-IN-RA_cc3d_12_04_2014_11_18_07 -> toTime:2.0  => notCompleted
        //PAS-IN-RA_cc3d_12_04_2014_14_37_30 -> toTime:8.5  => notCompleted
        //PAS-IN-RA_cc3d_12_07_2014_19_38_30 -> toTime:100  => isCompleted

        Assert.assertEquals("Modeltype should have '1' completed simulations and not: " + modeltypeOverview.getNumberOfCompletedSimulations(),1, modeltypeOverview.getNumberOfCompletedSimulations());
    }

    @Test
    public void statisticValueOfFitnessPlot() {
        String mean = f.format(modeltypeOverview.getStatisticValues().get(1).getMean());
        String stdDev = f.format(modeltypeOverview.getStatisticValues().get(1).getStdDev());

        Assert.assertEquals("Name of Metrictype should be 'FitnessPlot' and not: " + modeltypeOverview.getStatisticValues().get(1).getMetricTypeName(), "FitnessPlot",modeltypeOverview.getStatisticValues().get(1).getMetricTypeName() );
        Assert.assertEquals("Mean of FitnessPlot should be '0,37' and not: " + mean, "0,37" , mean);
        Assert.assertEquals("Standard Deviation of FitnessPlot should be '0,05' and not: " + stdDev, "0,05" , stdDev);
    }
}
