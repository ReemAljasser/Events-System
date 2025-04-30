

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class EventDAO {
    private final String url = "jdbc:mysql://localhost:3306/events_db";
    private final String user = "root"; // اسم المستخدم
    private final String password = ""; // كلمة المرور
        private List<Event> events = new ArrayList<>();


    public void saveEvent(Event event) {
        String sql = "INSERT INTO events (name, date, location, category) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, event.getName());
            stmt.setString(2, event.getDate());
            stmt.setString(3, event.getLocation());
            stmt.setString(4, event.getCategory());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteLastEvent() {
      try {
          Connection conn = DBConnection.getConnection();
          
          // نحصل على آخر id
          PreparedStatement getLastId = conn.prepareStatement("SELECT id FROM events ORDER BY id DESC LIMIT 1");
          ResultSet rs = getLastId.executeQuery();
          
          if (rs.next()) {
              int lastId = rs.getInt("id");

              // حذف الفعالية
              PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM events WHERE id = ?");
              deleteStmt.setInt(1, lastId);
              deleteStmt.executeUpdate();
          }

          conn.close();
      } catch (Exception e) {
          e.printStackTrace();
      }
  }

  // Method to delete all events  
  public void deleteAllEvents() {  
    events.clear(); // Clear the list or add your database logic here  
}  
}
