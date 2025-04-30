import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.util.Calendar;

import com.mysql.cj.xdevapi.Statement;
import com.toedter.calendar.JDayChooser;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

public class CustomDayChooser extends JDayChooser {

    private static final Logger LOGGER = Logger.getLogger(CustomDayChooser.class.getName());

    private Set<LocalDate> eventDates = new HashSet<>();
    private JButton[] dayButtons;
    private JButton colorizeButton; // تعريف الزر


    

    public CustomDayChooser() {
        super();
        LOGGER.info("CustomDayChooser initialized");

        // نحاول نحصل على أزرار الأيام
        Component[] components = getDayPanel().getComponents();
        List<JButton> buttons = new ArrayList<>();

        for (Component c : components) {
            if (c instanceof JButton) {
                buttons.add((JButton) c);
            }
        }

        dayButtons = buttons.toArray(new JButton[0]);

        colorizeButton = new JButton("Show Events with Colors");
        colorizeButton.addActionListener(e -> colorizeCalendar());
    }

    public void setEventDates(Set<LocalDate> dates) {
        this.eventDates = dates;
        LOGGER.info("Event dates set: " + dates);
        repaint();
    }

    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      highlightEventDays();
  }

  private void colorizeCalendar() {
        // إضافة الألوان بناءً على الفعاليات من قاعدة البيانات
        try {
            // الاتصال بقاعدة البيانات
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/events_db?useSSL=false&serverTimezone=UTC", "root", "");
            String query = "SELECT date, name FROM events";
            Statement stmt = (Statement) conn.createStatement();
            ResultSet rs = ((java.sql.Statement) stmt).executeQuery(query);

            // تلوين الأيام في التقويم بناءً على البيانات من قاعدة البيانات
            while (rs.next()) {
                Date eventDate = rs.getDate("date");
                String eventName = rs.getString("name");

                // تحديد اليوم الذي يحتوي على الفعالية
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(eventDate);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // تحديد اللون (يمكنك تخصيصه حسب نوع الفعالية)
                Color eventColor = Color.RED; // مثال: اختر اللون الأحمر لكل الأحداث
                Calendar2 calendarCustom2 = new Calendar2(); // التقويم
                calendarCustom2.setDayBackground(day, eventColor); // تعيين اللون على اليوم المحدد في التقويم
            }

            conn.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

  private void highlightEventDays() {
    LocalDate now = LocalDate.now();

    for (JButton btn : dayButtons) {
        try {
            int day = Integer.parseInt(btn.getText());
            LocalDate date = LocalDate.of(now.getYear(), now.getMonthValue(), day);

            if (eventDates.contains(date)) {
                btn.setBackground(Color.PINK);
            } else {
                btn.setBackground(Color.WHITE);
            }
        } catch (NumberFormatException ignored) {
            btn.setBackground(Color.LIGHT_GRAY); // ممكن تكون زر فارغ
        }
    }
}


    public Set<LocalDate> getEventDates() {
        return eventDates;
        
    }

    public void addEventDate(LocalDate date) {
        eventDates.add(date);
        repaint();
    }

    public JButton[] getDayPanelButtons() {
        return this.dayButtons;
    }

    // تعيين لون الخلفية لليوم المحدد
    public void setDayBackground(int day, Color color) {
      if (dayButtons == null || dayButtons.length == 0) return;
  
      for (JButton btn : dayButtons) {
          try {
              int btnDay = Integer.parseInt(btn.getText());
              if (btnDay == day) {
                  btn.setOpaque(true);
                  btn.setBackground(color);
                  btn.repaint();
              }
          } catch (NumberFormatException ignored) {
              // تجاهل الأزرار التي لا تحتوي على أرقام
          }
      }
  }
    
}
