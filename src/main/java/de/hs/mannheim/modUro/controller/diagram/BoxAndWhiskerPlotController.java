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
package de.hs.mannheim.modUro.controller.diagram;

import de.hs.mannheim.modUro.controller.diagram.fx.ChartViewer;
import de.hs.mannheim.modUro.model.Project;
import de.hs.mannheim.modUro.model.StatisticValues;
import de.hs.mannheim.modUro.model.diagram.BoxAndWhiskerPlotModel;
import de.hs.mannheim.modUro.reader.BoxAndWhiskersPlotDiagram;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

import java.util.*;

/**
 * BoxAndWhiskerPlotController controls BoxAndWhiskerView.
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class BoxAndWhiskerPlotController {

    private BoxAndWhiskerPlotModel bawpModle;

    @FXML
    private BorderPane boxWhiskerPane;

    private Set<String> models;
    public Map<String, StatisticValues> stats;

    public void init(Project project) {
        this.bawpModle = new BoxAndWhiskerPlotModel(project);
        BoxAndWhiskersPlotDiagram bwd = new BoxAndWhiskersPlotDiagram(bawpModle);
        ChartViewer viewer = new ChartViewer(bwd);
        boxWhiskerPane.setCenter(viewer);
    }
}
