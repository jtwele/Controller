package brokerRecv;

import java.io.IOException;

import recv.MessageReceiverInvoice;

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

	private ConnectionFactory factory;
	private Connection connection;
	private Channel channel;
	private QueueingConsumer consumer;
	private String receivedMessage;

	
	
	

	private void receive(String queueName) throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException{
		System.out.println("receive()");
		this.factory = new ConnectionFactory();
		this.createConnection();
		this.declareQueue(queueName);
		this.receiveMessage();
	}
	public void receiveFromInvoice() throws InterruptedException, IOException {
		this.receive(this.CONTROLLER_QUEUE_INVOICE);
	}
	public void receiveFromWaWision() throws InterruptedException, IOException {
		this.receive(this.CONTROLLER_QUEUE_WAWISION);
	}
	public void receiveFromSugar() throws InterruptedException, IOException {
		this.receive(this.CONTROLLER_QUEUE_SUGAR );
	}

	public String getReceivedMessage() {
		return this.receivedMessage;
	}

	private void createConnection() throws IOException {
		System.out.println("create connection");
		this.factory = new ConnectionFactory();
		this.factory.setHost("141.22.29.97");
		this.factory.setUsername("controller");
		this.factory.setPassword("controller");
		this.factory.setVirtualHost("/");
		this.connection = factory.newConnection();
		this.channel = connection.createChannel();
	}

	private void declareQueue(String queuename) throws IOException {
		System.out.println("declare queue");
		this.channel.queueDeclare(queuename, false, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		consumer = new QueueingConsumer(channel);
		this.channel.basicConsume(queuename, true, consumer);
	}

	private void receiveMessage() throws ShutdownSignalException,
			ConsumerCancelledException, InterruptedException {
		int i = 0;
		System.out.println("receive Message");
		QueueingConsumer.Delivery delivery = null;
		while (true) {
			System.out.println("waiting for messages");
			delivery = consumer.nextDelivery();
			this.receivedMessage = new String(delivery.getBody());
			System.out.println((++i)+"."+"empfangene nachricht: "+this.receivedMessage);
		}

	}

	public static void main(String[] args) throws InterruptedException, IOException {

			ControllerRecv cr = new ControllerRecv();
			
//			cr.receiveFromInvoice();
			cr.receiveFromSugar();
//			cr.receiveFromWaWision();

	}

}
