package com.geohor.alignmentcalc.model;

public interface AligmentElement {

    public double getStartStation();
    public double getEndStation();
    public CogoPoint getStartCoord();
    public CogoPoint getEndCoord();
    public double[] getStationAndOffset(CogoPoint p);
    public boolean isAdjacent(CogoPoint p);


}
