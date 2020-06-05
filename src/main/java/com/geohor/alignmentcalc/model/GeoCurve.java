package com.geohor.alignmentcalc.model;

public class GeoCurve implements AligmentElement{


    private CogoPoint startCoord;
    private CogoPoint endCoord;
    private CogoPoint centerCoord;
    private double radius;
    private double startStation;
    private double endStation;



    public GeoCurve(CogoPoint startCoord, CogoPoint endCoord, CogoPoint centerCoord, double startStation) {
        this.startCoord = startCoord;
        this.endCoord = endCoord;
        this.centerCoord = centerCoord;
        this.radius = centerCoord.distance(startCoord);  // TODO check distance to endCoord as well
        this.startStation = startStation;
        this.endStation = startStation+getLength();
    }

    @Override
    public double getStartStation() {
        return startStation;
    }

    @Override
    public double getEndStation() {
        return 0;
    }

    public CogoPoint getStartCoord() {
        return startCoord;
    }

    public CogoPoint getEndCoord() {
        return endCoord;
    }

    @Override
    public double[] getStationAndOffset(CogoPoint p) {
        return new double[0];
    }


    public double getStation(CogoPoint p) {
        return startStation+getLength(p);
    }


    public double getOffset(CogoPoint p) {

        double angleOf = centerCoord.getAngleOf(startCoord, endCoord);

        return -radius+centerCoord.distance(p);
    }

    @Override
    public boolean isAdjacent(CogoPoint p) {
        return false;
    }

    public double getLength() {
        return centerCoord.getAngleOf(endCoord, startCoord)*radius;
    }

    public double getLength(CogoPoint p) {
        return centerCoord.getAngleOf(p, startCoord)*radius;
    }



    public CogoPoint getCenterCoord() {
        return centerCoord;
    }

    public double getRadius() {
        return radius;
    }




}
