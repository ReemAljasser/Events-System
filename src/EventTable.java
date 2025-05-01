

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.formdev.flatlaf.FlatLightLaf;
import com.toedter.calendar.JDateChooser;



public class EventTable extends JFrame {
    private JTable eventTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton;
    private JComboBox<String> categoryComboBox;
    private JDateChooser dateChooser;

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
  new CalendarWithDB().setVisible(true);
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
        
        // 🔎 شريط البحث
searchField = new JTextField(20);
searchField.setFont(new Font("Arial", Font.PLAIN, 14));

searchButton = createStyledButton("Search", new Color(100, 149, 237)); // أزرق فاتح
searchButton.addActionListener(e -> performSearch());

// ✅ إضافة البحث فوق الجدول
JPanel topPanel = new JPanel();
topPanel.setBackground(new Color(240, 240, 240)); // خلفية رمادية فاتحة
topPanel.add(createStyledLabel("Search:"));
topPanel.add(searchField);
topPanel.add(searchButton);

add(topPanel, BorderLayout.NORTH);

// أضف هذا في الجهة العلوية للنافذة مع خانة البحث
categoryComboBox = new JComboBox<>(new String[]{"All", "Workshops", "Exhibitions", "Lectures", "Hackathons","Bootcamp","Conference","Lecture"});
categoryComboBox.setFont(new Font("Arial", Font.PLAIN, 14));

// زر الفلترة حسب التصنيف
JButton filterCategoryButton = createStyledButton("Filter by Category", new Color(100, 149, 237));
filterCategoryButton.addActionListener(e -> filterByCategory());

// أضف comboBox و filterButton إلى topPanel مثل ما فعلنا مع خانة البحث
topPanel.add(createStyledLabel("Category:"));
topPanel.add(categoryComboBox);
topPanel.add(filterCategoryButton);

// في دالة البناء (EventTable)
dateChooser = new JDateChooser();
dateChooser.setFont(new Font("Arial", Font.PLAIN, 14));
dateChooser.setDateFormatString("yyyy-MM-dd"); // تنسيق التاريخ

// زر الفلترة حسب التاريخ
JButton filterDateButton = createStyledButton("Filter by Date", new Color(100, 149, 237));
filterDateButton.addActionListener(e -> filterByDate());

// أضف dateChooser و filterDateButton إلى topPanel
topPanel.add(createStyledLabel("Date:"));
topPanel.add(dateChooser);
topPanel.add(filterDateButton);

// إضافة زر Clear
JButton clearButton = createStyledButton("Clear Filters", new Color(255, 69, 0)); // أحمر
clearButton.addActionListener(e -> clearFilters());

// إضافة الزر في نفس الواجهة
topPanel.add(clearButton);


    }


    private JLabel createStyledLabel(String text) {
      JLabel label = new JLabel(text);
      label.setFont(new Font("Arial", Font.BOLD, 14)); // تغيير نوع الخط وحجمه
      label.setForeground(new Color(83, 30, 141)); // تغيير اللون إلى لون مميز (مثل اللون البنفسجي)
      label.setHorizontalAlignment(SwingConstants.LEFT); // محاذاة النص لليسار
      label.setBackground(new Color(240, 240, 240)); // تعيين خلفية بلون فاتح
      label.setOpaque(true); // تفعيل الشفافية ليظهر لون الخلفية
      label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // إضافة padding حول النص
      return label;
  }
  


    private void clearFilters() {
      // إعادة مسح قيمة خانة البحث
      searchField.setText("");  // تأكد أنك حافظت على اسم الـ JTextField كـ searchField
      
      // إعادة تعيين التصنيف إلى "All"
      categoryComboBox.setSelectedItem("All");
  
      // إعادة تعيين التاريخ إلى null
      dateChooser.setDate(null);  // تأكد من أن لديك JDateChooser اسمه dateChooser
  
      // إعادة تحميل الجدول بالكامل بدون فلاتر
      tableModel.setRowCount(0);  // مسح الجدول
      loadEventsFromDatabase();    // تحميل الفعاليات من قاعدة البيانات بدون أي فلترة
  }
  

    private void filterByDate() {
    Date selectedDate = dateChooser.getDate();
    
    if (selectedDate == null) {
        JOptionPane.showMessageDialog(this, "⚠️ Please select a date.");
        return;
    }

    // تحويل التاريخ إلى string
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String dateString = dateFormat.format(selectedDate);

    tableModel.setRowCount(0); // مسح الجدول قبل إضافة البيانات الجديدة
    
    try {
        Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        
        String query = "SELECT name, date, location, category FROM events WHERE date = '" + dateString + "'";
        
        ResultSet rs = stmt.executeQuery(query);
        
        while (rs.next()) {
            String name = rs.getString("name");
            String date = rs.getString("date");
            String location = rs.getString("location");
            String category = rs.getString("category");

            tableModel.addRow(new Object[]{ category, location, date, name });
        }

        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "An error occurred while filtering by date.");
    }
}

    
    private void filterByCategory() {
      String selectedCategory = (String) categoryComboBox.getSelectedItem();
      
      tableModel.setRowCount(0); // مسح الجدول قبل إضافة البيانات الجديدة
      
      try {
          Connection conn = DBConnection.getConnection();
          Statement stmt = conn.createStatement();
          
          String query = "SELECT name, date, location, category FROM events";
          
          // إذا اختار المستخدم تصنيف معين، نفلتر بناءً عليه
          if (!"All".equals(selectedCategory)) {
              query += " WHERE category = '" + selectedCategory + "'";
          }
  
          ResultSet rs = stmt.executeQuery(query);
          
          while (rs.next()) {
              String name = rs.getString("name");
              String date = rs.getString("date");
              String location = rs.getString("location");
              String category = rs.getString("category");
  
              tableModel.addRow(new Object[]{ category, location, date, name });
          }
  
          conn.close();
      } catch (Exception e) {
          e.printStackTrace();
          JOptionPane.showMessageDialog(this, "An error occurred while filtering by category.");
      }
  }
  

    private void performSearch() {
      String keyword = searchField.getText().trim();
  
      if (keyword.isEmpty()) {
          JOptionPane.showMessageDialog(this, "⚠️ Please enter a search term.");
          return;
      }
  
      tableModel.setRowCount(0); // 🧹 مسح الجدول قبل تعبئته من جديد
  
      try {
          Connection conn = DBConnection.getConnection();
          Statement stmt = conn.createStatement();
  
          // 🟢 SQL يدور على الاسم، أو الموقع، أو الفئة
          String query = "SELECT name, date, location, category FROM events WHERE "
                  + "name LIKE '%" + keyword + "%' OR "
                  + "location LIKE '%" + keyword + "%' OR "
                  + "category LIKE '%" + keyword + "%'";
  
          ResultSet rs = stmt.executeQuery(query);
  
          while (rs.next()) {
              String name = rs.getString("name");
              String date = rs.getString("date");
              String location = rs.getString("location");
              String category = rs.getString("category");
  
              tableModel.addRow(new Object[]{ category, location, date, name });
          }
  
          conn.close();
      } catch (Exception e) {
          e.printStackTrace();
          JOptionPane.showMessageDialog(this, "An error occurred while searching.");
      }
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
