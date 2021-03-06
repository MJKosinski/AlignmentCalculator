package com.geohor.alignmentcalc;

import com.geohor.alignmentcalc.model.*;
import org.apache.commons.math3.util.Precision;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

//TODO  needs given-when-then refactor

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
        CogoPoint p4 = new CogoPoint(16921.2,-270328.4);  // 760.284 ; -61.882 (l2)

        GeoLine line1 = new GeoLine(start,mid,0);
        GeoLine line2 = new GeoLine(mid,end,601.586);

        Assertions.assertEquals(601.586D, Precision.round(line1.getLength(),3),"line length calculation");
        Assertions.assertTrue(line1.getStation(p1)<0);
        Assertions.assertEquals(281.390D, Precision.round(line1.getStation(p2),3,4),"Station calculation");
        Assertions.assertEquals(-123.338D, Precision.round(line1.getOffset(p2),3),"offset calculation");
        Assertions.assertEquals(484.718D, Precision.round(line1.getStation(p3),3),"Station calculation");
        Assertions.assertEquals(79.675D, Precision.round(line1.getOffset(p3),3,4),"offset calculation");
        Assertions.assertFalse(line1.isAdjacent(p1));
        Assertions.assertTrue(line1.isAdjacent(p3));
        Assertions.assertFalse(line1.isAdjacent(p4));
    }

    @Test
    public void geoArcTest() {
        CogoPoint start = new CogoPoint(16726.346874354378,-270056.57449860167);
        CogoPoint end = new CogoPoint(16739.244433389293,-269951.47468569671);
        CogoPoint center = new CogoPoint(16816.99866073838,-270014.3577528082);
        CogoPoint start2 = new CogoPoint(16833.937488571559 ,-269834.38775467972);
        CogoPoint end2 = new CogoPoint(16865.12292709891 ,-269714.56414063589);
        CogoPoint center2 = new CogoPoint(16717.306147547937 ,-269740.06315401249);

//        GeoCurve curve1 = new GeoCurve(start,end,center,1088.9593);

        GeoArc arc1 = new GeoArc(start,end,center,center,100D,1088.9593, false);
        GeoArc arc2 = new GeoArc(start2,end2,center2,center,150D,1351.1336, true);

        Assertions.assertEquals(111.588, Precision.round(arc1.getLength(),3),"Curve length calculation");
        Assertions.assertEquals(100, Precision.round(arc1.getRadius(),3),"Curve radius calculation");
        Assertions.assertFalse(arc1.isAdjacent(new CogoPoint(16801.15,-270551.85)));
        Assertions.assertFalse(arc1.isAdjacent(new CogoPoint(16750.642,-269922.32)));
        Assertions.assertFalse(arc1.isAdjacent(new CogoPoint(16834.9,-270016.195)));
        Assertions.assertTrue(arc1.isAdjacent(new CogoPoint(16786,-270011)));
        Assertions.assertTrue(arc1.isAdjacent(new CogoPoint(16695,-269990)));
        Assertions.assertEquals(1144.099, Precision.round(arc1.getStation(new CogoPoint(16745,-270006)),3),"Curve1 1st station calculation");
        Assertions.assertEquals(-27.518, Precision.round(arc1.getOffset(new CogoPoint(16745,-270006)),3),"Curve1 1st offset calculation");
        Assertions.assertEquals(1172.637, Precision.round(arc1.getStation(new CogoPoint(16710,-269969)),3),"Curve1 2nd station calculation");
        Assertions.assertEquals(16.215, Precision.round(arc1.getOffset(new CogoPoint(16710,-269969)),3),"Curve1 2nd offset calculation");


        Assertions.assertFalse(arc2.isAdjacent(center));
        Assertions.assertFalse(arc2.isAdjacent(new CogoPoint(16695,-269734)));
        Assertions.assertFalse(arc2.isAdjacent(new CogoPoint(16871,-269683)));
        Assertions.assertTrue(arc2.isAdjacent(new CogoPoint(16817,-269783)));
        Assertions.assertTrue(arc2.isAdjacent(new CogoPoint(16904,-269862)));
        Assertions.assertEquals(1403.628, Precision.round(arc2.getStation(new CogoPoint(16901,-269803)),3),"Curve2 1st station calculation");
        Assertions.assertEquals(-44.176, Precision.round(arc2.getOffset(new CogoPoint(16901,-269803)),3),"Curve2 1st offset calculation");


    }

    @Test
    public void geoClothoidTest() {
        CogoPoint cl1Start = new CogoPoint(5548421.8673270475, 6543670.1954692863);
        CogoPoint cl1PI = new CogoPoint(5548427.4128540587, 6543654.4775390895);
        CogoPoint cl1END = new CogoPoint(5548429.9388168203, 6543646.5354438974);
        CogoPoint proj1 = new CogoPoint(5548427.947,6543666.897);
        CogoPoint proj2 = new CogoPoint(5548424.438,	6543650.395);
        GeoClothoid cl1 = new GeoClothoid(cl1Start,cl1PI,cl1END,25,400,0,false,1.790493109784,106.0544);

        CogoPoint cl2Start = new CogoPoint(5548447.8855243046, 6543551.640399944);
        CogoPoint cl2PI = new CogoPoint(5548448.4343230929, 6543543.3243802311);
        CogoPoint cl2END = new CogoPoint(5548449.0116939694, 6543526.6668641428);
        CogoPoint proj3 = new CogoPoint(5548454.647,6543547.278);
        CogoPoint proj4 = new CogoPoint(5548445.905,6543530.828);
        GeoClothoid cl2 = new GeoClothoid(cl2Start,cl2PI,cl2END,25,0,400,false,1.790493109784,227.8677);


        Assertions.assertEquals(5548421.8673270475,cl1.getPointByLength(0,cl1Start,false).getX());
        Assertions.assertEquals(5548429.9388168203,cl1.getPointByLength(cl1.getSpiralLength(),cl1Start, false).getX());
        Assertions.assertEquals(6543646.5354438974,cl1.getPointByLength(cl1.getSpiralLength(),cl1Start,false).getY());
        Assertions.assertEquals(5548426.920,Precision.round(cl1.getPointByLength(15.3581,cl1Start,false).getX(),3));
        Assertions.assertEquals(5.127,Precision.round(cl1.interpolateLength(cl1.getSpiralLength()/2,proj1,cl1.getSpiralLength()/2)[0],3));
        Assertions.assertEquals(0,Precision.round(cl1.interpolateLength(cl1.getSpiralLength()/2,cl1Start,cl1.getSpiralLength()/2)[0],3));
        Assertions.assertEquals(25,Precision.round(cl1.interpolateLength(cl1.getSpiralLength()/2,cl1END,cl1.getSpiralLength()/2)[0],3));
        Assertions.assertEquals(25,Precision.round(cl2.interpolateLength(cl2.getSpiralLength()/2,cl2Start,cl2.getSpiralLength()/2)[0],3));
        Assertions.assertEquals(0,Precision.round(cl2.interpolateLength(cl2.getSpiralLength()/2,cl2END,cl2.getSpiralLength()/2)[0],3));
        Assertions.assertEquals(20.271,Precision.round(cl2.interpolateLength(cl2.getSpiralLength()/2,proj3,cl2.getSpiralLength()/2)[0],3));
        Assertions.assertEquals(4.269,Precision.round(cl2.interpolateLength(cl2.getSpiralLength()/2,proj4,cl2.getSpiralLength()/2)[0],3));


        Assertions.assertEquals(5548447.8855243046,cl2.getPointByLength(cl2.getSpiralLength(),cl2END,true).getX());
        Assertions.assertEquals(6543526.6668641428,cl2.getPointByLength(0,cl2END,true).getY());


        Assertions.assertArrayEquals(new double[] {111.182,4.638}, roundMe(cl1.getStationAndOffset(proj1),3));
        Assertions.assertArrayEquals(new double[] {125.660,-4.039}, roundMe(cl1.getStationAndOffset(proj2),3));
        Assertions.assertArrayEquals(new double[] {232.597,6.486}, roundMe(cl2.getStationAndOffset(proj3),3));
        Assertions.assertArrayEquals(new double[] {248.599,-2.959}, roundMe(cl2.getStationAndOffset(proj4),3));
        Assertions.assertArrayEquals(new double[] {106.054,-0.000}, roundMe(cl1.getStationAndOffset(cl1Start),3));
        Assertions.assertArrayEquals(new double[] {131.054,0.000}, roundMe(cl1.getStationAndOffset(cl1END),3));
        Assertions.assertArrayEquals(new double[] {227.868,-0.000}, roundMe(cl2.getStationAndOffset(cl2Start),3));
        Assertions.assertArrayEquals(new double[] {252.868,0.000}, roundMe(cl2.getStationAndOffset(cl2END),3));

    }

    public double[] roundMe(double[] input, int scale) {

        if(input == null) {
            return null;
        }

        for (int i = 0; i < input.length; i++) {

            input[i] = Precision.round(input[i],scale);
        }
        return input;

    }


}
