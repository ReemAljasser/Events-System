
public class events {
  private String name;
  private String date;
  private String location;
  private String category;


  public void event(String name, String date, String location, String category) {
      this.name = name;
      this.date = date;
      this.location = location;
      this.category = category;
  }

  // Getters
  public String getName() {
      return name;
  }

  public String getDate() {
      return date;
  }

  public String getLocation() {
      return location;
  }

  public String getCategory() {
      return category;
  }

  // Setters
  public void setName(String name) {
      this.name = name;
  }

  public void setDate(String date) {
      this.date = date;
  }

  public void setLocation(String location) {
      this.location = location;
  }

  public void setCategory(String category) {
      this.category = category;
  }

  // عرض مختصر للفعالية - مفيد للطباعة أو التصحيح
  @Override
  public String toString() {
      return "Event{" +
              "name='" + name + '\'' +
              ", date='" + date + '\'' +
              ", location='" + location + '\'' +
              ", category='" + category + '\'' +
              '}';
  }


}
