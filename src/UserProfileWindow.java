import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class UserProfileWindow extends JFrame {
    private int userId;
    String username;
    private JLabel nameLabel, favCountLabel;

    public UserProfileWindow(int userId) {
        this.userId = userId;

        setTitle("User Profile");
        setSize(400, 300); // زيادة حجم النافذة
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        // تعديل الخلفية لتكون أكثر تناسقاً
        JPanel content = new JPanel(new GridLayout(5, 1, 10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        content.setBackground(new Color(245, 245, 245)); // لون فاتح أنيق

        // عنوان الترحيب
        JLabel welcomeLabel = new JLabel("Welcome " + getUsernameFromDatabase() + "!");
        welcomeLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setForeground(new Color(78, 129, 218)); // لون بنفسجي هادئ

        add(welcomeLabel, BorderLayout.NORTH);

        // إضافة النصوص للبيانات الشخصية
        nameLabel = new JLabel("User Name 👤: " + getUsernameFromDatabase());
        favCountLabel = new JLabel("Favorite Events Count ❤️: " + getFavoriteCount(userId));

        ImageIcon icon2 = new ImageIcon("resources/image/check-out (1).png");// استبدل المسار بمسار الأيقونة لديك  
        // تغيير حجم الأيقونة إلى 32x32 بكسل (يمكنك تغيير الأبعاد حسب الحاجة)  
       Image image2 = icon2.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);  
       ImageIcon resizedIcon2 = new ImageIcon(image2); 
        // تخصيص الأزرار
        JButton viewFavoritesBtn = new JButton("Show Favorite Events");
        JButton showAll = new JButton("View all events");
        JButton logoutBtn = new JButton("Logout ", resizedIcon2);



        // تخصيص الألوان والأحجام للأزرار
        viewFavoritesBtn.setBackground(new Color(0, 123, 255)); // لون أزرق جذاب
        viewFavoritesBtn.setForeground(Color.WHITE);
        viewFavoritesBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        viewFavoritesBtn.setFocusPainted(false);
        viewFavoritesBtn.setBorderPainted(false);

        showAll.setBackground(new Color(40, 167, 69)); // لون أخضر مريح
        showAll.setForeground(Color.WHITE);
        showAll.setFont(new Font("Arial", Font.PLAIN, 14));
        showAll.setFocusPainted(false);
        showAll.setBorderPainted(false);

        logoutBtn.setBackground(new Color(231, 76, 60)); // لون أحمر للخروج
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);

        // إضافة الأحداث للأزرار
        viewFavoritesBtn.addActionListener(e -> new FavoriteEventsWindow(userId).setVisible(true));
        logoutBtn.addActionListener(e -> {
            dispose(); // إغلاق الصفحة
            new LoginWindow().setVisible(true); // العودة إلى صفحة تسجيل الدخول
        });

        showAll.addActionListener(e -> {
            new EventTable().setVisible(true); // فتح نافذة EventTable
        });

        // إضافة العناصر إلى المحتوى
        content.add(nameLabel);
        content.add(favCountLabel);
        content.add(viewFavoritesBtn);
        content.add(showAll);
        content.add(logoutBtn);

        add(content, BorderLayout.CENTER);
    }

    private int getFavoriteCount(int userId) {
        int count = 0;
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM favorite_events WHERE user_id = ?");
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) count = rs.getInt(1);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    private String getUsernameFromDatabase() {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT username FROM users WHERE id = ?");
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("username");
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "غير معروف";
    }

}
