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
 * SettingFileMarshaller for XML Output of SettingFiles.
 * Warning: Method names are related to the JAXB conventions.
 * Do not change the names.
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 * @author Markus Gumbel (m.gumbel@hs-mannheim.de)
 */
@XmlRootElement(name = "moduro")
public class SettingFileMarshaller {

    private ParameterSetting params;
    private List<ProjectSetting> project;

    public ParameterSetting getParameter() {
        return params;
    }

    public void setParameter(ParameterSetting parameter) {
        this.params= parameter;
    }

    /**
     * Gets List of SettingFiles.
     *
     * @return
     */
    public List<ProjectSetting> getProject() {
        return project;
    }

    /**
     * Sets List of SettingsFiles.
     *
     * @param project
     */
    public void setProject(List<ProjectSetting> project) {
        this.project = project;
    }
}
