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

import java.util.List;

/**
 * Model class for a Project.
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class Project {

    private String name;                        //name of the project
    private List<Node> nodes;                   //list of nodes for this project
    List<ModelType> modelTypeList;


    /**
     * Constructor
     *
     * @param name
     * @param nodes
     */
    public Project(String name, List<Node> nodes, List<ModelType> modelTypeList) {
        this.name = name;
        this.nodes = nodes;
        this.modelTypeList = modelTypeList;
    }

    /*Getter & Setter*/
    public String getName() {
        return name;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<ModelType> getModelTypeList() {
        return modelTypeList;
    }
}
