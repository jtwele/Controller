package messages;

public class Messagefactory {
	
	/**
	 * This method creates two types of messages. 
	 * CreateClient(); benštigte Parameter: { 
	 * Firmenname, Kontakt: Vorname, Nachname, Mail, Telefon, Stra§e, Stadt, Bundesland, PLZ, Land }
	 * createInvoice(); benštigte Parameter: { Productkey, Produktbeschreibung, Preis, Menge }
	 * 
	 * @param  String[] message, String msgType
	 * @return for InvoiceNinja converted message
	 */
	
	public static String CreateInvoiceMessage(String[] message){
		String msg = "";
		for(int i = 0; i<message.length;i++){
			msg+=message[i]+ " "; 
		}
		return msg;
	}
	
	
	public static String suiteCreate(String[] message){
		return message[0];
	}
	
	public static String suiteCreateFindSupplierString(String[] findSupplier){
		//TODO: Richtig formatieren.
		return "{\n"
				+ "\"type\": \""+findSupplier[0]+"\","
				+ "\n\"inhalt\": {"
				+ "\n\t\"kategorie\": \""+findSupplier[1]+"\","
				+ "\n\t\"produkt\": \""+findSupplier[2]+"\","
				+ "\n\t\"menge\": "+findSupplier[3]+" "
				+ "\n\t}"
				+ "\n}";
	}
}
