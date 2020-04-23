package com.geohor.alignmentcalc.model;

public class GeoCurve {


    private CogoPoint startCoord;
    private CogoPoint endCoord;
    private CogoPoint centerCoord;
    private double radius;

    public GeoCurve(CogoPoint startCoord, CogoPoint endCoord, CogoPoint centerCoord) {
        this.startCoord = startCoord;
        this.endCoord = endCoord;
        this.centerCoord = centerCoord;
        this.radius = centerCoord.distance(startCoord);  // TODO check distance to endCoord as well
    }

    public CogoPoint getStartCoord() {
        return startCoord;
    }

    public CogoPoint getEndCoord() {
        return endCoord;
    }

    public CogoPoint getCenterCoord() {
        return centerCoord;
    }

    public double getRadius() {
        return radius;
    }




}
