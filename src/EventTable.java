

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import com.formdev.flatlaf.FlatLightLaf;



public class EventTable extends JFrame {
    private JTable eventTable;
    private DefaultTableModel tableModel;

    public EventTable() {
        setTitle("📋 Events list");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // توسيط النافذة
        setLayout(new BorderLayout());

        Font arabicFont = new Font("Arial", Font.PLAIN, 14);
        String[] columnNames = { "Category", "Location", "Date", "Event Name" };

        tableModel = new DefaultTableModel(columnNames, 0);
        eventTable = new JTable(tableModel);
        eventTable.setFont(arabicFont);
        eventTable.setRowHeight(30);
        eventTable.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        // تخصيص رأس الجدول
        JTableHeader header = eventTable.getTableHeader();
        header.setFont(arabicFont);
        header.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        // محاذاة الجدول في المنتصف
        JScrollPane scrollPane = new JScrollPane(eventTable);
        scrollPane.setPreferredSize(new Dimension(650, 400)); // تحديد حجم الجدول
        scrollPane.setBackground(new Color(186, 85, 211, 50)); // شفافية بدرجات اللون الموف
        add(scrollPane, BorderLayout.CENTER);

        // 🔄 زر تحديث
        JButton refreshButton = createStyledButton("Update", new Color(83, 30, 141));
        refreshButton.addActionListener(e -> {
            tableModel.setRowCount(0); // إعادة تعيين الجدول
            loadEventsFromDatabase();
        });

        // ✅ زر اختيار الفعالية
        JButton selectButton = createStyledButton("Choose your favorite activity", new Color(255, 153, 0)); // برتقالي
        selectButton.addActionListener(e -> handleEventSelection());

        // ❌ زر خروج
        JButton exitButton = createStyledButton("Exit", new Color(200, 55, 60));
        exitButton.addActionListener(e -> {
            dispose(); // إغلاق النافذة الحالية
            new LoginWindow().setVisible(true); // فتح نافذة LoginWindow
        });

        // 📅 زر عرض التقويم
JButton calendarButton = createStyledButton("Calendar", new Color(60, 179, 113)); // أخضر ناعم
calendarButton.addActionListener(e -> {
  new ShowCalendar().setVisible(true);
});

        // ✅ إضافة الأزرار في الأسفل
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(78, 129, 218)); // أزرق Tech-Inspire
        bottomPanel.add(refreshButton);
        bottomPanel.add(selectButton);
        bottomPanel.add(exitButton);
        bottomPanel.add(calendarButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // 📥 جلب البيانات عند التشغيل
        loadEventsFromDatabase();
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Tahoma", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void loadEventsFromDatabase() {
        try {
            Connection conn = DBConnection.getConnection(); // تأكدي من وجود هذا الكلاس
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name, date, location, category FROM events");

            while (rs.next()) {
                String name = rs.getString("name");
                String date = rs.getString("date");
                String location = rs.getString("location");
                String category = rs.getString("category");

                tableModel.addRow(new Object[]{ category, location, date, name });
                EventDateReminder.checkTodayEvents(name, date);

            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while fetching data from the database.");
        }
    }

    private void handleEventSelection() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "⚠️ There are currently no events.");
            return;
        }

        int selectedRow = eventTable.getSelectedRow();

        if (selectedRow >= 0) {
            String name = tableModel.getValueAt(selectedRow, 3).toString();
            String date = tableModel.getValueAt(selectedRow, 2).toString();
            String location = tableModel.getValueAt(selectedRow, 1).toString();
            String category = tableModel.getValueAt(selectedRow, 0).toString();

            int userId = Session.getid();
            String username = Session.getUsername();

            FavoriteEventDAO eventDAO = new FavoriteEventDAO();
            String role = Session.getRole();

            if (userId > 0) {
                if ("admin".equalsIgnoreCase(role)) {
                    JOptionPane.showMessageDialog(this, "⚠️ The administrator cannot add events to his favorites .");
                    return;
                }

                if (eventDAO.isEventAlreadyFavorited(userId, name)) {
                    JOptionPane.showMessageDialog(this, "⚠️ You have already added this event to your favorites .");
                } else {
                    eventDAO.saveFavoriteEvent(userId, name, date, location, category);
                    JOptionPane.showMessageDialog(this, "✅ The event has been added to your favorites .");
                }
            } else {
                JOptionPane.showMessageDialog(this, "⚠️ You must Login first.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "⚠️ Please select an event from the list first.");
        }

        System.out.println("Current User ID: " + Session.getid());
        System.out.println("Current Username: " + Session.getUsername());
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            new EventTable().setVisible(true);
        });
    }
}
