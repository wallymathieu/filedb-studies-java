package xmlimport

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

class OrderProduct {
    @JacksonXmlProperty(localName = "Product")
    var product = 0

    @JacksonXmlProperty(localName = "Order")
    var order = 0
}