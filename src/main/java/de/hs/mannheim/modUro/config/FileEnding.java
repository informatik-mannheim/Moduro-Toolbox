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
package de.hs.mannheim.modUro.config;

/**
 * Configuration for Files.
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public enum FileEnding {

    IMAGEFILE(".png"),           //Image file ending
    METRIC_DATA_FILE(".dat"),    //FitnessPlot.dat -> contains metric data
    METRICTYPE_FILE(".dat");     //Metrictype files

    private final String fileEnding;

    FileEnding(String fileEnding) {
        this.fileEnding = fileEnding;
    }

    public String getFileEnding() {
        return fileEnding;
    }
}
