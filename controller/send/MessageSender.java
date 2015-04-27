package controller.send;

import java.io.IOException;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class MessageSender {

	private final String INVOICE_QUEUE = "invoice";
	private final String WAWISION_QUEUE = "waWision";
	private final String SUGAR_QUEUE = "sugar";
	
	private final String INVOICE_HOST = "141.22.29.97";
	private final String WAWISION_HOST = "141.22.29.96";
	private final String SUGAR_HOST = "141.22.29.95";
	
	private final String INVOICE_USER = "invoice";
	private final String WAWISION_USER = "wawision";
	private final String SUGAR_USER = "sugar";
	
	private final String INVOICE_PW = "invoice";
	private final String WAWISION_PW = "wawision";
	private final String SUGAR_PW = "sugar";
	
	private Channel channel;
	private ConnectionFactory factory;
	private Connection connection;
	
	public MessageSender() {
		this.factory  =new ConnectionFactory();
	}


	public void sendMessage(String ziel, String nachricht) {
		switch (ziel) {
		case "IN":
			setConnectionCredentials(INVOICE_HOST, INVOICE_USER, INVOICE_PW);
			this.createConnection();
			this.declareQueue(INVOICE_QUEUE);
			sendToInvoiceNinja(nachricht);
			break;
		case "WW":
			setConnectionCredentials(WAWISION_HOST, WAWISION_USER, WAWISION_PW);
			this.createConnection();
			this.declareQueue(WAWISION_QUEUE);
			sendToWawision(nachricht);
			break;
		case "SG":
			setConnectionCredentials(SUGAR_HOST, SUGAR_USER, SUGAR_PW);
			this.createConnection();
			this.declareQueue(SUGAR_QUEUE);
			sendToSugarCrm(nachricht);
			break;
		default:
			break;
		}
//		closeConnection();
	}
	
	
	private void createConnection() {
		try {
			this.connection = factory.newConnection();
		} catch (IOException e) {
			System.out.println("Verbindung erstellen fehlgeschlagen");
		}
	    try {
			this.channel = connection.createChannel();
		} catch (IOException e) {
			System.out.println("Create Channel fehlgeschlagen");
		}
	}

	private void declareQueue(String queueName) {
		try {
			channel.queueDeclare(queueName, false, false, false, null);
		} catch (IOException e) {
			System.out.println("Channel declaration fehlgeschlagen");
		}	
	}
	

	private void setConnectionCredentials(String host, String username, String password) {
		this.factory = new ConnectionFactory();
		factory.setHost(host);
		factory.setUsername(username);
		factory.setPassword(password);
	}

	private void sendToInvoiceNinja(String message) {
		try {
			channel.basicPublish("", INVOICE_QUEUE, null, message.getBytes());
		} catch (IOException e) {
			System.out.println("Nachrichte an Invoice Ninja fehlgeschlagen");
		}
	}

	private void sendToWawision(String message) {
		try {
			channel.basicPublish("", WAWISION_QUEUE, null, message.getBytes());
		} catch (IOException e) {
			System.out.println("Nachrichte an WaWision fehlgeschlagen");
		}
	}

	private void sendToSugarCrm(String message) {
		try {
			channel.basicPublish("", SUGAR_QUEUE, null, message.getBytes());
		} catch (IOException e) {
			System.out.println("Nachrichte an Sugar-CRM fehlgeschlagen");
		}
	}
	
}
