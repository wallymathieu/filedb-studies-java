package xmlimport

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

class Product {
    @JacksonXmlProperty(localName = "Id")
    var id = 0

    @JacksonXmlProperty(localName = "Cost")
    var cost = 0f

    @JacksonXmlProperty(localName = "Name")
    var name: String? = null

    @JacksonXmlProperty(localName = "Version")
    var version = 0
}