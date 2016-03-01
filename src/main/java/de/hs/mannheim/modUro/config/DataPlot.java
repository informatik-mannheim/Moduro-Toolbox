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
 * Configurations for DataPlot.
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public enum DataPlot {

    MIN_TIME(40.0),     //min Time of a simulation to be in steady state
    MAX_TIME(700.0);    //max Time of a simulation to be done

    private final double value;

    DataPlot(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
