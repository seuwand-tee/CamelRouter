/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Account;

import domain.Account;
import domain.Customer;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import Creator.CustomerCreator;

/**
 *
 * @author User
 */
public class AccountBuilder extends RouteBuilder{
    @Override
    public void configure() throws Exception { 
        
        // Unmarshals the message received into an Account object.
        from("jetty:http://localhost:8090/api/newAccount?enableCORS=true")
           .setExchangePattern(ExchangePattern.InOnly)
           .log("Account created: ${body}")
           .unmarshal().json(JsonLibrary.Gson, Account.class)
           .log("Send to create-account queue: ${body}")
           .to("jms:queue:create-account");
        
        // Converts the Account object to a Customer object.
        from("jms:queue:create-account")
            .bean(CustomerCreator.class, "createCustomer(${body})")
            .log("Send to vend queue: ${body}")
            .to("jms:queue:vend");
        
        // Sends the JSON Customer object to Vend.
        from("jms:queue:vend")
            .log("Received Customer pre-marshal: ${body}")
            .removeHeaders("*")
            .setHeader("Authorization", constant("Bearer KiQSsELLtocyS2WDN5w5s_jYaBpXa0h2ex1mep1a"))
            .marshal().json(JsonLibrary.Gson)  
            .setHeader(Exchange.CONTENT_TYPE).constant("application/json")
            .setHeader(Exchange.HTTP_METHOD, constant("POST"))
            .log("Send to vend: ${body}")
            .to("https://info303otago.vendhq.com/api/2.0/customers")
            .to("jms:queue:vend-response");     
        
        // Extracts Customer response and converts back to Account object.
        from("jms:queue:vend-response")
                .setBody().jsonpath("$.data")
                .marshal().json(JsonLibrary.Gson)
                .unmarshal().json(JsonLibrary.Gson, Customer.class)
                .log("${body}")
                .log("${body.id}")
                .log("${body.email}")
                .log("${body.customerCode}")
                .log("${body.firstName}")
                .log("${body.lastName}")
                .log("${body.group}")
            .to("jms:queue:graphql"); 
        
        // Mutation of add customer to graphQL.        
        from("jms:queue:graphql")
	.toD("graphql://http://localhost:8082/graphql?query=mutation{addAccount(account:{id:\"${body.id}\", email:\"${body.email}\", , username:\"${body.customerCode}\", firstName:\"${body.firstName}\", lastName:\"${body.lastName}\", group:\"${body.group}\"}){id,email,username,firstName,lastName,group}}")
	.log("GraphQL service called");
    }
}
