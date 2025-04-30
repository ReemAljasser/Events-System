import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import javax.swing.*;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;

public class LoginWindow extends JFrame {
  private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JCheckBox chRememberMe;
    private JButton cmdLogin;

    public LoginWindow() {
         setLayout(new MigLayout("fill,insets 20", "[center]", "[center]"));
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        chRememberMe = new JCheckBox("Remember me");
        cmdLogin = new JButton("Login");
        JPanel panel = new JPanel(new MigLayout("wrap,fillx,insets 35 45 30 45", "fill,250:280"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:20;" +
                "[light]background:darken(@background,3%);" +
                "[dark]background:lighten(@background,3%)");

        txtPassword.putClientProperty(FlatClientProperties.STYLE, "" +
                "showRevealButton:true");
        cmdLogin.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");

        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your username or email");
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your password");

        JLabel lbTitle = new JLabel("Welcome back!");
        JLabel description = new JLabel("Please sign in to access your account");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +10");
        description.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]foreground:lighten(@foreground,30%);" +
                "[dark]foreground:darken(@foreground,30%)");

        panel.add(lbTitle);
        panel.add(description);
        panel.add(new JLabel("Username"), "gapy 8");
        panel.add(txtUsername);
        panel.add(new JLabel("Password"), "gapy 8");
        panel.add(txtPassword);
        panel.add(chRememberMe, "grow 0");
        panel.add(cmdLogin, "gapy 10");
        panel.add(createSignupLabel(), "gapy 10");
        add(panel);
        // ✅ ربط الزر بعملية تسجيل الدخول
        
        cmdLogin.addActionListener(e -> {
          try {
      handleLogin();
    } catch (SQLException ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
  }
});
    }

    private Component createSignupLabel() {
      JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
      panel.putClientProperty(FlatClientProperties.STYLE, "" +
              "background:null");
      JButton cmdRegister = new JButton("<html><a href=\"#\">Sign up</a></html>");
      cmdRegister.putClientProperty(FlatClientProperties.STYLE, "" +
              "border:3,3,3,3");
      cmdRegister.setContentAreaFilled(false);
      cmdRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
      cmdRegister.addActionListener(e -> {
        new Signup().setVisible(true);       });
      JLabel label = new JLabel("Don't have an account ?");
      label.putClientProperty(FlatClientProperties.STYLE, "" +
              "[light]foreground:lighten(@foreground,30%);" +
              "[dark]foreground:darken(@foreground,30%)");
      panel.add(label);
      panel.add(cmdRegister);
      return panel;
  }

    private void handleLogin() throws SQLException {
      String username = txtUsername.getText();
      String password = new String(txtPassword.getPassword());
  
      UserDAO userDAO = new UserDAO(DBConnection.getConnection());
      User user = userDAO.getUserByCredentials(username, password);
  
      if (user != null) {
        Session.setRole(user.getRole()); // 👈 أضيفي هذا السطر
          if (user.getRole().equalsIgnoreCase("admin")) {
            Session.setUser(user.getId(), user.getUsername());
              openAdminPanel(user);
          } else {
            Session.setUser(user.getId(), user.getUsername());
              openUserProfile(user);
          }
      } else {
          JOptionPane.showMessageDialog(this, "The username or password is incorrect.");
      }
      System.out.println("Session set: ID = " + user.getId() + ", Username = " + user.getUsername());

  }

  private void openAdminPanel(User user) {
    this.dispose();
    SwingUtilities.invokeLater(() -> new AdminDashboard().setVisible(true));
}

private void openUserProfile(User user) {
    this.dispose();
    SwingUtilities.invokeLater(() -> new UserProfileWindow(user.getId()).setVisible(true));
}


    public static void main(String[] args) {
      System.out.println("Login window started...");
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new LoginWindow().setVisible(true));
    }

}