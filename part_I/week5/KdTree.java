package part_I.week5;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class KdTree {
    private class KdNode {
        private double x, y;
        private Point2D value;
        private KdNode left, right;
        private RectHV rect;
        private boolean vertical;

        public KdNode(double x, double y, Point2D value, KdNode parent) {
            this.x = x;
            this.y = y;
            this.value = value;
            this.left = null;
            this.right = null;
            if (parent == null) {
                this.rect = new RectHV(0, 0, 1, 1); // root node
                this.vertical = true;
            } else {
                this.vertical = !parent.vertical;
                if (vertical) {
                    if (y < parent.y) // left
                        this.rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.rect.xmax(), parent.y);
                    else if (y > parent.y) // right
                        this.rect = new RectHV(parent.rect.xmin(), parent.y, parent.rect.xmax(), parent.rect.ymax());
                    else if (x < parent.x) // left
                        this.rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.rect.xmax(), parent.y);
                    else if (x > parent.x) // right
                        this.rect = new RectHV(parent.rect.xmin(), parent.y, parent.rect.xmax(), parent.rect.ymax());
                } else {
                    if (x < parent.x) // left
                        this.rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.x, parent.rect.ymax());
                    else if (x > parent.x) // right
                        this.rect = new RectHV(parent.x, parent.rect.ymin(), parent.rect.xmax(), parent.rect.ymax());
                    else if (y < parent.y) // left
                        this.rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.x, parent.rect.ymax());
                    else if (y > parent.y) // right
                        this.rect = new RectHV(parent.x, parent.rect.ymin(), parent.rect.xmax(), parent.rect.ymax());
                }
            }
        }
    }

    private KdNode root;
    private int n;

    public KdTree() {
        root = null;
        n = 0;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return n;
    }

    private void handleNullArg() {
        throw new IllegalArgumentException("Argument is null");
    }

    public void insert(Point2D p) {
        if (p == null) handleNullArg();
        root = insert(root, null, p.x(), p.y(), p);
        n++;
    }

    private KdNode insert(KdNode node, KdNode parent, Double x, Double y, Point2D p) {
        if (node == null) return new KdNode(p.x(), p.y(), p, parent);
        int cmp = 0;
        if (node.vertical) {
            cmp = x.compareTo(node.x);
        } else {
            cmp = y.compareTo(node.y);
        }
        if (cmp < 0) {
            node.left = insert(node.left, node, x, y, p);
        } else if (cmp > 0) {
            node.right = insert(node.right, node, x, y, p);
        } else if (x == node.x && y == node.y) {
            node.value = p;
            this.n--; // points already in tree
        } else { // first key is equal
            if (x < node.x || y < node.y)
                node.left = insert(node.left, node, x, y, p);
            else
                node.right = insert(node.right, node, x, y, p);
        }
        return node;
    }

    public boolean contains(Point2D p) {
        if (p == null) handleNullArg();
        return contains(root, p);
    }

    private boolean contains(KdNode node, Point2D p) {
        if (node == null) return false;
        if (node.value.equals(p)) return true;
        if (node.vertical) {
            if (p.x() < node.x) return contains(node.left, p);
            else if (p.x() > node.x) return contains(node.right, p);
            else return contains(node.left, p) | contains(node.right, p);
        } else {
            if (p.y() < node.y) return contains(node.left, p);
            else if (p.y() > node.y) return contains(node.right, p);
            else return contains(node.left, p) | contains(node.right, p);
        }
    }

    public void draw() {
        draw(root);
    }

    private void draw(KdNode node) {
        if (node == null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledCircle(node.x, node.y, .01);
        if (node.vertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.x, node.rect.ymin(), node.x, node.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.rect.xmin(), node.y, node.rect.xmax(), node.y);
        }
        draw(node.left);
        draw(node.right);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) handleNullArg();
        return range(rect, root);
    }

    private ArrayList<Point2D> range(RectHV rect, KdNode node) {
        if (node == null) return new ArrayList<Point2D>(0);
        ArrayList<Point2D> points = new ArrayList<>();
        if (rect.contains(node.value)) points.add(node.value);
        // check if point in node lies in given rectangle
        if (node.vertical) {
            if (node.x > rect.xmax()) points.addAll(range(rect, node.left));
            else if (node.x < rect.xmin()) points.addAll(range(rect, node.right));
            else { // vertical line intersect rect
                points.addAll(range(rect, node.left));
                points.addAll(range(rect, node.right));
            }
        } else {
            if (node.y > rect.ymax()) points.addAll(range(rect, node.left));
            else if (node.y < rect.ymin()) points.addAll(range(rect, node.right));
            else { // horizontal line intersect rect
                points.addAll(range(rect, node.left));
                points.addAll(range(rect, node.right));
            }
        }
        return points;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) handleNullArg();
        if (root == null) return null;
        return nearest(p, root, root.value);
    }

    private Point2D nearest(Point2D p, KdNode node, Point2D closest) {
        if (node == null) return closest;
        if (node.rect.distanceSquaredTo(p) > p.distanceSquaredTo(closest)) return closest;
        // look at SAME side of the query point p first
        closest = p.distanceSquaredTo(node.value) < p.distanceSquaredTo(closest) ? node.value : closest;
        boolean checkLeftFist = true;
        if (node.vertical && p.x() > node.x) checkLeftFist = false;
        else if (!node.vertical && p.y() > node.y) checkLeftFist = false;
        KdNode firstNode = node.left;
        KdNode secondNode = node.right;
        if (!checkLeftFist) {
            firstNode = node.right;
            secondNode = node.left;
        }
        Point2D subClosest = nearest(p, firstNode, closest);
        subClosest = nearest(p, secondNode, subClosest);
        return p.distanceSquaredTo(subClosest) < p.distanceSquaredTo(closest) ? subClosest : closest;
    }

    public static void main(String[] args) {
        KdTree kdtree = new KdTree();
        System.out.println("isEmpty at start: " + kdtree.isEmpty());
        kdtree.insert(new Point2D(0.75, 0.25));
        kdtree.insert(new Point2D(0.50000, 0.875));
        kdtree.insert(new Point2D(0.5625, 0.125));
        kdtree.insert(new Point2D(0.625, 0.5));
        kdtree.insert(new Point2D(1.0, 0.1875));
        kdtree.insert(new Point2D(0.5, 0.5625));
        kdtree.insert(new Point2D(0.0625, 0.75));
        kdtree.insert(new Point2D(0.6875, 0.8125));
        kdtree.insert(new Point2D(0.625, 0.8125));
        kdtree.insert(new Point2D(1.0, 0.9375));
        kdtree.insert(new Point2D(0.6875, 0.125));
        kdtree.insert(new Point2D(0.1875, 0.0));
        kdtree.insert(new Point2D(0.625, 0.4375));
        kdtree.insert(new Point2D(0.5625, 0.875));
        kdtree.insert(new Point2D(0.625, 0.25));
        kdtree.insert(new Point2D(0.875, 0.125));
        kdtree.insert(new Point2D(0.0, 0.8125));
        kdtree.insert(new Point2D(0.125, 0.625));
        kdtree.insert(new Point2D(0.8125, 1.0));
        kdtree.insert(new Point2D(0.6875, 0.5625));
        System.out.println("size after inserting 20 points: " + kdtree.size());
//        kdtree.draw();
        System.out.println("contains Point2D(0.625, 0.4375): " + kdtree.contains(new Point2D(0.625, 0.4375)));
        System.out.println("Point in RectHV(.1, .1, .95, .5): ");
        for (Point2D p : kdtree.range(new RectHV(0.1, 0.1, 0.95, 0.5))) {
            System.out.println(p);
        }

        Point2D queryPoint = new Point2D(0.25, 0.9375);
        Point2D nearest = kdtree.nearest(queryPoint);
        StdOut.println("Nearest point to Point2D = " + queryPoint + ": " + nearest +
                " | distanceTo: " + queryPoint.distanceSquaredTo(nearest));
        StdOut.println("reference nearest dist: " + queryPoint.distanceSquaredTo(new Point2D(0.5, 0.875)));

        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.filledCircle(queryPoint.x(), queryPoint.y(), 0.01);
        StdDraw.setPenColor(StdDraw.CYAN);
        StdDraw.filledCircle(nearest.x(), nearest.y(), 0.01);
    }
}
