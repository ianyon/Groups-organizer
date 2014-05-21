package cl.dcc.Groups_Organizer.data;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ian on 14-04-2014.
 */
public class Event {
    public String name, description, location;
    public Date datetime;
    public ArrayList<Person> confirmed, guestList;

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
}
