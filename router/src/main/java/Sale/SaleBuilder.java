package Sale;

import Creator.UpdateCustomerCreator;
import Creator.UpdateGroupCreator;
import domain.Customer;
import domain.Sale;
import domain.Summary;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

///**
// *
// * @author User
// */
public class SaleBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // Create Sale object and store information in exchange properties.
        from("jms:queue:new-sale")
                .unmarshal().json(JsonLibrary.Gson, Sale.class)
                .log("Formatted sale: ${body}")
                .setProperty("Customer_Id").simple("${body.customer.id}")
                .setProperty("Customer_Group").simple("${body.customer.group}")
                .setProperty("Customer_Email").simple("${body.customer.email}")
                .setProperty("Customer_FirstName").simple("${body.customer.firstName}")
                .setProperty("Customer_LastName").simple("${body.customer.lastName}")
                .to("jms:queue:sale-data");

        // POST to sales service.
        from("jms:queue:sale-data")
                .removeHeaders("*")
                .marshal().json(JsonLibrary.Gson)
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .to("http://localhost:8083/api/sales")
                .to("jms:queue:get-sale-summary");
//
//        GET sale summary of customer based on their ID.
        from("jms:queue:get-sale-summary")
                .removeHeaders("*")
                .setBody(constant(null))
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .recipientList()
                .simple("http://localhost:8083/api/sales/customer/${exchangeProperty.Customer_Id}/summary")
                .log("Customer Summary: ${body}")
                .to("jms:queue:summary-response");

        // Check to see if customer group needs to be updated.
        from("jms:queue:summary-response")
                .unmarshal().json(JsonLibrary.Gson, Summary.class)
                .log("Unmarshaled Customer Summary: ${body}")
                .bean(UpdateGroupCreator.class, "changeGroup(${body})")
                .log("Group field changed to vend ID group: ${body}")
                .choice()
                .when().simple("${body.group} == ${exchangeProperty.Customer_Group}")
                .log("Group does not need updating: ${body.group} does not equal ${exchangeProperty.Customer_Group}")
                .to("jms:queue:update-not-required")
                .otherwise()
                .log("Group must be updated: ${body.group} does not equal ${exchangeProperty.Customer_Group}")
                .to("jms:queue:update-customer-group");

        // Update customer group to "VIP Customers".
        from("jms:queue:update-customer-group")
                .log("Customer before updating: ${body}")
                .bean(UpdateCustomerCreator.class, "updateGroup(${exchangeProperty.Customer_Id},"
                        + "${exchangeProperty.Customer_FirstName}, "
                        + "${exchangeProperty.Customer_LastName}, "
                        + "${exchangeProperty.Customer_Email})")
                .log("Customer after updating: ${body}")
                .multicast()
                .to("jms:queue:put-vend","jms:queue:graphql");

        // Sends PUT request to Vend to change group of existing customer.
        from("jms:queue:put-vend")
                .log("${body.id}")
                .removeHeaders("*")
                .setHeader("Authorization", constant("Bearer KiQSsELLtocyS2WDN5w5s_jYaBpXa0h2ex1mep1a"))
                .marshal().json(JsonLibrary.Gson)
                .setHeader(Exchange.CONTENT_TYPE).constant("application/json")
                .setHeader(Exchange.HTTP_METHOD, constant("PUT"))
                .log("Send to vend to update: ${body}")
                .toD("https://info303otago.vendhq.com/api/2.0/customers/${exchangeProperty.Customer_Id}");
   
        
//        // Mutation of change group of existing customer to graphQL.
        from("jms:queue:graphql")
//        .setBody().jsonpath("$.data")
//        .marshal().json(JsonLibrary.Gson)
//        .unmarshal().json(JsonLibrary.Gson, Customer.class)
        .toD("graphql://http://localhost:8082/graphql?query=mutation{changeGroup(id:\"${exchangeProperty.Customer_Id}\", newGroup:\"${body.group}\") {id,email,username,firstName,lastName,group}}")
        .log("GraphQL service called");
    }

}
