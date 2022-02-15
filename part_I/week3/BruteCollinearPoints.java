package part_I.week3;

import java.util.ArrayList;
import java.util.List;

public class BruteCollinearPoints {
    private final List<LineSegment> lineSegments;

    public BruteCollinearPoints(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException("Points array cannot be null");
        lineSegments = new ArrayList<LineSegment>();
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i] == null || points[i + 1] == null) {
                throw new IllegalArgumentException("No point can be null");
            }
            int rightSize = points.length - i - 1;
            double[] slopes = new double[rightSize];
            for (int j = i + 1, jj = 0; j < points.length; j++, jj++) {
                slopes[jj] = points[i].slopeTo(points[j]);
            }
            for (int jj = 0; jj < rightSize; jj++) {
                double candidateSlope = slopes[jj];
                int numSlopes = 0;
                Point maxLinePoint = null;
                for (int jjj = jj + 1; jjj < rightSize; jjj++) {
                    if (candidateSlope == slopes[jjj]) {
                        numSlopes++;
                        if (maxLinePoint == null || points[jjj].compareTo(maxLinePoint) > 0) {
                            maxLinePoint = points[jjj];
                        }
                    }
                    if (numSlopes == 2) {
                        lineSegments.add(new LineSegment(points[jj], maxLinePoint));
                    }
                }
            }
        }
    }

    public int numberOfSegments() {
        return lineSegments.size();
    }

    public LineSegment[] segments() {
        LineSegment[] s = new LineSegment[lineSegments.size()];
        lineSegments.toArray(s);
        return s;
    }

    public static void main(String[] args) {
        Point[] ps = new Point[]{new Point(0, 0), new Point(1, 1), new Point(-2, -2), new Point(5, 5)};
        BruteCollinearPoints bcp = new BruteCollinearPoints(ps);

        System.out.println(bcp.numberOfSegments());
    }

}
