package custom;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Path2D;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder; 


public class teksfield extends JTextField{

        private Icon icon;
        
    public teksfield() {
        setOpaque(false);
        setBorder(new EmptyBorder(5, 35, 5, 10)); // ruang kiri buat icon
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
        setBorder(new EmptyBorder(5, icon.getIconWidth() + 15, 5, 10));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (icon != null) {
            int y = (getHeight() - icon.getIconHeight()) / 2;
            icon.paintIcon(this, g, 10, y);
        }
    }
}


