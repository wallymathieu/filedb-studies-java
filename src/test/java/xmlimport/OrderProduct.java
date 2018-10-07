package xmlimport;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class OrderProduct{
    @JacksonXmlProperty(localName ="Product")
    public int product;
    @JacksonXmlProperty(localName ="Order")
    public int order;
}
