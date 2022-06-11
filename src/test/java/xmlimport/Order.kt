package xmlimport

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import java.util.*

class Order {
    @JacksonXmlProperty(localName = "Id")
    var id = 0

    @JacksonXmlProperty(localName = "Customer")
    var customer = 0

    @JacksonXmlProperty(localName = "OrderDate")
    var orderDate: Date? = null

    @JacksonXmlProperty(localName = "Version")
    var version = 0
}