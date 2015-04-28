package recv;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class MessageReceiverSugar {

	private final String SUGAR_QUEUE = "sugar";

	private ConnectionFactory factory;
	private Connection connection;
	private Channel channel;
	private QueueingConsumer consumer;
	private String receivedMessage;

	public MessageReceiverSugar() throws IOException, InterruptedException {
		System.out.println("initialisiert");
		this.receive();
	}

	public void receive() throws InterruptedException, IOException {
		System.out.println("receive()");
		this.factory = new ConnectionFactory();
		this.createConnection();
		this.declareQueue();
		this.receiveMessage();
	}

	public String getReceivedMessage() {
		return this.receivedMessage;
	}

	private void createConnection() throws IOException {
		System.out.println("create connection");
		this.factory = new ConnectionFactory();
		this.factory.setHost("141.22.29.97");
		this.factory.setUsername("sugar");
		this.factory.setPassword("sugar");
		this.factory.setVirtualHost("/");
		this.connection = factory.newConnection();
		this.channel = connection.createChannel();
	}

	private void declareQueue() throws IOException {
		System.out.println("declare queue");
		this.channel.queueDeclare(SUGAR_QUEUE, false, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		consumer = new QueueingConsumer(channel);
		this.channel.basicConsume(SUGAR_QUEUE, true, consumer);
	}

	private void receiveMessage() throws ShutdownSignalException,
			ConsumerCancelledException, InterruptedException {
		System.out.println("receive Message");
		QueueingConsumer.Delivery delivery = null;
		while (true) {
			System.out.println("waiting for messages");
			delivery = consumer.nextDelivery();
			this.receivedMessage = new String(delivery.getBody());
			System.out.println(this.receivedMessage);
		}

	}

	public static void main(String[] args) {

		try {
			MessageReceiverSugar mrsg = new MessageReceiverSugar();
		} catch (InterruptedException | IOException e) {

		}
	}

}
