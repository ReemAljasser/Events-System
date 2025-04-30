
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

public class EventDateReminder {

    public static void checkTodayEvents(String eventName, String eventDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String today = sdf.format(new Date());

            if (eventDate.equals(today)) {
                JOptionPane.showMessageDialog(null, "📅 اليوم عندك فعالية: " + eventName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
