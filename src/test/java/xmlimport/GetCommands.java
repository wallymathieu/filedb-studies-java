package xmlimport;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import se.gewalli.commands.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GetCommands {
    public Collection<Command> Get() {
        try {
            BufferedReader r = Files.newBufferedReader(Paths.get("src/test/java/TestData.xml"));

            XmlMapper xmlMapper = new XmlMapper();
            Database d= xmlMapper.readValue(r, Database.class);
            List<Command> commands=new ArrayList<>();
            for (Customer c : d.customers) {
                commands.add(new AddCustomerCommand(
                        c.id,
                        c.version,
                        c.firstname,
                        c.lastname));
            }
            for (Product c : d.products) {
                commands.add(new AddProductCommand(
                        c.id,
                        c.version,
                        c.cost,
                        c.name));
            }
            for (Order c : d.orders) {
                commands.add(new AddOrderCommand(
                        c.id,
                        c.version,
                        c.customer,
                        c.orderDate.toInstant()));
            }
            for (OrderProduct c : d.orderProducts) {
                commands.add(new AddProductToOrderCommand(0, 0,
                        c.order, c.product));
            }
            return commands;
        } catch (IOException e) { //
            throw new RuntimeException(e);
        }
    }
}