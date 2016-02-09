package de.hs.mannheim.modUro.model.dialog;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * SettingFileWrapper for XML Output of SettingFiles.
 * @author  Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
@XmlRootElement(name = "root")
public class SettingFileWrapper {

    private List<SettingFile> project;

    /**
     * Gets List of SettingFiles.
     * @return
     */
    public List<SettingFile> getProject() {
        return project;
    }

    /**
     * Sets List of SettingsFiles.
     * @param project
     */
    public void setProject(List<SettingFile> project) {
        this.project = project;
    }
}
