package utils;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ColorUtils {
    public static Color getAvgColor(BufferedImage image)
    {
        int height = image.getHeight();
        int width = image.getWidth();
        int r = 0;
        int g = 0;
        int b = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Color c = new Color(image.getRGB(i,j));
                r += c.getRed();
                g += c.getGreen();
                b += c.getBlue();
            }
        }
        int a = (height*width);
        return new Color(r /a, g/a, b/a);
    }
    public static Color getDominateColor(BufferedImage image) {
        Map<Integer, Integer> colorMap = new HashMap<>();
        int height = image.getHeight();
        int width = image.getWidth();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = image.getRGB(i, j);
                if (!isGray(getRGBArr(rgb))) {
                    Integer counter = colorMap.get(rgb);
                    if (counter == null) {
                        counter = 0;
                    }

                    colorMap.put(rgb, ++counter);
                }
            }
        }
        String colorStr = null;
        try {
            colorStr = getMostCommonColor(colorMap);
            return new Color(
                    Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
                    Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
                    Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Color.white;
    }
    private static int[] getRGBArr(int pixel) {
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;

        return new int[]{red, green, blue};
    }

    private static boolean isGray(int[] rgbArr) {
        int rgDiff = rgbArr[0] - rgbArr[1];
        int rbDiff = rgbArr[0] - rgbArr[2];
        // Filter out black, white and grays...... (tolerance within 10 pixels)
        int tolerance = 10;
        if (rgDiff > tolerance || rgDiff < -tolerance) {
            if (rbDiff > tolerance || rbDiff < -tolerance) {
                return false;
            }
        };
        return true;
    }
    private static String getMostCommonColor(Map<Integer, Integer> map) throws Exception {
        java.util.List<Map.Entry<Integer, Integer>> list = new LinkedList<>(map.entrySet());

        Collections.sort(list, (Map.Entry<Integer, Integer> obj1, Map.Entry<Integer, Integer> obj2)
                -> ((Comparable) obj1.getValue()).compareTo(obj2.getValue()));

        Map.Entry<Integer, Integer> entry = list.get(list.size() - 1);

        int[] rgb = getRGBArr(entry.getKey());

        return "#" + Integer.toHexString(rgb[0])
                + Integer.toHexString(rgb[1])
                + Integer.toHexString(rgb[2]);
    }


}

