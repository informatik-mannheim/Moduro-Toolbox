package de.hs.mannheim.modUro.creator;

import de.hs.mannheim.modUro.model.MainModel;
import de.hs.mannheim.modUro.model.Project;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * JUnit Test for ProjectCreatorTest.
 * Test data: first project (Project1)
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class ProjectCreatorTest {

    MainModel mainModel;
    Project project;
    List<Project> projectList;

    @Before
    public void setUp() {
        mainModel = new MainModel("/setting/Setting.xml");
        projectList = mainModel.getProjectData();
        project = projectList.get(0);
    }

    @Test
    public void projectName() {
        Assert.assertEquals("Projectname should be 'Project1' and not: " + project.getName(), "Project1", project.getName());
    }

    @Test
    public void countProjectNodes() {
        Assert.assertEquals("Project should have '2' Nodes and not: " + project.getNodes().size(), 2, project.getNodes().size());
    }

    @Test
    public void countModeltypes() {
        Assert.assertEquals("Project should have '2' different Modeltypes and not: " + project.getModelTypeList().size(), 2, project.getModelTypeList().size());
    }
}
