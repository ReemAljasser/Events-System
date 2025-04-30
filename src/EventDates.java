import java.util.ArrayList;
import java.util.Date;

public class EventDates {
    private ArrayList<Date> eventDates;

    public EventDates() {
        eventDates = new ArrayList<>();
        // مثال على إضافة تواريخ من قاعدة البيانات
        eventDates.add(new Date(2025, 4, 24)); // 24 أبريل 2025
        eventDates.add(new Date(2025, 4, 25)); // 25 أبريل 2025
    }

    public ArrayList<Date> getEventDates() {
        return eventDates;
    }
}
