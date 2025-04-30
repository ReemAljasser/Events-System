
import java.sql.*;
public class test {
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
  
}
