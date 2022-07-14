/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Creator;

import domain.Summary;

/**
 *
 * @author User
 */
public class UpdateGroupCreator {
     public Summary changeGroup(Summary summary) {
        Summary s = new Summary();
        s.setNumberOfSales(summary.getNumberOfSales());
        s.setTotalPayment(summary.getTotalPayment());
        if(summary.getGroup().equals("Regular Customers")){
            s.setGroup("0afa8de1-147c-11e8-edec-2b197906d816");
        } else {
            s.setGroup("0afa8de1-147c-11e8-edec-201e0f00872c");
        } 
        return s;
    }
}
