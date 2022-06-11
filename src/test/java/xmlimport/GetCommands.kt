package xmlimport

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import se.gewalli.commands.*
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

class GetCommands {
    fun Get(): Collection<Command> {
        return try {
            val r = Files.newBufferedReader(Paths.get("src/test/java/TestData.xml"))
            val xmlMapper = XmlMapper()
            val d = xmlMapper.readValue(r, Database::class.java)
            val commands: MutableList<Command> =
                ArrayList()
            for (c in d.customers) {
                commands.add(
                    AddCustomerCommand(
                        c.id,
                        c.version,
                        c.firstname!!,
                        c.lastname!!
                    )
                )
            }
            for (c in d.products) {
                commands.add(
                    AddProductCommand(
                        c.id,
                        c.version,
                        c.cost,
                        c.name!!
                    )
                )
            }
            for (c in d.orders) {
                commands.add(
                    AddOrderCommand(
                        c.id,
                        c.version,
                        c.customer,
                        c.orderDate!!.toInstant()
                    )
                )
            }
            for (c in d.orderProducts) {
                commands.add(
                    AddProductToOrderCommand(
                        0, 0,
                        c.order, c.product
                    )
                )
            }
            commands
        } catch (e: IOException) { //
            throw RuntimeException(e)
        }
    }
}