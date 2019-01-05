import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSslConnectionFactory;

import javax.jms.*;
import java.security.SecureRandom;
import java.util.UUID;

public class ActiveProducerWorker {
	private String producerWorkerName;
	private Session sess;
	private ActiveMQConnection conn;
	private ActiveMQConnectionFactory connf;

	private MessageProducer producer;
	public boolean isActive;
	private String queueName;
	private Destination destination;
	
	public ActiveProducerWorker(String TXQueueName, String URI, String brokerUserNameAgent, String brokerPasswordAgent)  {

		this.producerWorkerName = UUID.randomUUID().toString();
		try {
			queueName = TXQueueName;
			connf = new ActiveMQConnectionFactory(URI);

			//Don't serialize VM connections
			if(URI.startsWith("vm://")) {
				connf.setObjectMessageSerializationDefered(true);
			}
			conn = (ActiveMQConnection) connf.createConnection();
			conn.start();
			sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			//destination = sess.createQueue(TXQueueName);
			destination = sess.createTopic(TXQueueName);

			producer = sess.createProducer(destination);
			producer.setTimeToLive(300000L);
			//producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);


			isActive = true;

		} catch (Exception e) {

		}
	}


	public boolean sendMessage(String message) {
		try {

			//releaseYear
			TextMessage tm = sess.createTextMessage(message);
			tm.setIntProperty("releaseYear",1977);

			producer.send(tm, DeliveryMode.NON_PERSISTENT, 0, 0);

			return true;
		} catch (JMSException jmse) {
			jmse.printStackTrace();
			return false;
		}
	}
}