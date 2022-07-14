
var customerApi = 'http://localhost:8090/api/newAccount';
 
const app = Vue.createApp({
 
data() {
    return {
        account: new Object()
    };
},

methods: {
 addAccount() {
    axios.post(customerApi, this.account)
        .then(() => {
            // update the catalogue
            alert("Account created.");
            })
        .catch(error => {
            console.error(error);
            alert("An error occurred - check the console for details.");
        });
}}
 
});
 
// mount the page at the <main> tag - this needs to be the last line in the file
app.mount("main");/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


