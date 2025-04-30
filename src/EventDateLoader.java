import java.sql.*;
import java.sql.Date;
import java.util.*;

public class EventDateLoader {

    public static List<Date> loadEventDates() {
        List<Date> eventDates = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/events_db?useSSL=false&serverTimezone=UTC",
                "root",
                "" // ← ضعي كلمة المرور إن وجدت
            );
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT date FROM events");

            while (rs.next()) {
                eventDates.add(rs.getDate("date"));
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return eventDates;
    }
}
