import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by dmanzelmann on 2/11/15.
 */
public class Listing {
    DateTime startTime;
    DateTime endTime;
    String room;
    String activity;
    String faculty;

    public Listing() {}

    public Listing(DateTime startTime, DateTime endTime, String room, String activity, String faculty) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
        this.activity = activity;
        this.faculty = faculty;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public void setEndTime(DateTime endTime) {
        this.endTime = endTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String toString() {
        DateTimeFormatter startTimeFmt = DateTimeFormat.forPattern("M d h:mma");
        DateTimeFormatter endTimeFmt = DateTimeFormat.forPattern("h:mma");
        return startTime.toString(startTimeFmt) + " - " + endTime.toString(endTimeFmt) + " | " + room + " | " + activity + " | " + faculty;
    }
}
