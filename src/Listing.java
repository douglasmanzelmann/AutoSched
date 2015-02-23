import java.util.Date;

/**
 * Created by dmanzelmann on 2/11/15.
 */
public class Listing {
    Date day;
    Date time;
    String date;
    String stringTime;
    String room;
    String activity;
    String faculty;

    public Listing() {}

    public Listing(Date day, Date time, String room, String activity, String faculty) {
        this.day = day;
        this.time = time;
        this.room = room;
        this.activity = activity;
        this.faculty = faculty;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public void setDate(String date) { this.date = date; }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setStringTime(String stringTime) { this.stringTime = stringTime; }

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
        return date + " | " + stringTime + " | " + room + " | " + activity + " | " + faculty;
    }
}
