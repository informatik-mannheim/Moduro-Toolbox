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

import java.awt.*;

/**
 * @author Markus Gumbel (m.gumbel@hs-mannheim.de)
 */
public class CellTypeColor {

    public static Color getColor(String celltype) {
        Color color;
        switch (celltype) {
            case "1: BM":
                color = Color.RED;
                break;
            case "2: S":
                color = Color.BLUE;
                break;
            case "3: B":
                color = new Color(255, 128, 0); // Orange
                break;
            case "4: I":
                color = Color.GREEN;
                break;
            case "5: U":
                color = Color.WHITE;
                break;
            default:
                color = Color.BLACK;
        }
        return color;
    }
}
