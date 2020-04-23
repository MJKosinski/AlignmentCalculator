package com.geohor.alignmentcalc.model;

public interface AligmentElement {

    public double getStartStation();
    public double getEndStation();
    public CogoPoint getStartCoord();
    public CogoPoint getEndCoord();
    public double getStation(CogoPoint p);
    public double getOffset(CogoPoint p);
    public boolean isAdjacent(CogoPoint p);


}
