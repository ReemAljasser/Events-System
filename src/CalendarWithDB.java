import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDayChooser;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import java.sql.*;
import java.util.*;

public class CalendarWithDB extends JFrame {
    private JCalendar calendar;

    public CalendarWithDB() {
        setTitle("Calendar");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        calendar = new JCalendar();
        getContentPane().add(calendar, BorderLayout.CENTER);

        JButton highlightButton = new JButton("Load Events");
        highlightButton.addActionListener(e -> highlightEventDates());
        getContentPane().add(highlightButton, BorderLayout.SOUTH);
    }

    private void highlightEventDates() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/events_db?useSSL=false&serverTimezone=UTC", 
                "root", 
                "" // <-- عدليها حسب كلمة المرور عندك
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

        for (Component c : buttons) {
            if (c instanceof JButton) {
                JButton dayButton = (JButton) c;
                try {
                    int day = Integer.parseInt(dayButton.getText());

                    for (Calendar eventDate : eventDates) {
                        int eventDay = eventDate.get(Calendar.DAY_OF_MONTH);
                        int eventMonth = eventDate.get(Calendar.MONTH);
                        int eventYear = eventDate.get(Calendar.YEAR);

                        if (eventDay == day && eventMonth == currentMonth && eventYear == currentYear) {
                            dayButton.setBackground(Color.YELLOW);
                        }
                    }
                } catch (NumberFormatException ex) {
                    // بعض الخانات تكون فاضية، نتجاهلها
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
