package de.hs.mannheim.modUro.fx;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;

/**
 * @author Markus Gumbel (m.gumbel@hs-mannheim.de)
 */
public class ModuroTreeItem extends TreeItem<String> {

    private Object o;

    public ModuroTreeItem(String label, Object o) {
        this(label, o, null);
    }

    public ModuroTreeItem(String label, Object o, Node graphic) {
        super(label, graphic);
        this.o = o;
    }

    public Object getObject() {
        return o;
    }
}
