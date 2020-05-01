package com.geohor.alignmentcalc.model;

public class GeoClothoid {

    private CogoPoint startCoord;
    private CogoPoint PICoord;    // intersection of long and short tangent
    private CogoPoint endCoord;
    private double spiralLength;
    private double radiusEnd;
    private double radiusStart;
    private RotationDirection rotation;
    private final double THETA;
    private final double A_PARAM;
    private double startStation;
    private double endStation;
    private boolean isArcOnBegin;
    private double expected_precision;


    public GeoClothoid(CogoPoint startCoord, CogoPoint PICoord, CogoPoint endCoord, double spiralLength, double radiusEnd, double radiusStart, RotationDirection rotation, double theta_deg, double startStation) {
        this.startCoord = startCoord;
        this.PICoord = PICoord;
        this.endCoord = endCoord;
        this.spiralLength = spiralLength;
        this.radiusEnd = radiusEnd;
        this.radiusStart = radiusStart;
        this.rotation = rotation;
        this.THETA = theta_deg*Math.PI/180;         //change deg to rad
        this.startStation = startStation;
        this.endStation = startStation + spiralLength;
        this.isArcOnBegin = radiusStart > radiusEnd;
        this.A_PARAM = Math.sqrt(Math.pow(spiralLength, 2) / (2D * this.THETA));
        this.expected_precision = 0.0001;
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


    public RotationDirection getRotation() {
        return rotation;
    }

    double getStation(CogoPoint p) {
        boolean isArcOnBegin = radiusStart > radiusEnd;
        double paramR;
        double paramA;
        double localStation = spiralLength / 2D;
        CogoPoint zeroCoord; // Start of clothoid (R=infinity)
        CogoPoint arcCoord;  // End of clothoid (R=paramR)
        if (isArcOnBegin()) {
            paramR = radiusStart;
            zeroCoord = endCoord;
            arcCoord = startCoord;
        } else {
            paramR = radiusEnd;
            zeroCoord = startCoord;
            arcCoord = endCoord;
        }

        paramA = Math.sqrt(spiralLength * paramR);
        // first check L/2
        double searchedL = getSpiralLength() / 2D;
        double delthaL = getSpiralLength() / 2D;


        return 0;
    }

    public CogoPoint getPointByLength(double l, CogoPoint beginOfSpiral, CogoPoint endOfSpiral, RotationDirection rotationFromBeginOfSpiral) {
        double seriesSumX = 0;
        double seriesSumY = 0;
        double localL = l / getA();
        for (int i = 1; i < 10; i++) {
            seriesSumX += Math.pow(-1, i + 1) * (Math.pow(localL, 4 * i - 3) / (factorial((2 * i) - 2) * ((4 * i) - 3) * Math.pow(2, (2 * i) - 2)));
            seriesSumY += Math.pow(-1, i + 1) * (Math.pow(localL, 4 * i - 1) / (factorial((2 * i) - 1) * ((4 * i) - 1) * Math.pow(2, (2 * i) - 1)));
        }
        double localDX = seriesSumX * getA();
        double localDY = rotationFromBeginOfSpiral == RotationDirection.CLOCKWISE_DIRECTION ? seriesSumY * getA() : -seriesSumY * getA();
        return beginOfSpiral.getXYFromRectangularOffset(getPICoord(), localDX, localDY);
    }


    public double interpolateLength(double l, CogoPoint projected, double delthaL) {
        System.out.print("L="+l+" / deltha ="+delthaL);
        if (delthaL > expected_precision) {
            double tau = Math.pow(l, 2) / (2D*Math.pow(getA(), 2));
            double localR = Math.pow(getA(), 2) / l;
            CogoPoint beginOfSpiral;  // coord where R = infinity
            CogoPoint endOfSpiral;  // coord where R = R of arc
            RotationDirection rotationFromBeginOfSpiral;

            if (isArcOnBegin()) {
                beginOfSpiral = getEndCoord();
                endOfSpiral = getStartCoord();
                rotationFromBeginOfSpiral = getRotation() == RotationDirection.CLOCKWISE_DIRECTION ?
                        RotationDirection.COUNTER_CLOCKWISE_DIRECTION : RotationDirection.CLOCKWISE_DIRECTION;
            } else {
                beginOfSpiral = getStartCoord();
                endOfSpiral = getEndCoord();
                rotationFromBeginOfSpiral = getRotation();
            }


            double currentAsimuthOfNormal = (rotationFromBeginOfSpiral == RotationDirection.CLOCKWISE_DIRECTION) ?
                    beginOfSpiral.radiusAzimuth(getPICoord()) + (tau + Math.PI*0.5) : beginOfSpiral.radiusAzimuth(getPICoord()) - (tau + Math.PI *0.5);

            System.out.println(" / CurrA = "+currentAsimuthOfNormal);

            CogoPoint onSpiral = getPointByLength(l, beginOfSpiral, endOfSpiral, rotationFromBeginOfSpiral);


            CogoPoint centerPoint = new CogoPoint(onSpiral.getX() + localR * Math.cos(currentAsimuthOfNormal), onSpiral.getY() + localR * Math.sin(currentAsimuthOfNormal));
            double angleFromCenterToProjected = onSpiral.getAngleOf(centerPoint, projected);


            if (angleFromCenterToProjected < Math.PI && rotation == RotationDirection.CLOCKWISE_DIRECTION) {
                return interpolateLength(l - (delthaL / 2), projected, (delthaL / 2)); //l - (delthaL / 2);
            } else if (angleFromCenterToProjected > Math.PI && rotation == RotationDirection.COUNTER_CLOCKWISE_DIRECTION) {
                return interpolateLength(l - (delthaL / 2), projected, (delthaL / 2));

            } else return interpolateLength(l + (delthaL / 2), projected, (delthaL / 2));


        }
        return l;


    }

    public long factorial(int n) {
        long fact = 1;
        for (int i = 2; i <= n; i++) {
            fact = fact * i;
        }
        return fact;
    }


}
