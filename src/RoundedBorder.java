 

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;

public class RoundedBorder extends AbstractBorder {
  private int radius;
  public RoundedBorder(int radius) {
    this.radius = radius;
}

@Override
public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setColor(Color.GRAY);
    g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    g2.dispose();
}

@Override
public Insets getBorderInsets(Component c) {
    return new Insets(radius + 1, radius + 1, radius + 1, radius + 1);
}

@Override
public Insets getBorderInsets(Component c, Insets insets) {
    insets.left = insets.top = insets.right = insets.bottom = radius + 1;
    return insets;
}
}
