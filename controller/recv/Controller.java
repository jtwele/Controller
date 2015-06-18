package recv;

import java.io.IOException;

import messages.Messagefactory;
import send.MessageSender;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

import crm.MsgWrapper;

public class Controller {
	
	private MessageSender sender;
	private String receivedMessage;

	public Controller(){
		this.sender = new MessageSender();
	}

	public void start() throws ShutdownSignalException, ConsumerCancelledException, IOException, InterruptedException{
		new Receiver("controllerInvoice", this).start();;
		new Receiver("controllerSugar", this).start();;
		new Receiver("controllerWaWision", this).start();;
	}

	public String getReceivedMessage() {
		return this.receivedMessage;
	}


	public synchronized void receiveMessage(String queuename, String message, String messageID) throws IOException{

			System.out.println("***************Nachricht empfangen von : "+queuename+" ************");
			if (true/*delivery.getProperties().getCorrelationId().matches(sender.getMessageID())*/) {
				switch (queuename) {
				case "controllerSugar":
					this.barbeiteSugarNachricht(message, sender.getMessageID());
					break;
				case "controllerWaWision":
					this.barbeiteWaWisionNachricht(message, sender.getMessageID());
					break;
				default:
					System.out.println("etwas ist schiefgelaufen");
					break;
				}
			}
		

	}

	private void barbeiteSugarNachricht(String nachricht, String messageID) throws IOException {
		String[] msg = nachricht.split(" ");
		if(msg[0].matches("bestellung")){
			// TODO: Nachricht für WaWision parsen. Wird erstmal nicht gemacht, sondern nur durchgereicht 
			sender.sendToWaWision(nachricht, messageID);
		}

	}

	/*Ankommendes Format:
	 
	  "create Firmenname2 "
	+ "Vorname2 Nachname2 Mail@mail.com Telefon2 "
	+ "Strasse2 Stadt2 Bundesland2 PLZ2 Land2" 
	
	*/
	private void barbeiteWaWisionNachricht(String nachricht, String messageID) throws IOException {
		String[] msg = nachricht.split(", ");
		
		switch(msg[0]){
			case "anlegen": // Produkt anlegen
				//sender.sendToSugar(MsgWrapper.createProductMsg(msg[1], msg[2]), this.sender.getMessageID());
				break;
			case("neu"): //Lieferant anlegen
				System.out.println("nachricht: " + nachricht);
				sender.sendToInvoice(Messagefactory.CreateInvoiceMessage(msg), sender.getMessageID());
				//sender.sendToSugar(MsgWrapper.createSupplierMsg(), messageID);
				break;
			case("bestellung")://Lieferant(-en) für Bestellung suchen
				//	sender.sendToSugar(MsgWrapper.findSupplierMsg(msg[1], msg[2], Integer.valueOf(msg[3])), this.sender.getMessageID());
				break;
			case("rechnung"): //rechnung erstellen
				sender.sendToInvoice(Messagefactory.CreateInvoiceMessage(msg), this.sender.getMessageID());
				break;
		}
	}

	public static void main(String[] args) throws InterruptedException,
			IOException {
		Controller cr = new Controller();
		cr.start();
		
	}

}
