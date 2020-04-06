package ca.bcit.studybuddy;

import java.util.ArrayList;

/**
 * Class to represent a user.
 */
public class User {
    public String name;
    public String location;
    public String major;
    public String phone;
    public String pk;
    public String school;
    public ArrayList<String> friends;
    public ArrayList<String> requests;
    public ArrayList<String> sendRequests;

    public User(String name, String location, String major, String phone, String pk, String school, ArrayList<String> friends, ArrayList<String> requests, ArrayList<String> sendRequests) {
        this.name = name;
        this.location = location;
        this.major = major;
        this.phone = phone;
        this.pk = pk;
        this.school = school;
        this.friends = friends;
        this.requests = requests;
        this.sendRequests = sendRequests;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", major='" + major + '\'' +
                ", phone='" + phone + '\'' +
                ", pk='" + pk + '\'' +
                ", school='" + school + '\'' +
                ", friends=" + friends +
                ", requests=" + requests +
                ", sendRequests=" + sendRequests +
                '}';
    }
}
