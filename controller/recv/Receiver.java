package recv;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

import send.MessageSender;

public class Receiver extends Thread{

	private final String queuename;
	private final String HOST = "141.22.29.97";
	private final String USER = "controller";
	private final String VHOST = "/";

	private ConnectionFactory factory;
	private Connection connection;
	private Channel channel;
	private QueueingConsumer consumer;
	private Controller controller;
	
	public Receiver(String queuename, Controller controller) throws ShutdownSignalException, ConsumerCancelledException, IOException, InterruptedException{
		this.queuename=queuename;
		this.controller = controller;
		
	}
	
	@Override
	public void run(){
		try {
			this.receive();
		} catch (ShutdownSignalException | ConsumerCancelledException
				| IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void receive() throws IOException,
			ShutdownSignalException, ConsumerCancelledException,
			InterruptedException {
		this.factory = new ConnectionFactory();
		this.createConnection();
		this.declareQueue(this.queuename);
		this.receiveMessage(this.queuename);
	}
	
	private void receiveMessage(String queueName) throws ShutdownSignalException, ConsumerCancelledException, InterruptedException, IOException {
		System.out.println("Warte auf Nachrichten von " + this.queuename);
		QueueingConsumer.Delivery delivery = this.consumer.nextDelivery();
		this.controller.receiveMessage(this.queuename, new String(delivery.getBody()), delivery.getProperties().getCorrelationId());
		
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

}
