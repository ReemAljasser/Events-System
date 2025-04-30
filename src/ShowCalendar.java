import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class ShowCalendar extends javax.swing.JFrame {

    public ShowCalendar() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        calendarCustom2 = new Calendar2(); // التقويم
        getContentPane().setLayout(new BorderLayout());

        // إضافة التقويم أولاً
        getContentPane().add(jPanel1, BorderLayout.CENTER);

        // إعداد الزر لإظهار الفعاليات
        colorizeButton = new JButton("Show Events with Colors");
        colorizeButton.addActionListener(e -> new CalendarWithDB().setVisible(true));

        // إضافة الزر إلى jPanel1
        jPanel1.setLayout(new BorderLayout()); // تغيير التخطيط ليكون مناسبًا
        jPanel1.add(colorizeButton, BorderLayout.NORTH); // إضافة الزر في الجزء العلوي من jPanel1
        jPanel1.add(calendarCustom2, BorderLayout.CENTER); // إضافة التقويم في الجزء المركزي من jPanel1

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        calendarCustom2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(205, 205, 205)));

        pack();
        setLocationRelativeTo(null);  // لضبط الإطار في المنتصف
    }

    private void colorizeCalendar() {
        // إضافة الألوان بناءً على الفعاليات من قاعدة البيانات
        try {
            // الاتصال بقاعدة البيانات
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/events_db?useSSL=false&serverTimezone=UTC", "root", "");
            String query = "SELECT date, name FROM events";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // تلوين الأيام في التقويم بناءً على البيانات من قاعدة البيانات
            while (rs.next()) {
                Date eventDate = rs.getDate("date");
                String eventName = rs.getString("name");

                // تحديد اليوم الذي يحتوي على الفعالية
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(eventDate);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // تحديد اللون (يمكنك تخصيصه حسب نوع الفعالية)
                Color eventColor = Color.GREEN; // مثال: اختر اللون الأحمر لكل الأحداث
                calendarCustom2.setDayBackground(day, eventColor); // تعيين اللون على اليوم المحدد في التقويم
            }

            conn.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ShowCalendar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new ShowCalendar().setVisible(true);
        });
    }

    // Variables declaration
    private Calendar2 calendarCustom2;
    private javax.swing.JPanel jPanel1;
    private JButton colorizeButton; // تعريف الزر
}
