
import javax.swing.*;  
import java.awt.*;  
import java.awt.event.*;  
import java.sql.SQLException;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;
import raven.toast.Notifications;


public class Signup extends JFrame {  
  private JTextField txtFirstName;
  private JTextField txtLastName;
  private JRadioButton jrMale;
  private JRadioButton jrFemale;
  private JTextField txtUsername;
  private JPasswordField txtPassword;
  private JPasswordField txtConfirmPassword;
  private ButtonGroup groupGender;
  private JButton cmdRegister;
  private PasswordStrengthStatus passwordStrengthStatus;

    public Signup() {  
        setLayout(new MigLayout("fill,insets 20", "[center]", "[center]"));
        txtFirstName = new JTextField();
        txtLastName = new JTextField();
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        txtConfirmPassword = new JPasswordField();
        cmdRegister = new JButton("Sign Up");

        cmdRegister.addActionListener(e -> {
            if (isMatchPassword()) {
              try {
                handleSignup();  // ✅ استدعاء الدالة اللي تسجل البيانات في قاعدة البيانات
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error occurred: " + ex.getMessage());
            }
            } else {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Passwords don't match. Try again!");
            }
        });
        passwordStrengthStatus = new PasswordStrengthStatus();

        JPanel panel = new JPanel(new MigLayout("wrap,fillx,insets 35 45 30 45", "[fill,360]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:20;" +
                "[light]background:darken(@background,3%);" +
                "[dark]background:lighten(@background,3%)");
        
        txtFirstName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "First name");
        txtLastName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Last name");
        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your username or email");
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your password");
        txtConfirmPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Re-enter your password");
        txtPassword.putClientProperty(FlatClientProperties.STYLE, "" +
                "showRevealButton:true");
        txtConfirmPassword.putClientProperty(FlatClientProperties.STYLE, "" +
                "showRevealButton:true");

        cmdRegister.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");

        JLabel lbTitle = new JLabel("Welcome to our Tech Application");
        JLabel description = new JLabel("Join us to inspire your dreem, Sign up now and Show new events!");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +10");
        description.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]foreground:lighten(@foreground,30%);" +
                "[dark]foreground:darken(@foreground,30%)");

        passwordStrengthStatus.initPasswordField(txtPassword);

        panel.add(lbTitle);
        panel.add(description);
        panel.add(new JLabel("Full Name"), "gapy 10");
        panel.add(txtFirstName, "split 2");
        panel.add(txtLastName);
        panel.add(new JLabel("Gender"), "gapy 8");
        panel.add(createGenderPanel());
        panel.add(new JSeparator(), "gapy 5 5");
        panel.add(new JLabel("Username or Email"));
        panel.add(txtUsername);
        panel.add(new JLabel("Password"), "gapy 8");
        panel.add(txtPassword);
        panel.add(passwordStrengthStatus, "gapy 0");
        panel.add(new JLabel("Confirm Password"), "gapy 0");
        panel.add(txtConfirmPassword);
        panel.add(cmdRegister, "gapy 20");
        panel.add(createLoginLabel(), "gapy 10");
        add(panel);
      }
        private Component createGenderPanel() {
          JPanel panel = new JPanel(new MigLayout("insets 0"));
          panel.putClientProperty(FlatClientProperties.STYLE, "" +
                  "background:null");
          jrMale = new JRadioButton("Male");
          jrFemale = new JRadioButton("Female");
          groupGender = new ButtonGroup();
          groupGender.add(jrMale);
          groupGender.add(jrFemale);
          jrMale.setSelected(true);
          panel.add(jrMale);
          panel.add(jrFemale);
          return panel;
      }
  
      private Component createLoginLabel() {
          JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
          panel.putClientProperty(FlatClientProperties.STYLE, "" +
                  "background:null");
          JButton cmdLogin = new JButton("<html><a href=\"#\">Sign in here</a></html>");
          cmdLogin.putClientProperty(FlatClientProperties.STYLE, "" +
                  "border:3,3,3,3");
          cmdLogin.setContentAreaFilled(false);
          cmdLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
          cmdLogin.addActionListener(e -> {
            new LoginWindow().setVisible(true);           });
          JLabel label = new JLabel("Already have an account ?");
          label.putClientProperty(FlatClientProperties.STYLE, "" +
                  "[light]foreground:lighten(@foreground,30%);" +
                  "[dark]foreground:darken(@foreground,30%)");
          panel.add(label);
          panel.add(cmdLogin);
          return panel;
      }
  
    
    private boolean isMatchPassword() {
    
      String password = String.valueOf(txtPassword.getPassword());
      String confirmPassword = String.valueOf(txtConfirmPassword.getPassword());
      return password.equals(confirmPassword);
    }
    
    private void handleSignup() throws SQLException {  
      String username = txtUsername.getText();  
      String password = new String(txtPassword.getPassword());  

      if (username.isEmpty() || password.isEmpty()) {  
          JOptionPane.showMessageDialog(this, "Plase Fill All Information");  
          return;  
      }  

      UserDAO userDAO = new UserDAO(DBConnection.getConnection());  

      if (userDAO.checkUserExists(username)) {  
          JOptionPane.showMessageDialog(this, "User Name Used  !");  
      } else {  
          boolean success = userDAO.addUser(username, password);  
          if (success) {  
              JOptionPane.showMessageDialog(this, "Account Created Susccfully");  
              this.dispose();  
              new LoginWindow().setVisible(true);  
          } else {  
              JOptionPane.showMessageDialog(this, "Ops: There is Something Wrong !");  
          }  
      }
    }
  
    public static void main(String[] args) { 
      try {
        UIManager.setLookAndFeel(new FlatLightLaf());
    } catch (Exception ex) {
        ex.printStackTrace();
    } 
        SwingUtilities.invokeLater(() -> {  
            new Signup().setVisible(true);  
        });  
      
    }  
    
}
