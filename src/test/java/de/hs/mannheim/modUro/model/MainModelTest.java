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
