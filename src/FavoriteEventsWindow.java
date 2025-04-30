

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FavoriteEventsWindow extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private int userId;

    public FavoriteEventsWindow(int userId) {
        this.userId = userId;

        setTitle("📌 Favorite Events");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        // نموذج الجدول
        model = new DefaultTableModel(new String[]{"Name", "Date", "Location", "Category"}, 0);
        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        ImageIcon icon = new ImageIcon("resources/image/pdf.png");// استبدل المسار بمسار الأيقونة لديك  
         // تغيير حجم الأيقونة إلى 32x32 بكسل (يمكنك تغيير الأبعاد حسب الحاجة)  
        Image image = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);  
        ImageIcon resizedIcon = new ImageIcon(image);  
        
        ImageIcon icon2 = new ImageIcon("resources/image/trash.png");// استبدل المسار بمسار الأيقونة لديك  
         // تغيير حجم الأيقونة إلى 32x32 بكسل (يمكنك تغيير الأبعاد حسب الحاجة)  
        Image image2 = icon2.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);  
        ImageIcon resizedIcon2 = new ImageIcon(image2); 

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // 🔘 زر حذف
        JButton deleteButton = new JButton("Delete selected event ", resizedIcon2);
        deleteButton.setBackground(new Color(200, 55, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        deleteButton.addActionListener(e -> deleteSelectedFavorite());

        // 📄 زر تصدير
        JButton exportButton = new JButton("Export to PDF ", resizedIcon);
        exportButton.setBackground(new Color(46, 204, 113));
        exportButton.setForeground(Color.WHITE);
        exportButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        exportButton.addActionListener(e -> {
          String username = Session.getUsername();  // تأكدي أنه موجود بالـ Session
          FavoriteEventsExporter.exportToPDF(userId, username);
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(deleteButton);
        bottomPanel.add(exportButton);
        add(bottomPanel, BorderLayout.SOUTH);

        loadFavorites();
    }

    private void loadFavorites() {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT name, date, location, category FROM favorite_events WHERE user_id = ?");
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                String date = rs.getString("date");
                String location = rs.getString("location");
                String category = rs.getString("category");

                model.addRow(new Object[]{name, date, location, category});
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while loading events.");
        }
    }

    private void deleteSelectedFavorite() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String eventName = model.getValueAt(selectedRow, 0).toString();

            try {
                Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(
                        "DELETE FROM favorite_events WHERE user_id = ? AND name = ?");
                stmt.setInt(1, userId);
                stmt.setString(2, eventName);
                stmt.executeUpdate();
                conn.close();

                model.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "The event has been removed from your favorites.");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "An error occurred while deleting.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an event to delete.");
        }
    }
}
