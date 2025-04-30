
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FavoriteEventDAO {

      // Constructor to initialize the database connection

    public void saveFavoriteEvent(int user_id, String name, String date, String location, String category) {
    try {
        Connection conn = DBConnection.getConnection();
        String sql = "INSERT INTO favorite_events (user_id, name, date, location, category) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, user_id);
        stmt.setString(2, name);
        stmt.setString(3, date);
        stmt.setString(4, location);
        stmt.setString(5, category);
        stmt.executeUpdate();
        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
  }

    public boolean isEventAlreadyFavorited(int user_id, String name) {
      try {
          Connection conn = DBConnection.getConnection();
          String query = "SELECT * FROM favorite_events WHERE user_id = ? AND name = ?";
          PreparedStatement stmt = conn.prepareStatement(query);
          stmt.setInt(1, user_id);
          stmt.setString(2, name);
          ResultSet rs = stmt.executeQuery();
  
          return rs.next(); // إذا فيه نتيجة => الفعالية موجودة
      } catch (Exception e) {
          e.printStackTrace();
      }
      return false;
    }
  


}
