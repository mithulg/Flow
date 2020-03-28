package utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

public class GUIUtils {
    public static AbstractButton makeButton(String title, ImageIcon icon) {
        return new JButton(title) {
            @Override public void updateUI() {
                super.updateUI();
                setVerticalAlignment(SwingConstants.CENTER);
                setVerticalTextPosition(SwingConstants.CENTER);
                setHorizontalAlignment(SwingConstants.CENTER);
                setHorizontalTextPosition(SwingConstants.CENTER);
                setBorder(BorderFactory.createEmptyBorder());
                //setBorderPainted(false);
                setContentAreaFilled(false);
                setFocusPainted(false);
                setOpaque(false);
                setForeground(Color.WHITE);
                setIcon(icon);
            }
        };
    }
}
