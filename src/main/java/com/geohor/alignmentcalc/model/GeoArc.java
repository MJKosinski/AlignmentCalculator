package com.geohor.alignmentcalc.model;

public class GeoArc implements AligmentElement {
//this is type of arcus that is imported from Civil3D xml raport

    private CogoPoint startCoord;
    private CogoPoint endCoord;
    private CogoPoint centerCoord;
    private CogoPoint piCoord;    // tangent intersection point
    private double radius;
    private RotationDirection rotation;
    private double startStation;
    private double endStation;


    public GeoArc(CogoPoint startCoord, CogoPoint endCoord, CogoPoint centerCoord, CogoPoint piCoord, double radius, double startStation, RotationDirection rotation) {
        this.startCoord = startCoord;
        this.endCoord = endCoord;
        this.centerCoord = centerCoord;
        this.piCoord = piCoord;
        this.radius = radius;
        this.startStation = startStation;
        this.endStation = startStation + getLength();
        this.rotation = rotation;
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

    @Override
    public double getStation(CogoPoint p) {
        return startStation + getLength(p);
    }

    @Override
    public double getOffset(CogoPoint p) {

        double offset = 0;

        switch (rotation) {

            case CLOCKWISE_DIRECTION:
                offset = radius - centerCoord.distance(p);
                break;


            case COUNTER_CLOCKWISE_DIRECTION:
                offset = centerCoord.distance(p) - radius;
                break;


        }

        return offset;

    }

    @Override
    public boolean isAdjacent(CogoPoint p) {

        if(centerCoord.getAngleOf(startCoord, p) <= centerCoord.getAngleOf(startCoord, endCoord) && rotation == RotationDirection.CLOCKWISE_DIRECTION) {
            return true;
        }
        else if (centerCoord.getAngleOf(startCoord, p) >= centerCoord.getAngleOf(startCoord, endCoord) && rotation == RotationDirection.COUNTER_CLOCKWISE_DIRECTION) {
            return true;
        }

        else return false;
    }

    public double getLength() {
        if(rotation == RotationDirection.CLOCKWISE_DIRECTION)
            return centerCoord.getAngleOf(startCoord, startCoord) * radius;
        else
        return centerCoord.getAngleOf(endCoord, startCoord) * radius;
    }

    public double getLength(CogoPoint p) {
        if(rotation == RotationDirection.CLOCKWISE_DIRECTION)
            return centerCoord.getAngleOf(startCoord, p) * radius;
        else
        return centerCoord.getAngleOf(p, startCoord) * radius;
    }
}
