package com.geohor.alignmentcalc.model;

public class GeoClothoid {

    private CogoPoint startCoord;
    private CogoPoint PICoord;    // intersection of long and short tangent
    private CogoPoint endCoord;
    private double spiralLength;
    private double radiusEnd;
    private double radiusStart;
    private RotationDirection rotation;
    private double theta;
    private double startStation;
    private double endStation;


    public GeoClothoid(CogoPoint startCoord, CogoPoint PICoord, CogoPoint endCoord, double spiralLength, double radiusEnd, double radiusStart, RotationDirection rotation, double theta, double startStation) {
        this.startCoord = startCoord;
        this.PICoord = PICoord;
        this.endCoord = endCoord;
        this.spiralLength = spiralLength;
        this.radiusEnd = radiusEnd;
        this.radiusStart = radiusStart;
        this.rotation = rotation;
        this.theta = theta;
        this.startStation = startStation;
        this.endStation = startStation + spiralLength;

    }

    public CogoPoint getStartCoord() {
        return startCoord;
    }

    public CogoPoint getPICoord() {
        return PICoord;
    }

    public CogoPoint getEndCoord() {
        return endCoord;
    }

    public double getRadiusEnd() {
        return radiusEnd;
    }

    public double getRadiusStart() {
        return radiusStart;
    }

    public double getTheta() {
        return theta;
    }

    public double getStartStation() {
        return startStation;
    }

    public double getEndStation() {
        return endStation;
    }

    public double getSpiralLength() {
        return spiralLength;
    }


    public RotationDirection getRotation() {
        return rotation;
    }

    double getStation(CogoPoint p) {
        boolean isArcOnBegin = radiusStart > radiusEnd;
        double paramR;
        double paramA;
        double localStation = spiralLength/2D;
        CogoPoint zeroCoord; // Start of clothoid (R=infinity)
        CogoPoint arcCoord;  // End of clothoid (R=paramR)
        if(isArcOnBegin) {
            paramR = radiusStart;
            zeroCoord = endCoord;
            arcCoord = startCoord;
        }
        else {
            paramR = radiusEnd;
            zeroCoord = startCoord;
            arcCoord = endCoord;
        }

        paramA = Math.sqrt(spiralLength*paramR);
        // first check L/2
        double searchedL = getSpiralLength()/2D;
        double delthaL = getSpiralLength()/2D;
        CogoPoint onSpiral = getPointByLength(searchedL,paramA,zeroCoord,endCoord,getRotation());







        return 0;
    }

    public CogoPoint getPointByLength(double l, double a, CogoPoint begin, CogoPoint end, RotationDirection rotation){
        double seriesSumX = 0;
        double seriesSumY = 0;
        double localL = l/a;
        for (int i = 1; i < 10; i++) {
            seriesSumX += Math.pow(-1,i+1) * (Math.pow(localL,4*i-3)/(factorial((2*i)-2)*((4*i)-3)*Math.pow(2,(2*i)-2)));
            seriesSumY += Math.pow(-1,i+1) * (Math.pow(localL,4*i-1)/(factorial((2*i)-1)*((4*i)-1)*Math.pow(2,(2*i)-1)));
        }
        double localDX = seriesSumX/a;
        double localDY = seriesSumY/a;
        return begin.getXYFromRectangularOffset(end, localDX, localDY);
    }

    public long factorial(int n) {
        long fact = 1;
        for (int i = 2; i <= n; i++) {
            fact = fact * i;
        }
        return fact;
    }

    public double approximateLength(double l, CogoPoint onSpiral, CogoPoint projected ,double a, double delthaL) {  //todo onSpiral is redundant
        double tau = Math.pow(l,2)/Math.pow(a,2);
        double localR = Math.pow(a,2)/l;
        double currentAsimuthOfNormal =  (rotation == RotationDirection.CLOCKWISE_DIRECTION) ? getStartCoord().radiusAzimuth(getEndCoord())+(tau+Math.PI/2) : getStartCoord().radiusAzimuth(getEndCoord())-(tau+Math.PI/2);
//        double toProjectedAsimuth = onSpiral.radiusAzimuth(projected);
        CogoPoint centerPoint = new CogoPoint(onSpiral.getX()+localR*Math.cos(currentAsimuthOfNormal),onSpiral.getY()+localR*Math.sin(currentAsimuthOfNormal));
        double angleFromCenterToProjected = onSpiral.getAngleOf(centerPoint,projected);

        if (delthaL < 0.0001) return l;

        else if (angleFromCenterToProjected < Math.PI/2 && rotation == RotationDirection.CLOCKWISE_DIRECTION) {
            return approximateLength(l, onSpiral,projected ,a, l-(delthaL/2));
        }
        else if(angleFromCenterToProjected > Math.PI/2 && rotation == RotationDirection.COUNTER_CLOCKWISE_DIRECTION) {
            return approximateLength(l, onSpiral,projected ,a, l-(delthaL/2));
        }
        else return approximateLength(l, onSpiral,projected ,a, l+(delthaL/2));
    }





}
