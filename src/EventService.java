

import java.util.ArrayList;
import java.util.List;

public class EventService {
    private List<Event> events = new ArrayList<>();

    public void addEvent(Event e) {
        events.add(e);
    }

    public List<Event> getAllEvents() {
        return events;
    }

  // Method to delete all events  
  public void deleteAllEvents() {  
    events.clear(); // Clear the list or add your database logic here  
}  
}