package send;

import java.io.IOException;


public class Send {

	public static void main(String[] args) {
		
		/*
		 * IN = invoiceNinja
		 * WW = WaWision
		 * SG = Sugar
		 */
		MessageSender sender = new MessageSender();

		try{
		sender.sendMessage("WW", "Nachricht an WaWision gesendet");
		//sender.sendMessage("IN", "Nachricht an InvoiceNinja gesendet");
		//sender.sendMessage("SG", "Nachricht an Sugar gesendet");
		}catch( IOException e ){
		}

	}

}
