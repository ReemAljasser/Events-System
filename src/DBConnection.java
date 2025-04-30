
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

public class DBConnection {
  public static void main(String[] args) {
    String url = "jdbc:mysql://localhost:3306/events_db";
    String user = "root";
    String password = ""; // أو اكتبي كلمة المرور إذا كان عندك

    try (Connection conn = DriverManager.getConnection(url, user, password)) {
        System.out.println("DB contnected");
    } catch (SQLException e) {
        System.out.println("Fail to connect");
        e.printStackTrace();
    }
}

public static Connection getConnection() throws SQLException {
  final String url = "jdbc:mysql://localhost:3306/events_db?useSSL=false&serverTimezone=UTC";
    final String user = "root"; // غيّريه إذا كان اسم المستخدم مختلف
    final String password = ""; // غيّريه إذا عندك كلمة مرور

  return DriverManager.getConnection(url, user, password);
}

}
