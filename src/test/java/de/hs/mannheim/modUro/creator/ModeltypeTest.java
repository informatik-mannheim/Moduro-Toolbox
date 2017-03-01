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
import de.hs.mannheim.modUro.model.ModuroModel;
import de.hs.mannheim.modUro.model.Project;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * JUnit Test for ModeltypeTest.
 * Test data: first project (Project1)-> first Modeltype (CM-IN-DAE)
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class ModeltypeTest {
    MainModel mainModel;
    Project project;
    List<Project> projectList;
    ModuroModel moduroModel;

    @Before
    public void setUp() {
        mainModel = new MainModel("src/test/resources/setting/Setting.xml");
        projectList = mainModel.getProjectData();
        project = projectList.get(0);
        moduroModel = project.getModuroModelList().get(0);
    }

    @Test
    public void nameOfFirstModeltypeInModeltypeList() {
        Assert.assertEquals("Modeltype name should be 'CM-IN-DAE' and not: " + moduroModel.getName(), "CM-IN-DAE", moduroModel.getName());
    }

    @Test
    public void countSimulationInSpecificModeltype() {
        Assert.assertEquals("Modeltype should have '2' Simulations and not: " + moduroModel.getSimulations().size(), 2, moduroModel.getSimulations().size());
    }
}

