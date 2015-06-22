package erpMock;

import java.io.IOException;

import recv.Controller;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class ERPreceive extends Thread{
	
	private final String queuename = "waWision";
	private final String HOST = "141.22.29.97";
	private final String USER = "wawision";
	private final String VHOST = "/";

	private ConnectionFactory factory;
	private Connection connection;
	private Channel channel;
	private QueueingConsumer consumer;
	private ERPMock erp;
	
	public ERPreceive(ERPMock erp){
		this.erp = erp;
	}


	@Override
	public void run() {
		try {
			this.receive();
		} catch (ShutdownSignalException | ConsumerCancelledException
				| IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void receive() throws IOException, ShutdownSignalException,
			ConsumerCancelledException, InterruptedException {
		this.factory = new ConnectionFactory();
		this.createConnection();
		this.declareQueue();
		this.receiveMessage();
	}

	private void receiveMessage()
			throws ShutdownSignalException, ConsumerCancelledException,
			InterruptedException, IOException {
		while(true){
			QueueingConsumer.Delivery delivery = this.consumer.nextDelivery();		
			this.erp.handleIncomingMessage(new String(delivery.getBody()));
			System.out.println("Incoming message: " + new String(delivery.getBody()));
		}
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

	private void declareQueue() throws IOException {
		this.channel.queueDeclare(this.queuename, false, false, false, null);
		consumer = new QueueingConsumer(channel);
		this.channel.basicConsume(this.queuename, true, consumer);
	}


}
