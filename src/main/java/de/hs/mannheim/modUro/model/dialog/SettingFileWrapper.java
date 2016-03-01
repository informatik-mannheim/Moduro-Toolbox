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
package de.hs.mannheim.modUro.model.dialog;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * SettingFileWrapper for XML Output of SettingFiles.
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
@XmlRootElement(name = "root")
public class SettingFileWrapper {

    private List<SettingFile> project;

    /**
     * Gets List of SettingFiles.
     *
     * @return
     */
    public List<SettingFile> getProject() {
        return project;
    }

    /**
     * Sets List of SettingsFiles.
     *
     * @param project
     */
    public void setProject(List<SettingFile> project) {
        this.project = project;
    }
}
