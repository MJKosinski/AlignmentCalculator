package com.geohor.alignmentcalc.model;

public class GeoLine implements AligmentElement {

    private CogoPoint startCoord;
    private CogoPoint endCoord;
    private double startStation;
    private double endStation;


    public GeoLine(CogoPoint startCoord, CogoPoint endCoord, double startStation) {
        this.startCoord = startCoord;
        this.endCoord = endCoord;
        this.startStation = startStation;
        this.endStation = startStation + getLength();
    }

    @Override
    public double getStartStation() {
        return startStation;
    }

    @Override
    public double getEndStation() {
        return endStation;
    }

    public void setStartStation(double startStation) {
        this.startStation = startStation;
        this.endStation = startStation + getLength();
    }

    public CogoPoint getStartCoord() {
        return startCoord;
    }

    public void setStartCoord(CogoPoint startCoord) {

        this.startCoord = startCoord;
    }

    public CogoPoint getEndCoord() {
        return endCoord;
    }

    @Override
    public double getStation(CogoPoint point) {

        double u = (((point.getX()-getStartCoord().getX())*(getEndCoord().getX()-getStartCoord().getX()))
                +((point.getY()-getStartCoord().getY())*(getEndCoord().getY()-getStartCoord().getY())))
                /Math.pow(getLength(),2);

        CogoPoint pointOnLine = new CogoPoint();
        pointOnLine.setLocation(getStartCoord().getX()+(u*(getEndCoord().getX()-getStartCoord().getX())),getStartCoord().getY()+(u*(getEndCoord().getY()-getStartCoord().getY())));


        double localStation = u*getLength();
//        double offset;
//        double angleBetweenLineAndPoint = getStartCoord().gradeAzimuth(point)-getStartCoord().gradeAzimuth(getEndCoord());
//        if (angleBetweenLineAndPoint<0) angleBetweenLineAndPoint+=400;
//        if(angleBetweenLineAndPoint < 200) {
//            offset= pointOnLine.distance(point);
//        }
//        else offset = -pointOnLine.distance(point);

        return localStation+startStation;
    }


    @Override
    public double getOffset(CogoPoint point){
        double localStation = getStation(point)-startStation;
        double alphaAngle = getStartCoord().getAngleOf(getEndCoord(),point);
        return localStation*Math.tan(alphaAngle);
    }

    @Override
    public boolean isAdjacent(CogoPoint p) {
        return false;
    }

    public void setEndCoord(CogoPoint endCoord) {
        this.endCoord = endCoord;
    }

    public double getLength() {
        return startCoord.distance(endCoord);
    }


}
