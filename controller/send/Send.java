package send;

import java.io.IOException;

import messages.Messagefactory;


public class Send {

	public static void main(String[] args) {

		MessageSender sender = new MessageSender();

		try{
<<<<<<< HEAD
	//	sender.sendToInvoice("Nachricht an InvoiceNinja gesendet");
//		sender.sendToSugar("Nachricht an Sugar gesendet");
		
		sender.sendToWaWision("Nachricht an WaWision gesendet");
=======
		sender.sendToInvoice("Mustermann 1234 0401234567 Musterstrasse Musterstadt Musterstate 12345 DE Mustermann ogulcan.kurtul@haw-hamburg.de Max Mustermann 0401234567");
//		sender.sendToSugar("Wer das liest ist doof");
//		sender.sendToWaWision("haloooooooooo");
>>>>>>> b520f412b2a584752b40f4f2b3d8677c0a52dfb1
		}catch( IOException e ){
		}

	}

}
