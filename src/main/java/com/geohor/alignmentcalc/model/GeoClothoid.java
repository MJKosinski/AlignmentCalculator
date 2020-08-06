package com.geohor.alignmentcalc.model;

public class GeoClothoid implements AligmentElement{

    private CogoPoint startCoord;
    private CogoPoint PICoord;    // intersection of long and short tangent
    private CogoPoint endCoord;
    private double spiralLength;
    private double radiusEnd;
    private double radiusStart;
    private boolean isClockWiseRotated;
    private final double THETA;
    private final double A_PARAM;
    private double startStation;
    private double endStation;
    private boolean isArcOnBegin;
    private double expected_precision;


    public GeoClothoid(CogoPoint startCoord, CogoPoint PICoord, CogoPoint endCoord, double spiralLength, double radiusEnd, double radiusStart,boolean isClockWiseRotated, double theta_deg, double startStation) {
        this.startCoord = startCoord;
        this.PICoord = PICoord;
        this.endCoord = endCoord;
        this.spiralLength = spiralLength;
        this.radiusEnd = radiusEnd;
        this.radiusStart = radiusStart;
        this.isClockWiseRotated = isClockWiseRotated;
        this.THETA = theta_deg*Math.PI/180;         //change deg to rad
        this.startStation = startStation;
        this.endStation = startStation + spiralLength;
        this.isArcOnBegin = radiusStart > radiusEnd;
        this.A_PARAM = Math.sqrt(Math.pow(spiralLength, 2) / (2D * this.THETA));
        this.expected_precision = 0.00005;
    }

    public boolean isClockWiseRotated() {
        return isClockWiseRotated;
    }

    public boolean isClockWiseRotatedFromBeginOfSpiral() {
        return isArcOnBegin == !isClockWiseRotated();
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

    public CogoPoint getStartOfSpiralCoord() {

        if(isArcOnBegin()) {
            return getEndCoord();
        }
        else return getStartCoord();
    }

    public CogoPoint getEndOfSpiralCoord() {
        if(isArcOnBegin()){
            return getStartCoord();
        }
        else return getEndCoord();
    }


    @Override
    public double[] getStationAndOffset(CogoPoint p) {

        double[] result = interpolateLength(this.getSpiralLength() * 0.5, p, this.getSpiralLength() * 0.5);  //length and offset from begin of spiral

        if(isArcOnBegin()) {
            result[0] = getEndStation()-result[0];
        }
        else {
            result[0] = result[0] + getStartStation();
        }

        if(isClockWiseRotated()) {
            result[1] = -result[1];
        }

        return result;
    }

    @Override
    public boolean isAdjacent(CogoPoint p) {

        if (isInFirstOrFourthQuater(this.getStartCoord().getAngleOf(this.getPICoord(),p)) || isInFirstOrFourthQuater(this.getEndCoord().getAngleOf(this.getPICoord(),p))) {
            return true;
        }
        return false;
    }

    public double getRadiusEnd() {
        return radiusEnd;
    }

    public double getRadiusStart() {
        return radiusStart;
    }

    public double getTheta() {
        return THETA;
    }

    public double getStartStation() {
        return startStation;
    }

    public double getEndStation() {
        return endStation;
    }

    public double getA() {
        return A_PARAM;
    }

    public boolean isArcOnBegin() {
        return isArcOnBegin;
    }

    public double getSpiralLength() {
        return spiralLength;
    }

    double getStation(double length) {
        double station;
        if (this.isArcOnBegin()) {
            station = this.getEndStation() - length;
        }
        else {
            station = this.getStartStation() + length;
        }
        return station;
    }

    public CogoPoint getPointByLength(double l, CogoPoint beginOfSpiral , boolean isClockWiseRotatedFromBeginOfSpiral) {
        double seriesSumX = 0;
        double seriesSumY = 0;
        double localL = l / getA();
        for (int i = 1; i < 10; i++) {
            seriesSumX += Math.pow(-1, i + 1) * (Math.pow(localL, 4 * i - 3) / (factorial((2 * i) - 2) * ((4 * i) - 3) * Math.pow(2, (2 * i) - 2)));
            seriesSumY += Math.pow(-1, i + 1) * (Math.pow(localL, 4 * i - 1) / (factorial((2 * i) - 1) * ((4 * i) - 1) * Math.pow(2, (2 * i) - 1)));
        }
        double localDX = seriesSumX * getA();
        double localDY = isClockWiseRotatedFromBeginOfSpiral ? seriesSumY * getA() : -seriesSumY * getA();
        return beginOfSpiral.getXYFromRectangularOffset(getPICoord(), localDX, localDY);
    }

    //gives length from BEGINNING of spiral (where R = infinity) and negative offset to the centerpoint direction
    public double[] interpolateLength(double l, CogoPoint projected, double delthaL) {

        double tau = Math.pow(l, 2) / (2D*Math.pow(getA(), 2));
        double localR = Math.pow(getA(), 2) / l;
        double currentAsimuthOfNormal = (isClockWiseRotatedFromBeginOfSpiral()) ?
                getStartOfSpiralCoord().radiusAzimuth(getPICoord()) + (tau + Math.PI*0.5) : getStartOfSpiralCoord().radiusAzimuth(getPICoord()) - (tau + Math.PI *0.5);
        CogoPoint onSpiral = getPointByLength(l, getStartOfSpiralCoord(), isClockWiseRotatedFromBeginOfSpiral());
        CogoPoint centerPoint = new CogoPoint(onSpiral.getX() + localR * Math.cos(currentAsimuthOfNormal), onSpiral.getY() + localR * Math.sin(currentAsimuthOfNormal));

        double angleFromCenterToProjected = onSpiral.getAngleOf(centerPoint, projected);

        if (delthaL > expected_precision) {


            if (angleFromCenterToProjected < Math.PI && isClockWiseRotatedFromBeginOfSpiral()) {
                return interpolateLength(l - (delthaL / 2), projected, (delthaL / 2)); //l - (delthaL / 2);
            } else if (angleFromCenterToProjected > Math.PI && !isClockWiseRotatedFromBeginOfSpiral()) {
                return interpolateLength(l - (delthaL / 2), projected, (delthaL / 2));
            } else return interpolateLength(l + (delthaL / 2), projected, (delthaL / 2));

        }

        double offset = onSpiral.distance(projected);

        if(angleFromCenterToProjected < 0.5*Math.PI || angleFromCenterToProjected > 1.5*Math.PI) offset = -offset;



        //TODO  check angle fromCenterToProjected =~ 0 or pi
        return new double[] {l,offset};

    }

    public long factorial(int n) {
        long fact = 1;
        for (int i = 2; i <= n; i++) {
            fact = fact * i;
        }
        return fact;
    }

    public boolean isInFirstOrFourthQuater(double angle) {
        return angle <= Math.PI * .5 || angle >= Math.PI * 1.5;

    }


}
