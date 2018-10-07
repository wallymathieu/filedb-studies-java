package xmlimport;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Customer{
    @JacksonXmlProperty(localName ="Id")
    public int id;
    @JacksonXmlProperty(localName ="Firstname")
    public String firstname;
    @JacksonXmlProperty(localName ="Lastname")
    public String lastname;
    @JacksonXmlProperty(localName ="Version")
    public int version;
}
