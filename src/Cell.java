
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JLabel;

public class Cell extends JButton {

    private Date date;
    private boolean title;
    private boolean isToDay;
    private Color customBackgroundColor = null;  // لون الخلفية المخصص
    private boolean isSpecialDate = false;
    


    public Cell() {
        setContentAreaFilled(false); // نمنع JButton من رسم الخلفية تلقائياً
        setBorder(null);
        setHorizontalAlignment(JLabel.CENTER);
    }

    public void asTitle() {
        title = true;
    }

    public boolean isTitle() {
        return title;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void currentMonth(boolean act) {
        if (act) {
            setForeground(new Color(68, 68, 68));
        } else {
            setForeground(new Color(169, 169, 169));
        }
    }

    public void setAsToDay() {
        isToDay = true;
        setForeground(Color.WHITE);
    }

    public void setBackgroundColor(Color color) {
        this.customBackgroundColor = color;
        repaint(); // نعيد رسم الزر بعد تغيير اللون
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // رسم الخلفية إذا كان فيه لون مخصص
        if (customBackgroundColor != null) {
            g2.setColor(customBackgroundColor);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        // رسم خط أسفل الخلايا التي تمثل عنوان الأسبوع (السبت - الأحد - ...)
        if (title) {
            g2.setColor(new Color(213, 213, 213));
            g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
        }

        // تمييز اليوم الحالي بدائرة ملونة
        if (isToDay) {
            g2.setColor(new Color(97, 49, 237)); // لون بنفسجي
            int x = getWidth() / 2 - 17;
            int y = getHeight() / 2 - 17;
            g2.fillRoundRect(x, y, 35, 35, 100, 100);
        }

        if (isSpecialDate) {
          g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
          g2.setColor(new Color(255, 204, 102)); // لون برتقالي فاتح
          int x = getWidth() / 2 - 17;
          int y = getHeight() / 2 - 17;
          g2.fillRoundRect(x, y, 35, 35, 100, 100);
      }
      

        super.paintComponent(grphcs);
    }

    public void setAsSpecialDate() {
      isSpecialDate = true;
  }
  
}
