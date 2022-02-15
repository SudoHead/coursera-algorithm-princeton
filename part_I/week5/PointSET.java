package part_I.week5;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class PointSET {
    private final SET<Point2D> set;

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public PointSET() {
        this.set = new SET<>();
    }

    public int size() {
        return set.size();
    }

    private void handleNullArg() {
        throw new IllegalArgumentException("Argument is null");
    }

    public void insert(Point2D p) {
        if (p == null) handleNullArg();
        set.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) handleNullArg();
        return set.contains(p);
    }

    public void draw() {
        for (Point2D p : set) {
            StdDraw.point(p.x(), p.y());
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) handleNullArg();
        List<Point2D> listPoints = new ArrayList<>();
        for (Point2D p : set) {
            if (rect.contains(p))
                listPoints.add(p);
        }
        return listPoints;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) handleNullArg();
        Point2D nearest = null;
        for (Point2D candidate : set) {
            if (nearest == null || candidate.distanceSquaredTo(p) < nearest.distanceSquaredTo(p))
                nearest = candidate;
        }
        return nearest;
    }

    public static void main(String[] args) {
        PointSET ps = new PointSET();
        System.out.println("is empty at start: " + ps.isEmpty());
        ps.insert(new Point2D(0.5, 0.5));
        ps.insert(new Point2D(0.0, 0.0));
        ps.insert(new Point2D(0.0, 0.5));
        ps.insert(new Point2D(-0.1, -0.5));
        System.out.println("size after insertions: " + ps.size());
        System.out.println("contains point (0.0, 0.0): " + ps.contains(new Point2D(0.0, 0.0)));
        System.out.println("range with rect (-0.1,-0.1)(0.1,0.1): " + ps.range(new RectHV(-0.1, -0.1, 0.1, 0.1)));
        System.out.println("nearest to (-0.5, 0.0): " + ps.nearest(new Point2D(-0.5, 0.0)));
        ps.draw();
    }
}
