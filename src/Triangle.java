import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Comparator;

public class Triangle {
    private final Point p1, p2, p3;
    private final Color c1, c2, c3;

    public Triangle(Point p1, Point p2, Point p3, Color c1, Color c2, Color c3) {
        Object[][] pointsWithColors = {
                {p1, c1},
                {p2, c2},
                {p3, c3}
        };

        Arrays.sort(pointsWithColors, Comparator.comparingInt(a -> ((Point) a[0]).y));

        this.p1 = (Point) pointsWithColors[0][0];
        this.c1 = (Color) pointsWithColors[0][1];
        this.p2 = (Point) pointsWithColors[1][0];
        this.c2 = (Color) pointsWithColors[1][1];
        this.p3 = (Point) pointsWithColors[2][0];
        this.c3 = (Color) pointsWithColors[2][1];
    }

    public void shade(BufferedImage img) {
        for (int y = p1.y; y <= p2.y; y++) {
            float beta1 = (y - p1.y) / (float) (p2.y - p1.y);
            float beta2 = (y - p1.y) / (float) (p3.y - p1.y);

            int x_l = interpolateCoordinate(p2.x, p1.x, beta1);
            Color c_l = interpolateColor(c2, c1, beta1);

            int x_r = interpolateCoordinate(p3.x, p1.x, beta2);
            Color c_r = interpolateColor(c3, c1, beta2);

            shadeHorizontalLine(img, y, x_l, c_l, x_r, c_r);
        }

        for (int y = p2.y + 1; y <= p3.y; y++) {
            float beta1 = (y - p2.y) / (float) (p3.y - p2.y);
            float beta2 = (y - p1.y) / (float) (p3.y - p1.y);

            int x_l = interpolateCoordinate(p3.x, p2.x, beta1);
            Color c_l = interpolateColor(c3, c2, beta1);

            int x_r = interpolateCoordinate(p3.x, p1.x, beta2);
            Color c_r = interpolateColor(c3, c1, beta2);

            shadeHorizontalLine(img, y, x_l, c_l, x_r, c_r);
        }
    }

    public void shade(Graphics2D g2d) {
        for (int y = p1.y; y < p2.y; y++) {
            float beta1 = (y - p1.y) / (float) (p2.y - p1.y);
            float beta2 = (y - p1.y) / (float) (p3.y - p1.y);

            int x_l = interpolateCoordinate(p2.x, p1.x, beta1);
            Color c_l = interpolateColor(c2, c1, beta1);

            int x_r = interpolateCoordinate(p3.x, p1.x, beta2);
            Color c_r = interpolateColor(c3, c1, beta2);

            shadeHorizontalLine(g2d, y, x_l, c_l, x_r, c_r);
        }

        for (int y = p2.y; y < p3.y; y++) {
            float beta1 = (y - p2.y) / (float) (p3.y - p2.y);
            float beta2 = (y - p1.y) / (float) (p3.y - p1.y);

            int x_l = interpolateCoordinate(p3.x, p2.x, beta1);
            Color c_l = interpolateColor(c3, c2, beta1);

            int x_r = interpolateCoordinate(p3.x, p1.x, beta2);
            Color c_r = interpolateColor(c3, c1, beta2);

            shadeHorizontalLine(g2d, y, x_l, c_l, x_r, c_r);
        }
    }

    private void shadeHorizontalLine(BufferedImage img, int y, int x_l, Color c_l, int x_r, Color c_r) {
        if (x_l > x_r) {
            int tempX = x_l; x_l = x_r; x_r = tempX;
            Color tempC = c_l; c_l = c_r; c_r = tempC;
        }

        float delta_r = (c_r.getRed() - c_l.getRed()) / (float) (x_r - x_l);
        float delta_g = (c_r.getGreen() - c_l.getGreen()) / (float) (x_r - x_l);
        float delta_b = (c_r.getBlue() - c_l.getBlue()) / (float) (x_r - x_l);

        float curr_r = c_l.getRed();
        float curr_g = c_l.getGreen();
        float curr_b = c_l.getBlue();

        for (int x = x_l; x < x_r; x++) {
            int r = clamp(curr_r);
            int g = clamp(curr_g);
            int b = clamp(curr_b);
            Color color = new Color(r, g, b);
            img.setRGB(x, y, color.getRGB());

            curr_r += delta_r;
            curr_g += delta_g;
            curr_b += delta_b;
        }
    }

    private void shadeHorizontalLine(Graphics2D g2d, int y, int x_l, Color c_l, int x_r, Color c_r) {
        if (x_l > x_r) {
            int tempX = x_l; x_l = x_r; x_r = tempX;
            Color tempC = c_l; c_l = c_r; c_r = tempC;
        }

        float delta_r = (c_r.getRed() - c_l.getRed()) / (float) (x_r - x_l);
        float delta_g = (c_r.getGreen() - c_l.getGreen()) / (float) (x_r - x_l);
        float delta_b = (c_r.getBlue() - c_l.getBlue()) / (float) (x_r - x_l);

        float curr_r = c_l.getRed();
        float curr_g = c_l.getGreen();
        float curr_b = c_l.getBlue();

        for (int x = x_l; x <= x_r; x++) {
            int r = clamp(Math.round(curr_r));
            int g = clamp(Math.round(curr_g));
            int b = clamp(Math.round(curr_b));

            g2d.setColor(new Color(r, g, b));
            g2d.fillRect(x, y, 1, 1);

            curr_r += delta_r;
            curr_g += delta_g;
            curr_b += delta_b;
        }
    }

    private int clamp(float x) {
        if(x < 0) return 0;
        if(x > 255) return 255;
        return (int)x;
    }

    private int interpolateCoordinate(int x1, int x2, float beta_y) {
        return (int)(beta_y * x1 + (1-beta_y) * x2);
    }

    private Color interpolateColor(Color col1, Color col2, float beta_y) {
        int r = (int)(col2.getRed() * (1 - beta_y) + col1.getRed() * beta_y);
        int g = (int)(col2.getGreen() * (1 - beta_y) + col1.getGreen() * beta_y);
        int b = (int)(col2.getBlue() * (1 - beta_y) + col1.getBlue() * beta_y);
        return new Color(r, g, b);
    }
}
