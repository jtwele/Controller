package send;

import java.io.IOException;


public class Send {

	public static void main(String[] args) {

		MessageSender sender = new MessageSender();

		try{
		sender.sendToInvoice("Nachricht an InvoiceNinja gesendet");
		sender.sendToSugar("Nachricht an InvoiceNinja gesendet");
		sender.sendToWaWision("Nachricht an InvoiceNinja gesendet");
		}catch( IOException e ){
		}

	}

}
