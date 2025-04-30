 

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EventForm extends JFrame{
  private JTextField nameField, dateField, locationField;
    private JComboBox<String> categoryBox;
    private JButton submitButton;
    private EventDAO eventDAO = new EventDAO(); // داخل الكلاس

    

     public EventForm() {
        setTitle("إضافة فعالية جديدة");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLayout(new GridLayout(5, 2, 10, 10));
        setLocationRelativeTo(null); // يوسّط النافذة
         // تغيير اتجاه الواجهة إلى اليمين
         applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        // العناصر
        JLabel nameLabel = new JLabel("اسم الفعالية:");
        nameField = new JTextField();

        JLabel dateLabel = new JLabel("تاريخ الفعالية:");
        dateField = new JTextField();

        JLabel locationLabel = new JLabel("الموقع:");
        locationField = new JTextField();

        JLabel categoryLabel = new JLabel("نوع الفعالية:");
        String[] categories = { "lecture", "camp", "fair" };
        categoryBox = new JComboBox<>(categories);

        submitButton = new JButton("إضافة");
        

        // الأحداث
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String date = dateField.getText();
                String location = locationField.getText();
                String category = (String) categoryBox.getSelectedItem();
                

                if (name.isEmpty() || date.isEmpty() || location.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "يرجى تعبئة جميع الحقول!", "خطأ", JOptionPane.ERROR_MESSAGE);
                } else {
                    // هنا تقدر تطبع أو ترسل البيانات لـ سيرفس أو تحفظها في ملف
                    System.out.println("اسم الفعالية: " + name);
                    System.out.println("التاريخ: " + date);
                    System.out.println("الموقع: " + location);
                    System.out.println("النوع: " + category);
                    JOptionPane.showMessageDialog(null, "✅ تم إضافة الفعالية بنجاح!");
                    clearForm();
                }
            }
        });

        // إضافة للعناصر
        add(nameLabel); add(nameField);
        add(dateLabel); add(dateField);
        add(locationLabel); add(locationField);
        add(categoryLabel); add(categoryBox);
        add(new JLabel()); add(submitButton);
    }

    private void clearForm() {
        nameField.setText("");
        dateField.setText("");
        locationField.setText("");
        categoryBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new EventForm().setVisible(true);
        });
    }
}
