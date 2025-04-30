

import java.sql.*;

public class UserDAO {
    private Connection connection;

    // Constructor to initialize the database connection
    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    // Method to add a user to the database
    public boolean addUser(String username, String password) {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Return true if the insertion was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to check if a user exists in the database
    public boolean checkUserExists(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet resultSet = stmt.executeQuery();
            return resultSet.next(); // Return true if the user exists
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to authenticate user by checking username and password
    public String authenticateUser(String username, String password) throws SQLException {
      Connection conn = DBConnection.getConnection();
      String sql = "SELECT role FROM users WHERE username = ? AND password = ?";
      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setString(1, username);
      stmt.setString(2, password);
      ResultSet rs = stmt.executeQuery();
  
      if (rs.next()) {
          return rs.getString("role"); // يرجع "admin" أو "user"
      }
      return null;
  }
    public int getUserIdByUsername(String username) throws SQLException {
      Connection conn = DBConnection.getConnection();
      String sql = "SELECT id FROM users WHERE username = ?";
      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setString(1, username);
      ResultSet rs = stmt.executeQuery();
  
      if (rs.next()) {
          return rs.getInt("id");
      }
      return -1; // أو throw new exception
  }

  public User getUserByCredentials(String username, String password) throws SQLException {
    String query = "SELECT id, role FROM users WHERE username = ? AND password = ?";
    PreparedStatement stmt = connection.prepareStatement(query);
    stmt.setString(1, username);
    stmt.setString(2, password);
    ResultSet rs = stmt.executeQuery();

    if (rs.next()) {
        int id = rs.getInt("id");
        String role = rs.getString("role");
        return new User(id, username, role); // تحتاج كلاس User بسيط
    }
    return null;
}

  
}
