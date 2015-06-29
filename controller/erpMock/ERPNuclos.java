package erpMock;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.AMQP.BasicProperties;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


import crm.MsgWrapper;

// Nachrichten an controllerWaWision Queue senden 
public class ERPNuclos {
	
	private Channel channel;
	private ConnectionFactory factory;
	private Connection connection;
	private String messageID;
	
	
	public ERPNuclos(String type){
		if(type.matches("bestellung")){			
			new ERPreceive2(this).start(); 
		}
	}

	
	/**
	 * Bearbeitet die einkommende Nachricht, die ursprünglich von sugar kommt.
	 * Es kann nur eine bestellung sein!!! Diese wird für InvoiceNinja geparst und versendet.
	 * @param msg
	 * @throws IOException 
	 */
	public void handleIncomingMessage(String msg) throws IOException{
			String[] message = MsgWrapper.createInvoiceMsg(msg);
			//this.send(message[0]+", "+message[1] + ", " +message[2] +", "+message[3]+", " +message[4]);
			this.send("FirmaBB"+", "+"12345" + ", " +"Lenker" +", "+"24,99"+", " +"7");
	}
	
	
	public void send(String message) throws IOException {
		this.setConnectionCredentials("141.22.29.97", "wawisionSender", "wawisionSender");
		this.createConnection();
		this.declareQueue("controllerWaWision");
		this.publish(message, "controllerWaWision");
		this.closeConnection();
	}
	
	
	private void createConnection() throws IOException {
		this.connection = factory.newConnection();
		this.channel = this.connection.createChannel();
		
	}

	private void declareQueue(String queueName) throws IOException {
		this.channel.queueDeclare(queueName, false, false, false, null);
	}

	private void setConnectionCredentials( String host, String username,
			String password) {
		this.factory = new ConnectionFactory();
		this.factory.setHost(host);
		this.factory.setUsername(username);
		this.factory.setPassword(password);
	}

	private void publish(String message, String queueName) throws IOException {
		//TODO: MessageID setzen!!!
		this.messageID = "123";
		BasicProperties props =  new BasicProperties().builder().correlationId(this.messageID).build();
		this.channel.basicPublish("", queueName, props, message.getBytes());
	}

	private void closeConnection() throws IOException {
		this.channel.close();
		this.connection.close();
		System.out.println("****** Nachricht wurde versendet ********");
	}
	
	public static void main(String[] args) throws IOException{
		final ERPNuclos erp = new ERPNuclos(args[0]);
		/*
		 * erp Nachrichten: Bestellung
		 * 
		 *  erstelle Rechnung
		 *  
		 *  erstelle Lieferanten
		 */

		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						ServerSocket serv = new ServerSocket(9000);
						System.out.println("lausche");
						Socket sock = serv.accept();
						BufferedReader read = new BufferedReader(new InputStreamReader(sock.getInputStream()));

						String s = "";
						String buff = read.readLine();
						while (buff != null) {
						//	buff = read.readLine();
							if (buff != null && !buff.equals("null")) {
								s = s + buff;
								break;
							}
						}
						System.out.println(s);
						erp.send(s);
						read.close();
						serv.close();

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();		
		
	}
	
}
