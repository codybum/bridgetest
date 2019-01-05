import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class ActiveAgentConsumer {
	private Topic RXqueue;
	private Session sess;
	private ActiveMQConnection conn;
	private ActiveMQConnectionFactory connf;

	public ActiveAgentConsumer(String RXQueueName, String URI, String brokerUserNameAgent, String brokerPasswordAgent) throws JMSException {
		int retryCount = 10;

		connf = new ActiveMQConnectionFactory(URI);
		//Don't serialize VM connections
		if(URI.startsWith("vm://")) {
			connf.setObjectMessageSerializationDefered(true);
		}

		conn = (ActiveMQConnection) connf.createConnection();
		conn.start();
		sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		RXqueue = sess.createTopic(RXQueueName);
		MessageConsumer consumer = sess.createConsumer(RXqueue, "releaseYear < 1980");
		//MessageConsumer consumer = session.createConsumer(queue, "releaseYear < 1980");

		consumer.setMessageListener(new MessageListener() {
			public void onMessage(Message msg) {
				try {

					if (msg instanceof TextMessage) {

						//System.out.println(RXQueueName + " msg:" + ((TextMessage) msg).getText());

						String message = ((TextMessage) msg).getText();

					} else {
						System.out.println("non-Text message recieved!");
					}
				} catch(Exception ex) {

					ex.printStackTrace();
				}
			}
		});


	}

}