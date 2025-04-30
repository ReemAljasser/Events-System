public class Session {
  private static int userId;
  private static String username;
  private static String role; // 👈 متغير جديد


  public static void setUser(int id, String user) {
      userId = id;
      username = user;
  }

  public static int getid() {
      return userId;
  }

  public static String getUsername() {
      return username;
  }
  public static void setRole(String r) {
    role = r;
}
  public static String getRole() {
    return role;
}
}
