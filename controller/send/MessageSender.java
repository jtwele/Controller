package send;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
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
	private String messageID;

	public MessageSender() {
		this.factory = new ConnectionFactory();
	}

	public void sendToInvoice(String nachricht, String messageID) throws IOException {
		this.send(INVOICE_HOST, INVOICE_USER, INVOICE_PW, INVOICE_QUEUE,
				nachricht, messageID);
	}

	public void sendToSugar(String nachricht, String messageID) throws IOException {
		this.send(SUGAR_HOST, SUGAR_USER, SUGAR_PW, SUGAR_QUEUE, nachricht, messageID);
	}

	public void sendToWaWision(String nachricht, String messageID) throws IOException {
		this.send(WAWISION_HOST, WAWISION_USER, WAWISION_PW,
				WAWISION_QUEUE, nachricht, messageID);
	}

	private void send(String host, String username, String password,
			String queuename, String nachricht, String messageID) throws IOException {
		System.out.println("send()");
		this.setConnectionCredentials(host, username, password);
		this.createConnection();
		this.declareQueue(queuename);
		this.publish(nachricht, queuename, messageID);
		this.closeConnection();

	}

	private void createConnection() throws IOException {
		this.connection = factory.newConnection();
		System.out.println("connection" + this.connection);
		this.channel = this.connection.createChannel();
	}

	private void declareQueue(String queueName) throws IOException {
		System.out.println("declareQueue()");
		this.channel.queueDeclare(queueName, false, false, false, null);
		System.out.println("ENDE declareQueue()");
	}

	private void setConnectionCredentials(String host, String username,
			String password) {
		System.out.println("setConnectionCerentials()");
		this.factory = new ConnectionFactory();
		this.factory.setHost(this.INVOICE_HOST);
		this.factory.setUsername(username);
		this.factory.setPassword(password);
	}

	@SuppressWarnings("static-access")
	private void publish(String message, String queueName, String messageID) throws IOException {
		
		if(messageID.matches(null) /*|| andere Bedingungen*/){
			this.messageID = java.util.UUID.randomUUID().toString();
		}else{
			this.messageID = messageID;
		}
		
		BasicProperties props =  new BasicProperties().builder().correlationId(this.messageID).build();
		this.channel.basicPublish("", queueName, props, message.getBytes());
	}

	private void closeConnection() throws IOException {
		this.channel.close();
		this.connection.close();
	}

	public String getMessageID() {
		return messageID;
	}
	

}
