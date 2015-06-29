/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crm;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 * Message-Wrapper für das SuiteCRM System
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

    public static String createSupplierMsg(String title, String anrede, String first_name,
            String last_name, String phone, String mobile, String mail,
            String strasse, String stadt, String plz, String bundesland, String staat) {
        JsonObject msg = Json.createObjectBuilder()
                .add("type", "lieferant_anlegen")
                .add("inhalt", Json.createArrayBuilder()
                        .add(Json.createObjectBuilder()
                                .add("title", title)
                                .add("anrede", anrede)
                                .add("first_name", first_name)
                                .add("last_name", last_name)
                                .add("phone", phone)
                                .add("mobile", mobile)
                                .add("mail", mail)
                                .add("strasse", strasse)
                                .add("stadt", stadt)
                                .add("plz", plz)
                                .add("bundesland", bundesland)
                                .add("staat", staat)))
                .build();

        return msg.toString();
    }

    /**
     * Formats the incoming message containing the findSupplier() reply of the
     * CRM
     *
     * @param msg
     * @return msg[] for creating an invoice with Invoice Ninja
     */
    public static String[] createInvoiceMsg(String msg) {
        //TODO: Filter the the parameters for the invoiceMsg() function.

        System.out.println("createInvoice(): empfangen: " + msg);
        if ("error".equalsIgnoreCase(msg)) {
            throw new IllegalArgumentException("Falsche Nachrichtenformat");
        }

        try (JsonReader jsonReader = Json.createReader(new StringReader(msg))) {

            JsonObject jsonObject = jsonReader.readObject();
            JsonObject company = jsonObject.getJsonObject("inhalt").getJsonArray("return")
                    .getJsonObject(0).getJsonObject("persoenliche Daten");
            JsonObject order = jsonObject.getJsonObject("inhalt").getJsonArray("return")
                    .getJsonObject(0).getJsonObject("bestellung");

            String companyName = company.getString("nachname");
            String itemNr = order.getString("productId");
            String product = jsonObject.getJsonObject("inhalt").getString("produkt");
            String price = order.getString("preis");
            String quantity = order.getString("menge");
            System.out.println("Vom sugar uebergebene Menge: " + quantity);

            return invoiceMsg(companyName, itemNr, product, price, quantity);
        }       
    }

    private static String[] invoiceMsg(String companyName, String itemNr, String product, String price, String quantity) {
        String[] msg = new String[6];
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

        String createProduct = createProductMsg("Sattel Men", "Fahrradzubehoer");
        System.out.println(createProduct);

        String findSupplier = findSupplierMsg("Fahrradzubehoer", "Sattel Men", 80);
        System.out.println(findSupplier);

        String createSupplier = createSupplierMsg("title", "anrede", "firstname", "lastname", "phone", "mobiel",
                "mail", "strasse", "stadt", "plz", "bundesland", "staat");
        System.out.println(createSupplier);

        String findSupplierReply = "{\"type\":\"bestellung\",\"inhalt\":{\"kategorie\":\"Fahrradzubehoer\",\"produkt\":\"Sattel Men\",\"menge\":\"80\",\"return\":[{\"persoenliche Daten\":{\"titel\":\"\",\"anrede\":\"Dr.\",\"vorname\":\"Hannah\",\"nachname\":\"M\\u00fcller\",\"telefonnr\":\"08935641131\",\"mobile\":\"017692698888 \",\"email\":\"hannah@mueller.de\"},\"adresse\":{\"strasse\":\"Rotkreuzplatz 11\",\"stadt\":\"M\\u00fcnchen\",\"plz\":\"80311\",\"bundesland\":\"Bayern\",\"staat\":\"Deutschland\"},\"bestellung\":{\"productId\":\"1131a5ab-b694-b6fe-d5ae-556431c58d0a\",\"menge\":\"30\",\"preis\":\"25.990000\"}}]}}";

        for (String arg : createInvoiceMsg(findSupplierReply)) {
            System.out.println(arg);
        }
        
    }
}
