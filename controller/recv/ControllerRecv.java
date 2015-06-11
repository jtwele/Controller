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

public class ControllerRecv {

	private final String CONTROLLER_QUEUE_INVOICE = "controllerInvoice";
	private final String CONTROLLER_QUEUE_SUGAR = "controllerSugar";
	private final String CONTROLLER_QUEUE_WAWISION = "controllerWaWision";
	private final String HOST = "141.22.29.97";
	private final String USER = "controller";
	private final String VHOST = "/";
	private MessageSender sender;

	private ConnectionFactory factory;
	private Connection connection;
	private Channel channel;
	private QueueingConsumer consumer;
	private String receivedMessage;
	private RecvThreadSugar rts;
	private RecvThreadWaWision rtw;
	private RecvThreadInvoice rti;

	public ControllerRecv() {
		this.sender = new MessageSender();
		this.rts = new RecvThreadSugar();
		this.rtw = new RecvThreadWaWision();
		this.rti = new RecvThreadInvoice();
	}

	private void receive(String queueName) throws IOException,
			ShutdownSignalException, ConsumerCancelledException,
			InterruptedException {
		this.factory = new ConnectionFactory();
		this.createConnection();
		this.declareQueue(queueName);
		this.receiveMessage(queueName);
	}

	private void receiveFromInvoice() throws InterruptedException, IOException {
		System.out.println("receiveFromInvoice()");
		this.receive(this.CONTROLLER_QUEUE_INVOICE);
	}

	private void receiveFromWaWision() throws InterruptedException, IOException {
		System.out.println("receiveFromWaWision()");
		this.receive(this.CONTROLLER_QUEUE_WAWISION);
	}

	private void receiveFromSugar() throws InterruptedException, IOException {
		System.out.println("receiveFromSugar()");
		this.receive(this.CONTROLLER_QUEUE_SUGAR);
	}

	public void start() {
		this.rts.start();
		this.rtw.start();
		this.rti.start();
	}

	public String getReceivedMessage() {
		return this.receivedMessage;
	}

	private void createConnection() throws IOException {
		this.factory = new ConnectionFactory();
		this.factory.setHost(this.HOST);
		this.factory.setUsername(this.USER);
		this.factory.setPassword(this.USER);
		this.factory.setVirtualHost(this.VHOST);
		this.connection = factory.newConnection();
		this.channel = connection.createChannel();
	}

	private void declareQueue(String queuename) throws IOException {
		this.channel.queueDeclare(queuename, false, false, false, null);
		consumer = new QueueingConsumer(channel);
		this.channel.basicConsume(queuename, true, consumer);
	}

	private void receiveMessage(String queuename)
			throws ShutdownSignalException, ConsumerCancelledException,
			InterruptedException, IOException {
		QueueingConsumer.Delivery delivery = null;
		while (true) {
			System.out.println("waiting for messages");
			delivery = consumer.nextDelivery();
			System.out.println("***************nachricht empfangen von : "+queuename+" ************");
			if (true/*delivery.getProperties().getCorrelationId().matches(sender.getMessageID())*/) {
				String message = new String(delivery.getBody());
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

	private class RecvThreadInvoice extends Thread {
		@Override
		public void run() {
			
				try {
					receiveFromInvoice();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
	}

	private class RecvThreadWaWision extends Thread {
		@Override
		public void run() {
			try {
				receiveFromWaWision();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private class RecvThreadSugar extends Thread {
		@Override
		public void run() {
			try {
				receiveFromSugar();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws InterruptedException,
			IOException {
		ControllerRecv cr = new ControllerRecv();
		cr.start();
	}

}
