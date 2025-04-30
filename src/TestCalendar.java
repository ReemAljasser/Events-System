import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class TestCalendar {
  public static void main(String[] args) {
      SwingUtilities.invokeLater(() -> {
          JFrame frame = new JFrame("اختبار تلوين الأيام");
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frame.setSize(300, 300);

          CustomDayChooser chooser = new CustomDayChooser();

          Set<LocalDate> events = new HashSet<>();
          events.add(LocalDate.now()); // اليوم
          events.add(LocalDate.of(2025, 4, 25)); // يوم ثاني

          chooser.setEventDates(events);

          frame.add(chooser);
          frame.setVisible(true);

      });
  }

  public void setVisible(boolean b) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'setVisible'");
  }
}
