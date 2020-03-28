package utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class ImageUtils {
    private static BufferedImage getBlurFilter(BufferedImage sourceImage)
    {
        int blur = 1000;
        float[] matrix = new float[blur];
        for (int i = 0; i < blur; i++)
            matrix[i] = 1.0f/blur;
        BufferedImageOp op = new ConvolveOp( new Kernel(20, 20, matrix), ConvolveOp.EDGE_ZERO_FILL, null );
        //BoxBlurFilter filter = new BoxBlurFilter();
        BufferedImage blurredImage = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        return op.filter(sourceImage, blurredImage);
    }


    public static BufferedImage blurImage(BufferedImage sourceImage, int its) {
        BufferedImage blurredImage = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        blurredImage = getBlurFilter(sourceImage);
        for (int i = 0; i < its; i ++)
        {
            sourceImage = getBlurFilter(blurredImage);
        }
        return blurredImage;
    }
    public static ImageIcon resizeIcon(ImageIcon in, int width, int height)
    {
        Image img = in.getImage();
        Image newimg = img.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(newimg);
    }
}
