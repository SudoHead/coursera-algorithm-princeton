package part_II.week2;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    private Picture pic;
    private int[][] picRGB;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException("Picture cannot be null.");
        pic = new Picture(picture);
        picRGB = new int[pic.width()][pic.height()];
        for (int x = 0; x < pic.width(); x++) {
            for (int y = 0; y < pic.height(); y++) {
                picRGB[x][y] = pic.getRGB(x, y);
            }
        }
    }

    // current picture
    public Picture picture() {
        return new Picture(pic);
    }

    // width of current picture
    public int width() {
        return pic.width();
    }

    // height of current picture
    public int height() {
        return pic.height();
    }

    private void checkInput(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height())
            throw new IllegalArgumentException("Input coordinates out of bound!");
    }

    private int extractSubRGB(int rgb, int rgbChannel) {
        // The red (R), green (G), and blue (B) components are encoded using the least significant 24 bits.
        switch (rgbChannel) {
            case 0: // RED
                return (rgb >> 16) & 0xFF;
            case 1: // GREEN
                return (rgb >> 8) & 0xFF;
            case 2: // BLUE
                return (rgb >> 0) & 0xFF;
            default:
                throw new IllegalArgumentException("rgbChannel must be 0, 1 or 2");
        }
    }

    private int centralXDifference(int x, int y, int rgbChannel) {
        int d = extractSubRGB(picRGB[x + 1][y], rgbChannel) - extractSubRGB(picRGB[x - 1][y], rgbChannel);
        return Math.abs(d);
    }

    private int centralYDifference(int x, int y, int rgbChannel) {
        int d = extractSubRGB(picRGB[x][y + 1], rgbChannel) - extractSubRGB(picRGB[x][y - 1], rgbChannel);
        return Math.abs(d);
    }

    private double xGradient(int x, int y) {
        int rx = centralXDifference(x, y, 0);
        int gx = centralXDifference(x, y, 1);
        int bx = centralXDifference(x, y, 2);
        return rx * rx + gx * gx + bx * bx;
    }

    private double yGradient(int x, int y) {
        int ry = centralYDifference(x, y, 0);
        int gy = centralYDifference(x, y, 1);
        int by = centralYDifference(x, y, 2);
        return ry * ry + gy * gy + by * by;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        checkInput(x, y);
        if (x == 0 || x == pic.width() - 1 || y == 0 || y == pic.height() - 1)
            return 1000; // border energy is set to be strictly larger than interior pixels.
        return Math.sqrt(xGradient(x, y) + yGradient(x, y));
    }

    private int mapV(int x, int y) {
        return x + y * width();
    }

    private GridDAG buildGridDAG(boolean vertical) {
        double[] energies = new double[height() * width()];
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                energies[mapV(x, y)] = this.energy(x, y);
            }
        }
        GridDAG g = new GridDAG(width(), height(), energies);
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                int v = mapV(x, y);
                if (vertical) {
                    if (y == 0 && x > 0 && x % 2 == 0) continue; // overlapping border edges
                    if (y == 0) // add edge from virtual vertices
                        g.addEdge(g.getVirtualFrontVertex(), v);
                    if (y + 1 >= height()) {
                        g.addEdge(v, g.getVirtualBackVertex());
                        continue;
                    }
                    // add edges to the 3 downward neighbors
                    int n0 = mapV(x - 1, y + 1);
                    int n1 = mapV(x, y + 1);
                    int n2 = mapV(x + 1, y + 1);
                    g.addEdge(v, n1);
                    if (x > 0) g.addEdge(v, n0);
                    if (x < width() - 1) g.addEdge(v, n2);
                } else { // horizontal
                    if (x == 0 && y > 0 && y % 2 == 0) continue; // overlapping border edges
                    if (x == 0) // add edge from virtual vertices
                        g.addEdge(g.getVirtualFrontVertex(), v);
                    if (x + 1 >= width()) {
                        g.addEdge(v, g.getVirtualBackVertex());
                        continue;
                    }
                    // add edges to 3 rightward neighbors
                    if (x + 1 >= width()) continue;
                    int n0 = mapV(x + 1, y - 1);
                    int n1 = mapV(x + 1, y);
                    int n2 = mapV(x + 1, y + 1);
                    g.addEdge(v, n1);
                    if (y > 0) g.addEdge(v, n0);
                    if (y < height() - 1) g.addEdge(v, n2);
                }
            }
        }
        return g;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        GridDAG G = buildGridDAG(false);
        Seam seam = new Seam(G);
        int[] hSeam = new int[width()];
        int i = 0;
        for (int v : seam.getSeam())
            hSeam[i++] = v / width();
        return hSeam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        GridDAG G = buildGridDAG(true);
        Seam seam = new Seam(G);
        int[] vSeam = new int[height()];
        int i = 0;
        for (int v : seam.getSeam())
            vSeam[i++] = v % width();
        return vSeam;
    }

    private void checkSeamArg(int[] seam, boolean vertical) {
        if (seam == null || seam.length == 0 ||
                (vertical && seam.length != height()) || (!vertical && seam.length != width()))
            throw new IllegalArgumentException("Invalid seam arg.");
        int upperBound = vertical ? width() : height();
        int last = seam[0];
        for (int i : seam) {
            // successive entries in seam[] must differ by -1, 0, +1;
            if (i < 0 || i >= upperBound || Math.abs(i - last) > 1)
                throw new IllegalArgumentException("Invalid seam value.");
            last = i;
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        checkSeamArg(seam, false);
        if (height() <= 1)
            throw new IllegalArgumentException("Cannot call removeHorizontalSeam with height <= 1");
        Picture processedPic = new Picture(width(), height() - 1);
        picRGB = new int[processedPic.width()][processedPic.height()];
        for (int x = 0; x < processedPic.width(); x++) {
            for (int y = 0; y < processedPic.height(); y++) {
                int color;
                if (y >= seam[x])
                    color = pic.getRGB(x, y + 1);
                else
                    color = pic.getRGB(x, y);
                processedPic.setRGB(x, y, color);
                picRGB[x][y] = color;
            }
        }
        pic = processedPic;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        checkSeamArg(seam, true);
        if (width() <= 1)
            throw new IllegalArgumentException("Cannot call removeVerticalSeam with width <= 1");
        Picture processedPic = new Picture(width() - 1, height());
        for (int x = 0; x < processedPic.width(); x++) {
            for (int y = 0; y < processedPic.height(); y++) {
                int color;
                if (x >= seam[y])
                    color = pic.getRGB(x + 1, y);
                else
                    color = pic.getRGB(x, y);
                processedPic.setRGB(x, y, color);
                picRGB[x][y] = color;
            }
        }
        pic = processedPic;
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        if (args.length != 3)
            System.out.println("Usage - carve 300 vertical seams and 200 horizontal seams: test_image.png 300 200");
        String inputPic = args[0];
        int removeVertical = Integer.parseInt(args[1]);
        int removeHorizontal = Integer.parseInt(args[2]);

        SeamCarver sc = new SeamCarver(new Picture(inputPic));

        for (int y = 0; y < sc.height(); y++) {
            for (int x = 0; x < sc.width(); x++) {
                System.out.printf("%.2f\t\t", sc.energy(x, y));
            }
            System.out.println();
        }

        for (int i = 0; i < removeVertical; i++) {
            System.out.println("\n" + "VerticalSeam #" + i + " : ");
            int[] vSeam = sc.findVerticalSeam();
            for (int y = 0; y < sc.height(); y++) {
                System.out.print(vSeam[y] + ", ");
            }
            sc.removeVerticalSeam(vSeam);
        }

        for (int i = 0; i < removeHorizontal; i++) {
            System.out.println("\n" + "HorizontalSeam #" + i + " : ");
            int[] hSeam = sc.findHorizontalSeam();
            for (int x = 0; x < sc.width(); x++) {
                System.out.print(hSeam[x] + ", ");
            }
            sc.removeHorizontalSeam(hSeam);
        }
        sc.picture().save("test_carved.png");
    }

}
