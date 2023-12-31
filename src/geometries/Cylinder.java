package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.ArrayList;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 *  Finite Cylinder in 3D space
 */
public class Cylinder extends Tube{
    final private double _height;

    /**
     * constructor that uses the constructor of the Tube
     * @param axisRay  axis ray
     * @param radius   radius
     * @param height   height
     */
    public Cylinder(Ray axisRay, double radius, double height) {
        super(axisRay, radius);
        this._height = height;
    }

    /**
     *
     * @return the height of the cylinder
     */
    public double getHeight() {
        return _height;
    }

    @Override
    public String toString() {
        return "Cylinder:" +
                "_height=" + _height +
                ", _axisRay=" + _axisRay +
                ", _radius=" + _radius ;
    }

    /**
     * calculate the normal by the point
     * @param point {@link Point} external to the shape
     * @return the normal of the cylinder
     */
    @Override
    public Vector getNormal(Point point) {
        Point o = _axisRay.getP0();
        Vector v = _axisRay.getDirection();

        // projection of P-O on the ray:
        double t;
        try {
            t = alignZero(point.subtract(o).dotProduct(v));
        } catch (IllegalArgumentException e) { // P = O
            return v;
        }

        // if the point is at a base
        if (t == 0 || isZero(_height - t)) // if it's close to 0, we'll get ZERO vector exception
            return v;

        o = o.add(v.scale(t));
        return point.subtract(o).normalize();
    }

    /**
     *
     * @param ray
     * @param maxDistance
     * @return a list of the intersections
     */
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        List<GeoPoint> res = new ArrayList<>();
        List<GeoPoint> lst = super.findGeoIntersectionsHelper(ray, maxDistance);
        if (lst != null)
            for (GeoPoint geoPoint : lst) {
                double distance = alignZero(geoPoint.point.subtract(_axisRay.getP0()).dotProduct(_axisRay.getDirection()));
                if (distance > 0 && distance <= _height)
                    res.add(geoPoint);
            }

        if (res.size() == 0)
            return null;
        return res;
    }
}
