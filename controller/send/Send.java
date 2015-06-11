package send;

import java.io.IOException;




public class Send {

	public static void main(String[] args) {

		MessageSender sender = new MessageSender();

		try{

		sender.sendToInvoice("Mustermann 1234 0401234567 Musterstrasse Musterstadt Musterstate 12345 DE Mustermann ogulcan.kurtul@haw-hamburg.de Max Mustermann 0401234567", null);
//		sender.sendToSugar("Wer das liest ist doof");
//		sender.sendToWaWision("haloooooooooo");
		}catch( IOException e ){
		}

	}

}
