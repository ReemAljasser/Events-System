
import javax.swing.*;  
import javax.swing.table.DefaultTableModel;  
import java.awt.*;  
import com.formdev.flatlaf.FlatLightLaf;  

public class AdminDashboard extends JFrame {  
    private JTable table;  
    private JTextField nameField, dateField, locationField;  
    private JComboBox<String> categoryBox;  
    private EventService eventService;  
    private EventDAO eventDAO = new EventDAO();  

    public AdminDashboard() {  
        super("📍Technical Events System");  
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        setSize(800, 400);  
        setLocationRelativeTo(null);  

        eventService = new EventService();  
        setLayout(new BorderLayout());  

        // الجدول  
        table = new JTable(new DefaultTableModel(new Object[]{"Name", "Date", "Category", "Location"}, 0));  
        add(new JScrollPane(table), BorderLayout.CENTER);  

        // الفورم باستخدام GridBagLayout  
        JPanel form = new JPanel(new GridBagLayout());  
        GridBagConstraints gbc = new GridBagConstraints();  
        gbc.insets = new Insets(5, 5, 5, 5); // حواف بين العناصر  
        form.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);  

        // إدخال الحقول  
        nameField = new JTextField(15);  
        dateField = new JTextField(10);  
        locationField = new JTextField(10);  
        categoryBox = new JComboBox<>(new String[]{"Workshops", "Exhibitions", "Lectures", "Hackathons","Bootcamp","Conference","Lecture"});  

        // الزر  
        JButton addButton = new JButton("Add an event");  
        JButton deleteButton = new JButton("Delete Activity X");  
        JButton clearAllButton = new JButton("Delete all events 🚫 ");  
        JButton showUsers = new JButton("Show all users");  

        JButton showAll = new JButton("View all events"); // أخضر
        showAll.addActionListener(e -> {
            new EventTable().setVisible(true); 
            updateTable();  
                // فتح نافذة EventTable
        });

        ImageIcon icon2 = new ImageIcon("src/icon/pdf.png");// استبدل المسار بمسار الأيقونة لديك  
        // تغيير حجم الأيقونة إلى 32x32 بكسل (يمكنك تغيير الأبعاد حسب الحاجة)  
       Image image2 = icon2.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);  
       ImageIcon resizedIcon2 = new ImageIcon(image2); 

        JButton exportPDFButton = new JButton("Export All Events to PDF", resizedIcon2);
exportPDFButton.addActionListener(e -> {
    AdminEventsExporter.exportToPDF();
    JOptionPane.showMessageDialog(this, "✅ Events exported successfully to Admin_Events_Summary.pdf");
});



        // تخصيص الألوان  
        configureButton(addButton, new Color(46, 204, 113));  
        configureButton(deleteButton, new Color(231, 76, 60));  
        configureButton(showAll, new Color(83, 30, 141));  
        configureButton(showUsers, new Color(41, 160, 113));
        configureButton(exportPDFButton, new Color(30, 100, 100)); 



        clearAllButton.setBackground(Color.DARK_GRAY);  
        clearAllButton.setForeground(Color.WHITE);  

        // إضافة الحقول والأزرار إلى النموذج  
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;  
        form.add(new JLabel("Name:"), gbc);  

        gbc.gridx = 1; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;  
        form.add(nameField, gbc);  

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;  
        form.add(new JLabel("Date:"), gbc);  

        gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;  
        form.add(dateField, gbc);  

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;  
        form.add(new JLabel("Location:"), gbc);  

        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST;  
        form.add(locationField, gbc);  

        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST;  
        form.add(new JLabel("Category:"), gbc);  

        gbc.gridx = 1; gbc.gridy = 3; gbc.anchor = GridBagConstraints.WEST;  
        form.add(categoryBox, gbc);  

        gbc.gridx = 0; gbc.gridy = 4;  
        form.add(addButton, gbc);  

        gbc.gridx = 1; gbc.gridy = 4;  
        form.add(deleteButton, gbc);  

        gbc.gridx = 0; gbc.gridy = 5;  
        form.add(showAll, gbc);  
        
        gbc.gridx = 1; gbc.gridy = 5;  
        form.add(clearAllButton, gbc);  

        gbc.gridx = 0; gbc.gridy = 6;  
        form.add(showUsers, gbc);

        gbc.gridx = 3; gbc.gridy = 4;  
        form.add(exportPDFButton, gbc);
        
        
        
        
        add(form, BorderLayout.SOUTH);  

        // إضافة فعالية  
        addButton.addActionListener(e -> {  
            String name = nameField.getText().trim();  
            String date = dateField.getText().trim();  
            String location = locationField.getText().trim();  
            String category = categoryBox.getSelectedItem().toString();  

            if (name.isEmpty() || date.isEmpty() || location.isEmpty()) {  
                JOptionPane.showMessageDialog(this, "Please fill in all fields before adding.", "alert", JOptionPane.WARNING_MESSAGE);  
                return;  
            }  

            Event event = new Event(name, date, location, category);  
            eventDAO.saveEvent(event);  
            eventService.addEvent(event);  
            updateTable();  

            nameField.setText("");  
            dateField.setText("");  
            locationField.setText("");  
        });  

        // حذف آخر فعالية  
        deleteButton.addActionListener(e -> {  
            if (!eventService.getAllEvents().isEmpty()) {  
                Event lastEvent = eventService.getAllEvents().get(eventService.getAllEvents().size() - 1);  
                eventDAO.deleteLastEvent();  
                eventService.getAllEvents().remove(lastEvent);  
                updateTable();  
            } else {  
                JOptionPane.showMessageDialog(this, "There are no events to delete.");  
            }  
        }); 
        
        // Delete all events  
        clearAllButton.addActionListener(e -> {  
  int confirm = JOptionPane.showConfirmDialog(this,   
          "Are you sure you want to delete all events?",   
          "Confirm Deletion",   
          JOptionPane.YES_NO_OPTION);  

          if (confirm == JOptionPane.YES_OPTION) {
            try {
                eventDAO.deleteAllEvents();
                eventService.deleteAllEvents();
                updateTable();
                JOptionPane.showMessageDialog(this, "All events have been successfully deleted.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting events: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
});  

        updateTable();  
        setVisible(true);  
        
        showUsers.addActionListener(e -> {  
            new AdminPanel().setVisible(true);  
        });  
    }  

    private void configureButton(JButton button, Color color) {  
        button.setBackground(color);  
        button.setForeground(Color.WHITE);  
        button.setFont(new Font("Arial", Font.BOLD, 14));  
    }  

    private void updateTable() {  
        DefaultTableModel model = (DefaultTableModel) table.getModel();  
        model.setRowCount(0);  
        for (Event e : eventService.getAllEvents()) {  
            model.addRow(new Object[]{e.getName(), e.getDate(), e.getLocation(), e.getCategory()});  
        }  
    }  

    public static void main(String[] args) {  
        try {  
            UIManager.setLookAndFeel(new FlatLightLaf());  
        } catch (Exception ex) {  
            ex.printStackTrace();  
        }   
        new AdminDashboard();  
    }  
}  