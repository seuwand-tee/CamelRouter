/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Creator;

import domain.Account;
import domain.Customer;

/**
 *
 * @author User
 */
public class CustomerCreator {
    public Customer createCustomer(Account account){
        Customer customer = new Customer();
        customer.setCustomerCode(account.getUsername());
        customer.setFirstName(account.getFirstName());
        customer.setLastName(account.getLastName());
        customer.setGroup("0afa8de1-147c-11e8-edec-2b197906d816");
        customer.setEmail(account.getEmail());
        
        // Returns a new Customer object.
        return customer;
    }
}
