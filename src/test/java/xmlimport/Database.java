package xmlimport;

import com.fasterxml.jackson.dataformat.xml.annotation.*;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(namespace = "http://tempuri.org/Database.xsd")
public class Database{
    @JacksonXmlProperty(localName = "Customers")
    public List<xmlimport.Customer> customers=new ArrayList<>();
    @JacksonXmlProperty(localName ="Products")
    public List<xmlimport.Product> products=new ArrayList<>();
    @JacksonXmlProperty(localName ="Orders")
    public List<xmlimport.Order> orders=new ArrayList<>();
    @JacksonXmlProperty(localName ="OrderProducts")
    public List<xmlimport.OrderProduct> orderProducts =new ArrayList<>();
}
