package de.hs.mannheim.modUro.model;

import java.io.File;
import java.util.List;

/**
 * Model class for a Node.
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */

public class Node {

    private String name;                        //name of the node
    private File path;                          //path of the node

    /**
     * Default Node Constructor.
     */
    public Node() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getPath() {
        return path;
    }

    public void setPath(File path) {
        this.path = path;
    }

}
