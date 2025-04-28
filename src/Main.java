import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        //exc 1 tests
        test(new Point(200, 300), new Point(300, 100), new Point(100, 100), "triangle1.png");

        test(new Point(100, 300), new Point(300, 100), new Point(100, 100), "triangle2.png");

        test(new Point(100, 100), new Point(300, 300), new Point(100, 300), "triangle3.png");

        test(new Point(100, 100), new Point(100, 300), new Point(300, 200), "triangle4.png");

        test(new Point(100, 100), new Point(100, 300), new Point(300, 350), "triangle5.png");

        randomTest(10, "triangles1.png");
        randomTest(1000, "triangles2.png");
    }

    static void test(Point p1, Point p2, Point p3, String filename)
    {
        int width = 400;
        int height = 400;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Triangle triangle = new Triangle(p1, p2, p3, Color.RED, Color.GREEN, Color.BLUE);

        triangle.shade(img);

        JFrame frame = new JFrame("Shaded Triangle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                triangle.shade(g2d);
            }
        };

        panel.setPreferredSize(new Dimension(500, 500));
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        try {
            File outputFile = new File(filename);
            ImageIO.write(img, "png", outputFile);
            System.out.println("Image saved as " + filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void randomTest(int n, String filename) {
        int width = 500;
        int height = 500;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Random rand = new Random();
        ArrayList<Triangle> triangles = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            Point p1 = new Point(rand.nextInt(width), rand.nextInt(height));
            Point p2 = new Point(rand.nextInt(width), rand.nextInt(height));
            Point p3 = new Point(rand.nextInt(width), rand.nextInt(height));
            Color c1 = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
            Color c2 = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
            Color c3 = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
            triangles.add(new Triangle(p1, p2, p3, c1, c2, c3));
        }

        long startImage = System.currentTimeMillis();
        Graphics2D g2dImg = img.createGraphics();
        for (Triangle triangle : triangles) {
            triangle.shade(g2dImg);
        }
        g2dImg.dispose();
        long endImage = System.currentTimeMillis();
        System.out.println("Time to draw on BufferedImage: " + (endImage - startImage) + " ms");

        try {
            File outputFile = new File(filename);
            ImageIO.write(img, "png", outputFile);
            System.out.println("Image saved as " + filename);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Random Triangles");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                long startPanel = System.currentTimeMillis();
                for (Triangle triangle : triangles) {
                    triangle.shade(g2d);
                }
                long endPanel = System.currentTimeMillis();
                System.out.println("Time to draw on JPanel: " + (endPanel - startPanel) + " ms");
            }
        };

        panel.setPreferredSize(new Dimension(width, height));
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
