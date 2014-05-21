package cl.dcc.Groups_Organizer.data;

import org.parceler.Parcel;

/**
 * Created by Ian on 21-05-2014.
 */
@Parcel
public class Person {
    public String name;
    public String gender;
    public String email;
    public int age;

    public Person(){}

    public Person(String name, String gender, String email, int age) {
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }
}