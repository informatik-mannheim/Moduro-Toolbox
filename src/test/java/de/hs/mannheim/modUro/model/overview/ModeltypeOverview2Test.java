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

import de.hs.mannheim.modUro.config.FitnessName;
import de.hs.mannheim.modUro.model.MainModel;
import de.hs.mannheim.modUro.model.ModuroModel;
import de.hs.mannheim.modUro.model.Project;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * JUnit Test for ModeltypeOverviewTest.
 * Test data: Modeltype (CM-IN-DAE)
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class ModeltypeOverview2Test {

    private ModeltypeOverview modeltypeOverview;

    @Before
    public void setUp() {
        MainModel mainModel = new MainModel("src/test/resources/setting/Setting.xml");
        Project project = mainModel.getProjectData().get(0);
        ModuroModel moduroModel = project.getModuroModelList().get(0);
        modeltypeOverview = new ModeltypeOverview(moduroModel, false, false);
    }

    @Test
    @Ignore
    public void statFitnessVolume() {
        double shouldMean = 0.846130251;
        double isMean = 0;
        double shouldStdev = 0.186456332;
        double isStdev = 0;
        isMean = modeltypeOverview.getStatisticValues().
                get(FitnessName.VOLUME_FITNESS.getName()).getMean();
        isStdev = modeltypeOverview.getStatisticValues().
                get(FitnessName.VOLUME_FITNESS.getName()).getStdDev();
        Assert.assertEquals("Mean volume fitness should be " + shouldMean + " and not: " + isMean,
                shouldMean, isMean, 1E-8);
        Assert.assertEquals("Stdev volume fitness should be " + shouldStdev + " and not: " + isStdev,
                shouldStdev, isStdev, 1E-8);
    }
}
