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
package de.hs.mannheim.modUro.reader;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 */
public class CelltimesReaderTest {

    @Test
    public void testCelltypes() throws Exception {
        String s = "src/test/resources/Simulationdata/Projekt1/node1/CM-IN-DAE_cc3d_01_15_2015_12_53_49/Celltimes.daz";
        CelltimesReader r = new CelltimesReader(s, 0.5, 20);
        List<String> types = r.getCellTypes();
        assertEquals("1: BM", types.get(0));
        assertEquals("2: S", types.get(1));
        assertEquals("3: B", types.get(2));
        assertEquals("4: I", types.get(3));
    }

    @Test
    public void testTimes() throws Exception {
        String s = "src/test/resources/Simulationdata/Projekt1/node1/CM-IN-DAE_cc3d_01_15_2015_12_53_49/Celltimes.daz";
        CelltimesReader r = new CelltimesReader(s, 0.5, 2.0);
        List<CellCycletimeEntry> l = r.getCycletimes();
        CellCycletimeEntry e1 = l.get(0);
        assertEquals(2.0, e1.time, 0.001);
        CellCycletimeEntry e2 = l.get(1);
        assertEquals(6.0, e2.time, 0.001);
        CellCycletimeEntry e3 = l.get(2);
        assertEquals(10.0, e3.time, 0.001);
    }

    @Test
    public void testValues() throws Exception {
        String s = "src/test/resources/Simulationdata/Projekt1/node1/CM-IN-DAE_cc3d_01_15_2015_12_53_49/Celltimes.daz";
        CelltimesReader r = new CelltimesReader(s, 0.5, 2.0);
        List<CellCycletimeEntry> l = r.getCycletimes();
        // Hmm, the first entry is t=2.0 and has no entries.
        CellCycletimeEntry e1 = l.get(1);
        assertEquals(5.0, e1.meanValues.get("2: S"), 0.001);
        CellCycletimeEntry e2 = l.get(2);
        assertEquals(4.5, e2.meanValues.get("3: B"), 0.001);

    }
}