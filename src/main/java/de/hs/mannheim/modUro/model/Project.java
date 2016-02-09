package de.hs.mannheim.modUro.model;

import de.hs.mannheim.modUro.model.ModelType;
import de.hs.mannheim.modUro.model.Node;

import java.util.List;

/**
 * Model class for a Project.
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class Project {

    private String name;                        //name of the project
    private List<Node> nodes;                   //list of nodes for this project
    List<ModelType> modelTypeList;


    /**
     * Constructor
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
