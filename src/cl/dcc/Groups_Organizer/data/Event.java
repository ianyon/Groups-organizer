package cl.dcc.Groups_Organizer.data;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ian on 14-04-2014.
 */
@Parcel
public class Event{
    public String name;
    public String description;
    public String location;
    public Date datetime;
    public List<Person> confirmed;
    public List<Person> guestList;

    private Person mAdmin;

    public Event(){}

    public Event(String name, String description, String location, Date datetime) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.datetime = datetime;
        this.confirmed = new ArrayList<Person>();
        this.guestList = new ArrayList<Person>();
    }

    public Event(String name, String description, String location, Date datetime, ArrayList<Person> guestList) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.datetime = datetime;
        this.confirmed = new ArrayList<Person>();
        this.guestList = guestList;
    }

    public Event(String name, String description, String location, Date datetime, ArrayList<Person> confirmed, ArrayList<Person> guestList) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.datetime = datetime;
        this.confirmed = confirmed;
        this.guestList = guestList;
    }

    public int getGuestCount() {
        return this.guestList.size();
    }

    public int getConfirmedCount() {
        return this.confirmed.size();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public Date getDatetime() {
        return datetime;
    }

    public String getTimeDare() {return datetime.toString();}

    public List<Person> getConfirmed() {
        return confirmed;
    }

    public List<Person> getGuestList() {
        return guestList;
    }
}