package controller.send;

public class Send {

	public static void main(String[] args) {
		
		/*
		 * IN = invoiceNinja
		 * WW = WaWision
		 * SG = Sugar
		 */
		MessageSender sender = new MessageSender();
		//sender.sendMessage("IN", "Nachricht an InvoiceNinja gesendet");
		sender.sendMessage("WW", "Nachricht an WaWision gesendet");
		//sender.sendMessage("SG", "Nachricht an Sugar gesendet");
		

	}

}
