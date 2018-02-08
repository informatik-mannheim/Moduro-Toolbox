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
 * RegEx Class for Parsing. Use RegEx.
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public enum RegEx {

    //RegEx for parse de.hs.mannheim.modUro.model type in dir; e.x. CM-IN-RA_cc3d_01_15_2015_15_21_21 --> CM-IN-RA
    //MODEL_TYPE_REG_EX("(([A-Z]{2,4}-?){2,})");
    MODEL_TYPE_REG_EX(ToolboxParameter.params.getDirnameseparator());

    private final String name;

    RegEx(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
