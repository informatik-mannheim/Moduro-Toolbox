package de.hs.mannheim.modUro.creator;

import de.hs.mannheim.modUro.model.MainModel;
import de.hs.mannheim.modUro.model.ModelType;
import de.hs.mannheim.modUro.model.Project;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * JUnit Test for ModeltypeCreatorTest.
 * Test data: first project (Project1)-> first Modeltype (CM-IN-DAE)
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class ModeltypeCreatorTest {
    MainModel mainModel;
    Project project;
    List<Project> projectList;
    ModelType modelType;

    @Before
    public void setUp() {
        mainModel = new MainModel("/setting/Setting.xml");
        projectList = mainModel.getProjectData();
        project = projectList.get(0);
        modelType = project.getModelTypeList().get(0);
    }

    @Test
    public void nameOfFirstModeltypeInModeltypeList() {
        Assert.assertEquals("Modeltype name should be 'CM-IN-DAE' and not: " + modelType.getName(), "CM-IN-DAE", modelType.getName());
    }

    @Test
    public void countSimulationInSpecificModeltype() {
        Assert.assertEquals("Modeltype should have '4' Simulations and not: " + modelType.getSimulations().size(), 4, modelType.getSimulations().size());
    }
}

