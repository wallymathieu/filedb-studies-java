package se.gewalli.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Customer {
    public final int id;
    public final String firstname;
    public final String lastname;
    public final int version;
    @JsonCreator
    public Customer(@JsonProperty("id") int id,@JsonProperty("firstname") String firstName,@JsonProperty("lastname") String lastName, @JsonProperty("version")int version) {
        this.id = id;
        this.firstname = firstName;
        this.lastname = lastName;
        this.version = version;
    }
}
