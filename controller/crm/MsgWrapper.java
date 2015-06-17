/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crm;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * Message-Wrapper für das SuiteCRM System (s.h. http://141.22.29.95/doc.html)
 *
 * @author le
 */
public class MsgWrapper {

    public static String createProductMsg(String product, String category) {
        JsonObject msg = Json.createObjectBuilder()
                .add("type", "anlegen")
                .add("inhalt", Json.createArrayBuilder()
                        .add(Json.createObjectBuilder()
                                .add("produkt", product)
                                .add("kategorie", category)))
                .build();

        return msg.toString();
    }

    public static String findSupplierMsg(String category, String product, int quantity) {
        JsonObject msg = Json.createObjectBuilder()
                .add("type", "bestellung")
                .add("inhalt", Json.createObjectBuilder()
                        .add("kategorie", category)
                        .add("produkt", product)
                        .add("menge", quantity))
                .build();

        return msg.toString();
    }
    
    public static String createSupplierMsg(){
    	
    	return "";
    }

    /**
     * Formats the incoming message containing the findSupplier() reply of the CRM
     * @param msg
     * @return msg[] for creating an invoice with Invoice Ninja
     */
    public static String[] createInvoiceMsg(String msg){
    	//TODO: Filter the the parameters for the invoiceMsg() function.
    	
    	return invoiceMsg("companyName", "itemNr", "product", "price", "quantity");
    }
    
    
    private static String[] invoiceMsg(String companyName, String itemNr, String product, String price, String quantity){
    	String[] msg  = new String[6];
    	msg[0] = "rechnung";
    	msg[1] = companyName;
    	msg[2] = itemNr;
    	msg[3] = product;
    	msg[4] = String.valueOf(price);
    	msg[5] = String.valueOf(quantity);
    	return msg;
    }
    
    
    // TODO: ggf. Antworten parsen und in einfachen String umwandeln
    public static void main(String[] args) {

        String createProduct = createProductMsg("Schrott", "Fahrradzubehoer");
        System.out.println(createProduct);

        String findSupplier = findSupplierMsg("Fahrradzubehoer", "Sattel Men", 10);
        System.out.println(findSupplier);
    }
    
    
}
