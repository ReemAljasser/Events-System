

import javax.swing.*;
import java.awt.*;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
public class FormsManager {
    private Application application;
    private static FormsManager instance;

    public static FormsManager getInstance() {
      
        if (instance == null) {
          Application app = new Application();
            instance = new FormsManager(app);
            // Instead, create the JFrame and set it visible directly  
            
        }
        return instance;
    }

    public FormsManager(Application app) {
      this.application = app; // ✅ تم تمرير وإسناد الكائن
  }
  
  

    public void initApplication(Application application) {
        this.application = application;
    }

    public void showForm(LoginWindow form) {
        EventQueue.invokeLater(() -> {
            FlatAnimatedLafChange.showSnapshot();
            application.setContentPane(form);
            application.revalidate();
            application.repaint();
            FlatAnimatedLafChange.hideSnapshotWithAnimation();
        });
    }

    public void showSignupForm() {
      SwingUtilities.invokeLater(() -> {  
        new Signup().setVisible(true);  // Pass the Application instance  
    }); 
  }
  public void showLoginForm() {
    SwingUtilities.invokeLater(() -> {  
      new LoginWindow().setVisible(true);  // Pass the Application instance  
    });
  }
}