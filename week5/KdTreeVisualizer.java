/******************************************************************************
 *  Compilation:  javac KdTreeVisualizer.java
 *  Execution:    java KdTreeVisualizer
 *  Dependencies: KdTree.java
 *
 *  Add the points that the user clicks in the standard draw window
 *  to a kd-tree and draw the resulting kd-tree.
 *
 ******************************************************************************/

package week5;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.concurrent.TimeUnit;

public class KdTreeVisualizer {

    public static void main(String[] args) throws InterruptedException {
        RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
        StdDraw.enableDoubleBuffering();
        KdTree kdtree = new KdTree();
        StdOut.println("insert 0, isEmpty: " + kdtree.isEmpty() + " size: " + kdtree.size());
        kdtree.insert(new Point2D(.7, .2));
        StdOut.println("insert 1, isEmpty: " + kdtree.isEmpty() + " size: " + kdtree.size());
        kdtree.insert(new Point2D(.5, .4));
        StdOut.println("insert 2, isEmpty: " + kdtree.isEmpty() + " size: " + kdtree.size());
        kdtree.insert(new Point2D(.2, .3));
        StdOut.println("insert 3, isEmpty: " + kdtree.isEmpty() + " size: " + kdtree.size());
        kdtree.insert(new Point2D(.4, .7));
        StdOut.println("insert 4, isEmpty: " + kdtree.isEmpty() + " size: " + kdtree.size());
        kdtree.insert(new Point2D(.9, .6));
        StdOut.println("insert 5, isEmpty: " + kdtree.isEmpty() + " size: " + kdtree.size());
        while (true) {
            if (StdDraw.isMousePressed()) {
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                StdOut.printf("%8.6f %8.6f\n", x, y);
                Point2D p = new Point2D(x, y);
                StdOut.printf("Adding point: " + p);
                Point2D nearest = kdtree.nearest(p);
                StdOut.println(" | nearest: " + nearest);
                if (rect.contains(p)) {
                    StdOut.printf("%8.6f %8.6f\n", x, y);
                    kdtree.insert(p);
                    if (!kdtree.contains(p)) {
                        throw new RuntimeException("Contains failed!!!");
                    }
                    StdDraw.clear();
                    kdtree.draw();
                    StdDraw.setPenColor(StdDraw.CYAN);
                    StdDraw.filledCircle(nearest.x(), nearest.y(), 0.01);
                    StdDraw.show();
                }
                TimeUnit.MILLISECONDS.sleep(300);
            }
            StdDraw.pause(20);
        }

    }
}
