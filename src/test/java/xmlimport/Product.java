package xmlimport;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Product{
    @JacksonXmlProperty(localName = "Id")
    public int id;
    @JacksonXmlProperty(localName ="Cost")
    public float cost;
    @JacksonXmlProperty(localName ="Name")
    public String name;
    @JacksonXmlProperty(localName ="Version")
    public int version;
}
