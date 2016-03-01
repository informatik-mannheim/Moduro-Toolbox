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
package de.hs.mannheim.modUro.model;

import de.hs.mannheim.modUro.model.MainModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class MainModelTest {

    MainModel mainModel;
    List<Project> projectList;

    @Before
    public void setUp() {
        mainModel = new MainModel("/setting/Setting.xml");
        projectList = mainModel.getProjectData();
    }

    @Test
    public void countProject() {
        Assert.assertEquals(2, mainModel.getProjectData().size());
    }

}
