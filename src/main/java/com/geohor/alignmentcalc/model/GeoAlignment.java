package com.geohor.alignmentcalc.model;

import java.util.List;

public class GeoAlignment {

    private String name;
    private double startStation;
    private double endStation;
    private List<AligmentElement> aligmentElements;

    public GeoAlignment(String name, double startStation, List<AligmentElement> aligmentElements) {
        this.name = name;
        this.startStation = startStation;
        this.aligmentElements = aligmentElements;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getStartStation() {
        return startStation;
    }

    public void setStartStation(double startStation) {
        this.startStation = startStation;
    }

    public double getEndStation() {
        return endStation;
    }

    public void setEndStation(double endStation) {
        this.endStation = endStation;
    }

    public List<AligmentElement> getAligmentElements() {
        return aligmentElements;
    }

    public void setAligmentElements(List<AligmentElement> aligmentElements) {
        this.aligmentElements = aligmentElements;
    }
}
