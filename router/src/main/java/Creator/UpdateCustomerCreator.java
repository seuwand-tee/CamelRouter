/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Creator;

import domain.Customer;

/**
 *
 * @author User
 */
public class UpdateCustomerCreator {
     public Customer updateGroup(String id, String firstName, String lastName, String email){
        Customer customer = new Customer();
        customer.setId(id);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setGroup("0afa8de1-147c-11e8-edec-201e0f00872c");
        customer.setEmail(email);
        
        // Returns a new Customer object.
        return customer;
    }
}
