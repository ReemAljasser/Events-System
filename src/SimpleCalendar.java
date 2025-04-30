import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDayChooser;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;
import java.util.List;
import java.util.Date;

public class SimpleCalendar extends JFrame {
    private JCalendar calendar;
    private List<Date> eventDates = new ArrayList<>();

    public SimpleCalendar() {
        initComponents();
        retrieveEventDatesFromDB(); // استدعاء البيانات
        markEventDates();           // حط العلامات
    }

    private void initComponents() {
        setTitle("Custom Calendar");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JCalendar calendar = new JCalendar();
        getContentPane().add(calendar, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        getContentPane().add(closeButton, BorderLayout.SOUTH);
    }

    private void retrieveEventDatesFromDB() {
        String url = "jdbc:mysql://localhost:3306/events_db?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = ""; // غيّريها حسب الإعدادات عندك

        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT date FROM events";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
              eventDates.add(new java.util.Date(rs.getDate("date").getTime()));

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void markEventDates() {
      JDayChooser dayChooser = calendar.getDayChooser();

        for (Date date : eventDates) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);

            Calendar selectedMonth = calendar.getCalendar();            
            if (month == selectedMonth.get(Calendar.MONTH) && year == selectedMonth.get(Calendar.YEAR)) {
                JButton dayButton = dayChooser.getDayPanel().getComponent(day - 1) instanceof JButton
                        ? (JButton) dayChooser.getDayPanel().getComponent(day - 1) : null;
                if (dayButton != null) {
                    dayButton.setBackground(Color.ORANGE); // لون مميز
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SimpleCalendar().setVisible(true);
        });
    }
}
