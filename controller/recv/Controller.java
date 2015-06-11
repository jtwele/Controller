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
				case "controllerInvoice":
					this.barbeiteInvoiceNachricht(message, sender.getMessageID());
					break;
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


	private void barbeiteInvoiceNachricht(String nachricht, String messageID) {
		// TODO: hier wird vorraussichtlich keine nachricht kommen.
		// Hoechsten ein Acknowledge

	}

	private void barbeiteSugarNachricht(String nachricht, String messageID) {
		/*
		 * hier kommt dann ein lieferant an mit menge, preis, ... 
		 * Aus den Daten muss dann eine RG. erstellt werden
		 */

	}

	private void barbeiteWaWisionNachricht(String nachricht, String messageID) throws IOException {
		String[] msg = nachricht.split(" ");
		if (msg[0].matches("create"))/* ein Lieferant soll angelegt werden */{
			sender.sendToInvoice(Messagefactory.CreateInvoiceMessage(msg), messageID);
			
			//this.sender.sendToInvoice(Messagefactory.CreateInvoiceMessage(nachricht, messageID));
		}else if(msg[0].matches("bestellung")){
				sender.sendToSugar(Messagefactory.suiteCreateFindSupplierString(msg), messageID);
		}
	}

	public static void main(String[] args) throws InterruptedException,
			IOException {
		Controller cr = new Controller();
		cr.start();
		
	}

}
