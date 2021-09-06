import java.util.Arrays;
import java.util.ArrayList;

public class FastCollinearPoints {

    private LineSegment[] segments;

    public FastCollinearPoints(Point[] points)
    {
        checkForNullPoints(points);
        Point[] pointsCopySO = Arrays.copyOf(points, points.length);
        Point[] pointsCopyNO = Arrays.copyOf(points, points.length);
        ArrayList<LineSegment> segmentsList = new ArrayList<LineSegment>();
        Arrays.sort(pointsCopyNO);
        checkForDuplicatedPoints(pointsCopyNO);
        for (int i = 0; i < pointsCopyNO.length; ++i)
        {
            Point origin = pointsCopyNO[i];
            Arrays.sort(pointsCopySO);
            Arrays.sort(pointsCopySO, origin.slopeOrder());
            int count = 1;
            Point lineBeginning = null;
            for (int j = 0; j < pointsCopySO.length - 1; ++j)
            {
                if (pointsCopySO[j].slopeTo(origin) == pointsCopySO[j + 1].slopeTo(origin))
                {
                    count++;
                    if (count == 2)
                    {
                        lineBeginning = pointsCopySO[j];
                        count++;
                    }
                    else if (count >= 4 && j + 1 == pointsCopySO.length - 1)
                    {
                        if (lineBeginning.compareTo(origin) > 0)
                        {
                            segmentsList.add(new LineSegment(origin, pointsCopySO[j + 1]));
                        }
                        count = 1;
                    }
                }
                else if (count >= 4)
                {
                    if (lineBeginning.compareTo(origin) > 0)
                    {
                        segmentsList.add(new LineSegment(origin, pointsCopySO[j]));
                    }
                    count = 1;
                }
                else
                {
                    count = 1;
                }


            }

        }
        segments = segmentsList.toArray(new LineSegment[segmentsList.size()]);


    }

    public int numberOfSegments() {
        return segments.length;
    }

    public LineSegment[] segments() {
        return Arrays.copyOf(segments, numberOfSegments());
    }

    private void checkForDuplicatedPoints(Point[] points)
    {
        for (int i = 0; i < points.length - 1; ++i)
        {
            if (points[i].compareTo(points[i+1]) == 0)
            {
                throw new java.lang.IllegalArgumentException("Duplicated points");
            }
        }
    }

    private void checkForNullPoints(Point[] points)
    {
        for (int i = 0; i < points.length; ++i)
        {
            if (points[i] == null)
            {
                throw new java.lang.NullPointerException("At least one point in array is null");
            }
        }
    }

    public static void main(String[] args) {
        Point[] ps = new Point[]{new Point(0, 0), new Point(1, 1), new Point(-2, -2), new Point(5, 5)};
        FastCollinearPoints bcp = new FastCollinearPoints(ps);

        System.out.println(bcp.numberOfSegments());
    }
}
