import com.formdev.flatlaf.FlatLightLaf;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDayChooser;
import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.sql.*;
import java.util.*;

public class CalendarWithDB extends JFrame {
    private JCalendar calendar;

    public CalendarWithDB() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf()); // ✅ FlatLaf من البداية
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("📅 Calendar with events");
        setSize(500, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        calendar = new JCalendar();

        // تحسين خط العنوان (شهر وسنة)
        Font titleFont = new Font("SansSerif", Font.BOLD, 18);
        ((JMonthChooser) calendar.getMonthChooser()).getComboBox().setFont(titleFont);
        ((JYearChooser) calendar.getYearChooser()).getSpinner().setFont(titleFont);

        // تحسين لون أيام الأسبوع
        calendar.getDayChooser().setSundayForeground(new Color(220, 20, 60)); // أحمر مسطح
        calendar.getDayChooser().setWeekdayForeground(new Color(33, 150, 243)); // أزرق مسطح
        calendar.getDayChooser().getDayPanel().setBackground(new Color(245, 245, 245)); // خلفية خفيفة

        getContentPane().setBackground(new Color(250, 250, 250)); // خلفية النافذة

        getContentPane().add(calendar, BorderLayout.CENTER);

        JButton highlightButton = new JButton("Load Events ");
        highlightButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        highlightButton.setBackground(new Color(0, 153, 76));
        highlightButton.setForeground(Color.WHITE);
        highlightButton.addActionListener(e -> highlightEventDates());

        getContentPane().add(highlightButton, BorderLayout.SOUTH);

        // ✅ من البداية نلون اليوم الحالي بشكل Flat
        markDates(Collections.emptyList());
    }

    private void highlightEventDates() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/events_db?useSSL=false&serverTimezone=UTC", 
                "root", 
                ""
            );

            String query = "SELECT date FROM events";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            List<Calendar> eventDates = new ArrayList<>();
            while (rs.next()) {
                java.sql.Date sqlDate = rs.getDate("date");
                Calendar cal = Calendar.getInstance();
                cal.setTime(sqlDate);
                eventDates.add(cal);
            }

            rs.close();
            ps.close();
            conn.close();

            markDates(eventDates);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void markDates(List<Calendar> eventDates) {
        JDayChooser dayChooser = calendar.getDayChooser();
        Component[] buttons = dayChooser.getDayPanel().getComponents();

        Calendar currentCal = calendar.getCalendar();
        int currentMonth = currentCal.get(Calendar.MONTH);
        int currentYear = currentCal.get(Calendar.YEAR);
        int today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int todayMonth = Calendar.getInstance().get(Calendar.MONTH);
        int todayYear = Calendar.getInstance().get(Calendar.YEAR);

        for (Component c : buttons) {
            if (c instanceof JButton) {
                JButton dayButton = (JButton) c;
                try {
                    int day = Integer.parseInt(dayButton.getText());

                    // ارجع اللون الأساسي أولاً
                    dayButton.setBackground(new Color(245, 245, 245));
                    dayButton.setOpaque(true);
                    dayButton.setBorderPainted(false);
                    dayButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
                    dayButton.setForeground(Color.DARK_GRAY);

                    // اليوم الحالي ✅
                    if (day == today && currentMonth == todayMonth && currentYear == todayYear) {
                        dayButton.setBackground(new Color(33, 150, 243)); // أزرق فلات
                        dayButton.setForeground(Color.WHITE);
                        dayButton.setFont(new Font("SansSerif", Font.BOLD, 14));
                    }

                    // لوّن الأيام اللي فيها أحداث ✅
                    for (Calendar eventDate : eventDates) {
                        int eventDay = eventDate.get(Calendar.DAY_OF_MONTH);
                        int eventMonth = eventDate.get(Calendar.MONTH);
                        int eventYear = eventDate.get(Calendar.YEAR);

                        if (eventDay == day && eventMonth == currentMonth && eventYear == currentYear) {
                            dayButton.setBackground(new Color(244, 67, 54)); // أحمر فلات
                            dayButton.setForeground(Color.WHITE);
                            dayButton.setFont(new Font("SansSerif", Font.BOLD, 14));
                        }
                    }

                } catch (NumberFormatException ex) {
                    // تجاهل الخانات الفارغة
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CalendarWithDB frame = new CalendarWithDB();
            frame.setVisible(true);
        });
    }
}
