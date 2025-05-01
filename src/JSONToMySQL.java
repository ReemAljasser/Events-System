import java.sql.*;
import java.nio.file.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class JSONToMySQL {
    public static void main(String[] args) {
        try {
            // 1. قراءة ملف JSON
            String content = new String(Files.readAllBytes(Paths.get("src/saudi_tech_events_2025.json")));
            JSONArray eventsArray = new JSONArray(content);

            // 2. الاتصال بقاعدة البيانات
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/events_db?useSSL=false&serverTimezone=UTC",
                    "root", ""); // عدّل حسب بياناتك

            // 3. إعداد استعلام الإدخال
            String sql = "INSERT INTO events (id, name, date, location, category) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            // 4. تمرير البيانات من JSON إلى الجدول
            for (int i = 0; i < eventsArray.length(); i++) {
                JSONObject event = eventsArray.getJSONObject(i);

                ps.setInt(1, event.getInt("id"));
                ps.setString(2, event.getString("name"));
                ps.setDate(3, Date.valueOf(event.getString("date")));
                ps.setString(4, event.getString("location"));
                ps.setString(5, event.getString("category"));

                ps.executeUpdate();
            }

            System.out.println("✅ Data inserted successfully!");

            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
