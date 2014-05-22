package cl.dcc.Groups_Organizer.data;

import java.util.ArrayList;

/**
 * Created by Ian on 21-05-2014.
 */
public class Group {
    public String name, description;
    public ArrayList<Person> members, administrators;

    public Group(String name, String description, ArrayList<Person> administrators) {
        this.name = name;
        this.description = description;
        this.members = new ArrayList<Person>();
        this.administrators = administrators;
    }

    public Group(String name, String description, ArrayList<Person> members, ArrayList<Person> administrators) {
        this.name = name;
        this.description = description;
        this.members = members;
        this.administrators = administrators;
    }
}
