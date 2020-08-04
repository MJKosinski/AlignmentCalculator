package com.geohor.alignmentcalc.model;

import java.util.Collection;

public class GeoArc implements AligmentElement {
//this is type of arcus that is imported from Civil3D xml raport

    private CogoPoint startCoord;
    private CogoPoint endCoord;
    private CogoPoint centerCoord;
    private CogoPoint piCoord;    // tangent intersection point
    private double radius;
    private RotationDirection rotation;
    private boolean isClockWiseRotated;
    private double startStation;
    private double endStation;


    public GeoArc(CogoPoint startCoord, CogoPoint endCoord, CogoPoint centerCoord, CogoPoint piCoord, double radius, double startStation, boolean isClockWiseRotated) {
        this.startCoord = startCoord;
        this.endCoord = endCoord;
        this.centerCoord = centerCoord;
        this.piCoord = piCoord;
        this.radius = radius;
        this.startStation = startStation;
        this.endStation = startStation + getLength();
        this.isClockWiseRotated = isClockWiseRotated;
    }

    public CogoPoint getCenterCoord() {
        return centerCoord;
    }

    public double getRadius() {
        return radius;
    }

    public RotationDirection getRotation() {
        return rotation;
    }

    @Override
    public double getStartStation() {
        return startStation;
    }

    @Override
    public double getEndStation() {
        return endStation;
    }

    @Override
    public CogoPoint getStartCoord() {
        return startCoord;
    }

    @Override
    public CogoPoint getEndCoord() {
        return endCoord;
    }


    public double getStation(CogoPoint p) {
        return startStation + getLength(p);
    }


    public double getOffset(CogoPoint p) {

        double offset = 0;

        if(isClockWiseRotated()) {
            offset = radius - centerCoord.distance(p);
        }
        else {
            offset = centerCoord.distance(p) - radius;
        }
        return offset;

    }

    @Override
    public double[] getStationAndOffset(CogoPoint p) {
        return new double[] {getStation(p),getOffset(p)};
    }

    @Override
    public boolean isAdjacent(CogoPoint p) {

        if(centerCoord.getAngleOf(startCoord, p) <= centerCoord.getAngleOf(startCoord, endCoord) && isClockWiseRotated()) {
            return true;
        }
        else if (centerCoord.getAngleOf(startCoord, p) >= centerCoord.getAngleOf(startCoord, endCoord) && !isClockWiseRotated()) {
            return true;
        }

        else return false;
    }

    public double getLength() {
        if(isClockWiseRotated())
            return centerCoord.getAngleOf(startCoord, startCoord) * radius;
        else
        return centerCoord.getAngleOf(endCoord, startCoord) * radius;
    }

    public double getLength(CogoPoint p) {
        if(isClockWiseRotated())
            return centerCoord.getAngleOf(startCoord, p) * radius;
        else
        return centerCoord.getAngleOf(p, startCoord) * radius;
    }

    public boolean isClockWiseRotated() {
        return isClockWiseRotated;
    }
}
