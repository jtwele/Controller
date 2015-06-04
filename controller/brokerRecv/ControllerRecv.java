package brokerRecv;

import java.io.IOException;

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
	
	private ConnectionFactory factory;
	private Connection connection;
	private Channel channel;
	private QueueingConsumer consumer;
	private String receivedMessage;
	private RecvThreadSugar rts;
	private RecvThreadWaWision rtw;
	private RecvThreadInvoice rti;
	

	
	
	public ControllerRecv(){
		System.out.println("Initialisiert");
		this.rts = new RecvThreadSugar();
		this.rtw = new RecvThreadWaWision();
		this.rti = new RecvThreadInvoice();
	}

	private void receive(String queueName) throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException{
		System.out.println("receive()");
		this.factory = new ConnectionFactory();
		this.createConnection();
		this.declareQueue(queueName);
		this.receiveMessage();
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
		this.receive(this.CONTROLLER_QUEUE_SUGAR );
	}

	public void start(){
		System.out.println("start()");
		this.rts.run();
		this.rtw.run();
		this.rti.run();
	}
	
	public String getReceivedMessage() {
		return this.receivedMessage;
	}

	private void createConnection() throws IOException {
		System.out.println("create connection");
		this.factory = new ConnectionFactory();
		this.factory.setHost(this.HOST);
		this.factory.setUsername(this.USER);
		this.factory.setPassword(this.USER);
		this.factory.setVirtualHost(this.VHOST);
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
	private class RecvThreadInvoice extends Thread{
		@Override
		public void run(){
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

	private class RecvThreadWaWision extends Thread{
		@Override
		public void run(){
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
	
	private class RecvThreadSugar extends Thread{
		@Override
		public void run(){
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
	
	public static void main(String[] args) throws InterruptedException, IOException {

			ControllerRecv cr = new ControllerRecv();
			cr.start();

	}

}
