package de.hs.mannheim.modUro.model.dialog;

import de.hs.mannheim.modUro.model.Node;
import java.util.List;

/**
 * Class for SettingFile Instance.
 * @author  Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class SettingFile {

    private String name;
    private List<Node> node;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Node> getNode() {
        return node;
    }

    public void setNode(List<Node> node) {
        this.node = node;
    }
}
