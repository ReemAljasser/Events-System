

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.AbstractButton;

public class App {  
  
    public static void main(String[] args) throws Exception {
        // إنشاء النافذة الأساسية
        JFrame frame = new JFrame("Tech Events");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        // عنوان في الأعلى
        JLabel label = new JLabel("منصة الفعاليات التقنية", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));

        // زر
        JButton showButton = new JButton("عرض الفعاليات");

        // عند الضغط على الزر
        showButton.addActionListener((ActionListener) new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showEventsWindow(); // فتح نافذة الفعاليات
            }

        });

        // ترتيب العناصر في النافذة
        frame.setLayout(new BorderLayout());
        frame.add(label, BorderLayout.CENTER);
        frame.add(showButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    // نافذة ثانية فيها الفعاليات
    private static void showEventsWindow() {
        JFrame eventsFrame = new JFrame("الفعاليات القادمة");
        eventsFrame.setSize(300, 300);

        String[] events = {
                " محاضرة الأمن السيبراني",
                " معسكر البيانات الضخمة",
                " المعرض التقني الدولي",
                " ورشة تحليل البيانات"
        };

        JList<String> eventList = new JList<>(events);
        eventList.setFont(new Font("Arial", Font.PLAIN, 14));

        eventsFrame.add(new JScrollPane(eventList));
        eventsFrame.setVisible(true);
    }
        
}

