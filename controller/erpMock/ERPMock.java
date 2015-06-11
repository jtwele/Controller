package erpMock;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.AMQP.BasicProperties;

// Nachrichten an controllerWaWision Queue senden 
public class ERPMock {
	
	private Channel channel;
	private ConnectionFactory factory;
	private Connection connection;
	private String messageID;
	
	public void sendCreateSupllierMsg(String message) throws IOException{
		this.send(message);
	}

	public void sendFindSupplier(String message) throws IOException{
			this.send(message);
	}
	

	private void send(String message) throws IOException {
		this.setConnectionCredentials("141.22.29.97", "controller", "controller");
		this.createConnection();
		this.declareQueue("controllerWaWision");
		this.publish(message, "controllerWaWision");
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

	private void setConnectionCredentials( String host, String username,
			String password) {
		System.out.println("setConnectionCerentials()");
		this.factory = new ConnectionFactory();
		this.factory.setHost(host);
		this.factory.setUsername(username);
		this.factory.setPassword(password);
	}

	private void publish(String message, String queueName) throws IOException {
		//TODO: MessageID setzen!!!
		BasicProperties props =  new BasicProperties().builder().correlationId(this.messageID).build();
		this.channel.basicPublish("", queueName, props, message.getBytes());
	}

	private void closeConnection() throws IOException {
		this.channel.close();
		this.connection.close();
	}
	
	public static void main(String[] args) throws IOException{
		ERPMock erp = new ERPMock();
		
		// String zum erstellen eines Lieferanten bauen 
		erp.sendCreateSupllierMsg("create Firmenname "
				+ "Vorname Nachname Mail Telefon "
				+ "Strasse Stadt Bundesland PLZ Land");
		/*
		 *  sugar brauch: 
		 *  NachrichtenTyp (bestellung), kategorie (Fahrradzubehoer), produkt (Sattel), menge (13)
		 */
		 //erp.sendFindSupplier("bestellung Fahradzubehoer Sattel 13");

		
	}
	
}
