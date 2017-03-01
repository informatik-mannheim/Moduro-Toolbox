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
package de.hs.mannheim.modUro.creator;

import de.hs.mannheim.modUro.model.MainModel;
import de.hs.mannheim.modUro.model.Project;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * JUnit Test for ProjectTest.
 * Test data: first project (Project1)
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class ProjectTest {

    MainModel mainModel;
    Project project;
    List<Project> projectList;

    @Before
    public void setUp() {
        mainModel = new MainModel("src/test/resources/setting/Setting.xml");
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
        Assert.assertEquals("Project should have '2' different Modeltypes and not: " + project.getModuroModelList().size(), 2, project.getModuroModelList().size());
    }
}
