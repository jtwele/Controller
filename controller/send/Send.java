package send;

import java.io.IOException;


public class Send {

	public static void main(String[] args) {

		MessageSender sender = new MessageSender();

		try{
	//	sender.sendToInvoice("Nachricht an InvoiceNinja gesendet");
//		sender.sendToSugar("Nachricht an Sugar gesendet");
		sender.sendToWaWision("Nachricht an WaWision gesendet");
		}catch( IOException e ){
		}

	}

}
