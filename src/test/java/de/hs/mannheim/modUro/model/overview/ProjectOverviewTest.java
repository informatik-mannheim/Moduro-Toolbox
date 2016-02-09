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
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class ProjectOverviewTest {

    MainModel mainModel;
    List<Project> projectList;
    Project project;

    ProjectOverview projectOverview;

    @Before
    public void setUp() {
        mainModel = new MainModel("/setting/Setting.xml");
        projectList = mainModel.getProjectData();
        project = projectList.get(1);

        projectOverview = new ProjectOverview(project);
    }

    @Test
    public void numberOfSimulationsTest() {
        Assert.assertEquals("Project should have '7' simulations and not: " + projectOverview.getNumberOfSimulations(),7, projectOverview.getNumberOfSimulations());
    }

    @Test
    public void numberOfSteadyStateSimulation() {
        //Def.
        //minTime = 40
        //steadystate = toTime > minTime
        //PAS-IN-DAE_cc3d_01_15_2015_12_46_01 -> toTime:4.5  => notInSteadyState
        //PAS-IN-RA_cc3d_12_04_2014_11_03_08  -> toTime:11.0 => notInSteadyState
        //PAS-IN-RA_cc3d_12_04_2014_11_07_38  -> toTime:10.0 => notInSteadyState
        //PAS-IN-RA_cc3d_12_04_2014_11_17_16  -> toTime:5.5  => notInSteadyState
        //PAS-IN-RA_cc3d_12_04_2014_11_18_07  -> toTime:2.0  => notInSteadyState
        //PAS-IN-RA_cc3d_12_04_2014_14_37_30  -> toTime:8.5  => notInSteadyState
        //PAS-IN-RA_cc3d_12_07_2014_19_38_30  -> toTime:100  => inSteadyState     //manuell in der Datei geänderter toTime Wert

        Assert.assertEquals("Project should have '1' in steady state simulations and not: " + projectOverview.getNumberOfSteadyStateSimulation(),1, projectOverview.getNumberOfSteadyStateSimulation());
    }

    @Test
    public void numberOfAbortedSimulations() {
        //Def.
        //isAborted = lastFitness <0.05 && isInSteadyState
        //PAS-IN-DAE_cc3d_01_15_2015_12_46_01 -> notInSteadyState && lastFitness=0.673270419438 => isNotAborted
        //PAS-IN-RA_cc3d_12_04_2014_11_03_08  -> notInSteadyState && lastFitness=0.407548176606 => isNotAborted
        //PAS-IN-RA_cc3d_12_04_2014_11_07_38  -> notInSteadyState && lastFitness=0.42091454367  => isNotAborted
        //PAS-IN-RA_cc3d_12_04_2014_11_17_16  -> notInSteadyState && lastFitness=0.343574898698 => isNotAborted
        //PAS-IN-RA_cc3d_12_04_2014_11_18_07  -> notInSteadyState && lastFitness=0.279540750852 => isNotAborted
        //PAS-IN-RA_cc3d_12_04_2014_14_37_30  -> notInSteadyState && lastFitness=0.747226637591 => isNotAborted
        //PAS-IN-RA_cc3d_12_07_2014_19_38_30  -> inSteadyState    && lastFitness=0.01 => isAborted        //manuell in der Datei geänderter toTime und lastFitness

        Assert.assertEquals("Project should have '1' aborted simulations and not: " + projectOverview.getNumberOfAbortedSimulations(),1, projectOverview.getNumberOfAbortedSimulations());
    }

    @Test
    public void numberOfCompletedSimulations() {
        //Def.
        //maxTime = 700
        //isCompleted = toTime >= maxTime
        //PAS-IN-DAE_cc3d_01_15_2015_12_46_01 -> toTime:4.5  => notCompleted
        //PAS-IN-RA_cc3d_12_04_2014_11_03_08  -> toTime:11.0 => notCompleted
        //PAS-IN-RA_cc3d_12_04_2014_11_07_38  -> toTime:10.0 => notCompleted
        //PAS-IN-RA_cc3d_12_04_2014_11_17_16  -> toTime:5.5  => notCompleted
        //PAS-IN-RA_cc3d_12_04_2014_11_18_07  -> toTime:2.0  => notCompleted
        //PAS-IN-RA_cc3d_12_04_2014_14_37_30  -> toTime:8.5  => notCompleted
        //PAS-IN-RA_cc3d_12_07_2014_19_38_30  -> toTime:100  => isCompleted

        Assert.assertEquals("Project should have '1' completed simulations and not: " + projectOverview.getNumberOfCompletedSimulations(),1, projectOverview.getNumberOfCompletedSimulations());
    }

    @Test
    public void listOfModeltypesInProject() {
        List<String> modelTypeList = new ArrayList<String>(){{add("PAS-IN-DAE"); add("PAS-IN-RA"); add("PAS-NU-DAE");}};

        Assert.assertEquals("Project should have other modeltypes.", modelTypeList, projectOverview.getKindsOfModelTypes());
    }
}
