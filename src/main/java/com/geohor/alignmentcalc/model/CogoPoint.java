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
        if(deltaX == 0) {
            return deltaY >= 0 ? Math.PI*0.5 : Math.PI*1.5;
        }
        else if (deltaX > 0) {
            if (deltaY >= 0) {
                return Math.atan(deltaY / deltaX);   // I QUATER
            }
            return Math.atan(deltaY / deltaX) + 2D*Math.PI;  // IV QUATER
        } else {

            return Math.atan(deltaY / deltaX) + Math.PI;  // II or III QUATER
        }

    }

    public double getAngleOf(CogoPoint left, CogoPoint right) {


        double angle = radiusAzimuth(right) - radiusAzimuth(left);

        if (angle >= 2D*Math.PI) angle -= 2D*Math.PI;
        if (angle < 0) angle += 2D*Math.PI;

        return angle;
    }

    public CogoPoint getXYFromRectangularOffset(CogoPoint end, double stat, double off) { //X P = XA + lAP cosA AB YP = YA + lAP sinAAB
        double azimuth = radiusAzimuth(end);
        double resultX = getX()+(stat*Math.cos(azimuth))-(off*Math.sin(azimuth));
        double resultY = getY()+(stat*Math.sin(azimuth))+(off*Math.cos(azimuth));
        return new CogoPoint(resultX,resultY);
    }

}
