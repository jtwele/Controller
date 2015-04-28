package send;

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


	public void sendMessage(String ziel, String nachricht)throws IOException{
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
	
	
	private void createConnection()throws IOException{ 
			this.connection = factory.newConnection();
			System.out.println("connection"+this.connection);
			this.channel =this.connection.createChannel();
	}

	private void declareQueue(String queueName)throws IOException {
			this.channel.queueDeclare(queueName, false, false, false, null);
	}
	

	private void setConnectionCredentials(String host, String username, String password) {
		this.factory = new ConnectionFactory();
		this.factory.setHost("localhost");
		this.factory.setUsername(username);
		this.factory.setPassword(password);
	}

	private void sendToInvoiceNinja(String message) throws IOException{
			channel.basicPublish("", INVOICE_QUEUE, null, message.getBytes());
	}

	private void sendToWawision(String message)throws IOException{
		System.out.println("Sende Nachricht an WaWision");	
		channel.basicPublish("", WAWISION_QUEUE, null, message.getBytes());
	}

	private void sendToSugarCrm(String message)throws IOException{
			channel.basicPublish("", SUGAR_QUEUE, null, message.getBytes());
	}
	
}
