package xmlimport

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

class Customer {
    @JvmField
    @JacksonXmlProperty(localName = "Id")
    var id = 0

    @JvmField
    @JacksonXmlProperty(localName = "Firstname")
    var firstname: String? = null

    @JvmField
    @JacksonXmlProperty(localName = "Lastname")
    var lastname: String? = null

    @JvmField
    @JacksonXmlProperty(localName = "Version")
    var version = 0
}