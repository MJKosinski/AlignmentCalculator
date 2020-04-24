package com.geohor.alignmentcalc.model;

import java.awt.geom.Point2D;

public class CogoPoint extends Point2D {

    private double x;
    private double y;


    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;

    }

    public CogoPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public CogoPoint() {
    }

    public double radiusAzimuth(CogoPoint p2) {

        double deltaX = p2.getX() - getX();
        double deltaY = p2.getY() - getY();
        double fi = Math.atan(deltaY / deltaX);

        if (deltaX >= 0) {
            if (deltaY >= 0) {
                return fi;
            }
            return fi + 2D*Math.PI;
        } else {

            return fi + Math.PI;
        }

    }

    public double getAngleOf(CogoPoint left, CogoPoint right) {


        double angle = radiusAzimuth(right) - radiusAzimuth(left);

        if (angle >= 2D*Math.PI) angle -= 2D*Math.PI;
        if (angle < 0) angle += 2D*Math.PI;

        return angle;
    }

    public CogoPoint getXYFromRectangularOffset(CogoPoint end, double stat, double off) {
        double azimuth = radiusAzimuth(end);
        double resultX = getX()+(stat*Math.cos(azimuth))-(off*Math.sin(azimuth));
        double resultY = getY()+(stat*Math.sin(azimuth))+(off*Math.cos(azimuth));
        return new CogoPoint(resultX,resultY);
    }





}
