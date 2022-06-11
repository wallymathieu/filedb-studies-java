package xmlimport

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(namespace = "http://tempuri.org/Database.xsd")
class Database {
    @JvmField
    @JacksonXmlProperty(localName = "Customers")
    var customers: List<Customer> = ArrayList()

    @JvmField
    @JacksonXmlProperty(localName = "Products")
    var products: List<Product> = ArrayList()

    @JvmField
    @JacksonXmlProperty(localName = "Orders")
    var orders: List<Order> = ArrayList()

    @JvmField
    @JacksonXmlProperty(localName = "OrderProducts")
    var orderProducts: List<OrderProduct> = ArrayList()
}