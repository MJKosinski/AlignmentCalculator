package com.geohor.alignmentcalc;

import com.geohor.alignmentcalc.model.CogoPoint;
import com.geohor.alignmentcalc.model.GeoLine;
import org.apache.commons.math3.util.Precision;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AlignmentcalcApplicationTests {

    @Test
    void contextLoads() {
    }


    @Test
    public void geoLineTest() {
        CogoPoint start = new CogoPoint(16600,-271000);
        CogoPoint mid = new CogoPoint(16932.1,-270498.387);
        CogoPoint end = new CogoPoint(16700,-270000);
        CogoPoint p1 = new CogoPoint(16627.681,-271148.67);  // isAdjacent should be false, station should be negative
        CogoPoint p2 = new CogoPoint(16858.18,-270833.46);  // isAdjacent should be true(l1), offset should be negative
        CogoPoint p3 = new CogoPoint(16801.15,-270551.85);  // 484.718;79.674

        GeoLine line1 = new GeoLine(start,mid,0);
        GeoLine line2 = new GeoLine(mid,end,601.586);


        Assertions.assertEquals(601.586D, Precision.round(line1.getLength(),3),"line length calculation");
        Assertions.assertTrue(line1.getStation(p1)<0);
        Assertions.assertEquals(281.390D, Precision.round(line1.getStation(p2),3,4),"Station calculation");
        Assertions.assertEquals(-123.338D, Precision.round(line1.getOffset(p2),3),"offset calculation");
        Assertions.assertEquals(484.718D, Precision.round(line1.getStation(p3),3),"Station calculation");
        Assertions.assertEquals(79.675D, Precision.round(line1.getOffset(p3),3,4),"offset calculation");





    }


}
