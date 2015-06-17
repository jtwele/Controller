package messages;

public class Messagefactory {
	
	/**
	 * This method creates two types of messages. 
	 * CreateClient(); benoetigte Parameter: { 
	 * Firmenname, Kontakt: Vorname, Nachname, Mail, Telefon, Strasse, Stadt, Bundesland, PLZ, Land }
	 * createInvoice(); benoetigte Parameter: { Productkey, Produktbeschreibung, Preis, Menge }
	 * 
	 * @param  String[] message, String msgType
	 * @return for InvoiceNinja converted message
	 */
	
	public static String CreateInvoiceMessage(String[] message){
		String msg = "";
		for(int i = 0; i<message.length;i++){
			msg+=message[i]+ ", "; 
		}
		return msg;
	}
	
	
}
