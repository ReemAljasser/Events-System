

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AdminPanel extends JFrame {

    private JTable userTable;

    public AdminPanel() {
        setTitle("Administrator Control Panel");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        ImageIcon icon = new ImageIcon("resources/image/user.png");// استبدل المسار بمسار الأيقونة لديك  
         // تغيير حجم الأيقونة إلى 32x32 بكسل (يمكنك تغيير الأبعاد حسب الحاجة)  
        Image image = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);  
        ImageIcon resizedIcon = new ImageIcon(image);  


        JLabel titleLabel = new JLabel("Administrator Control Panel",resizedIcon, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(83, 30, 141));
        add(titleLabel, BorderLayout.NORTH);

        // جدول المستخدمين
        userTable = new JTable(new DefaultTableModel(new Object[]{"id","username","password","role"}, 0));
        userTable.setFont(new Font("Arial", Font.PLAIN, 14));
        userTable.setRowHeight(28);
        userTable.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        add(new JScrollPane(userTable), BorderLayout.CENTER);

        JButton refreshButton = new JButton("Update information");
        refreshButton.setBackground(new Color(46, 204, 113));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));

        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.DARK_GRAY);
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // تحميل البيانات من قاعدة البيانات
        loadUsers();

        // عند الضغط على زر التحديث
        refreshButton.addActionListener(e -> loadUsers());

        // الرجوع للنافذة السابقة
        backButton.addActionListener(e -> {
            dispose();
            new LoginWindow().setVisible(true); // أو ترجعين للنافذة اللي تبينها
        });
    }

    private void loadUsers() {
        DefaultTableModel model = (DefaultTableModel) userTable.getModel();
        model.setRowCount(0);

        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, username, password, role FROM users");

            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String role = rs.getString("role");

                model.addRow(new Object[]{id, username, password, role});
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading user data");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminPanel().setVisible(true));
    }
}
