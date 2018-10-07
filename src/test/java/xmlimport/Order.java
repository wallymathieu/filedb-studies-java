package xmlimport;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Date;

public class Order{
    @JacksonXmlProperty(localName ="Id")
    public int id;
    @JacksonXmlProperty(localName ="Customer")
    public int customer;
    @JacksonXmlProperty(localName ="OrderDate")
    public Date orderDate;
    @JacksonXmlProperty(localName ="Version")
    public int version;
}
