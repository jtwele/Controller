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
					System.out.println("receiveMessage(): controllerSugar");
					this.barbeiteSugarNachricht(message, messageID);
					break;
				case "controllerWaWision":
					this.barbeiteWaWisionNachricht(message, messageID);
					break;
				default:
					System.out.println("etwas ist schiefgelaufen");
					break;
				}
			}
		

	}

	private void barbeiteSugarNachricht(String nachricht, String messageID) throws IOException {
		String[] msg = nachricht.split(" ");
		System.out.println("BearbeiteSugarNachricht(): Nachrichtentyp: " +msg[0] );
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
		System.out.println("nachricht: " + nachricht);
		switch(msg[0]){
			case "anlegen": // Produkt anlegen
				sender.sendToSugar(MsgWrapper.createProductMsg(msg[1], msg[2]), messageID);
				break;
			case("neu"): //Lieferant anlegen
				sender.sendToInvoice(Messagefactory.CreateInvoiceMessage(msg), messageID);
				sender.sendToSugar(MsgWrapper.createSupplierMsg(), messageID);
				break;
			case("bestellung")://Lieferant(-en) für Bestellung suchen
				msg[3] = msg[3].trim();
			System.out.println("**************** " + msg[3] + " *********************");
				sender.sendToSugar(MsgWrapper.findSupplierMsg(msg[1], msg[2], Integer.valueOf(msg[3])), messageID);
				break;
			case("rechnung"): //rechnung erstellen
				System.out.println("bearbeite nachricht: " + nachricht + "\n");
//				sender.sendToInvoice("rechnung, FirmaCC, 567, Lenker, 24.99, 7s", this.sender.getMessageID());
				sender.sendToInvoice(Messagefactory.CreateInvoiceMessage(msg), messageID);
				break;
		}
	}

	public static void main(String[] args) throws InterruptedException,
			IOException {
		Controller cr = new Controller();
		cr.start();
		
	}

}
