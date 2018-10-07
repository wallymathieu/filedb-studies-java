package se.gewalli.entities;


public class Customer {
    public final int id;
    public final String firstname;
    public final String lastname;
    public final int version;

    public Customer(int id, String firstName, String lastName, int version) {
        this.id = id;
        this.firstname = firstName;
        this.lastname = lastName;
        this.version = version;
    }
}
