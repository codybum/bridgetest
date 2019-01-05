
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.network.jms.InboundTopicBridge;
import org.apache.activemq.network.jms.OutboundTopicBridge;
import org.apache.activemq.network.jms.SimpleJmsTopicConnector;

public class BridgeAgent {

    public BridgeAgent(String localport, String remotePort) throws Exception {

        //ActiveMQConnection connection = ActiveMQConnection.makeConnection("tcp://localhost:" + remotePort);
        //connection.start();

        /*
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue("event");
        MessageProducer producer = session.createProducer(destination);
        producer.send(session.createTextMessage("Test Message"));
        System.out.println("send message");
        session.close();
        connection.close();
        */

        //InboundTopicBridge topicBridge = new InboundTopicBridge();
        //OutboundTopicBridge outbountTopicBridge = new OutboundTopicBridge();
        SimpleJmsTopicConnector jmsTopicConnector = null;


        //localConnectionFactory = createLocalConnectionFactory();
        //foreignConnectionFactory = createForeignConnectionFactory();

        //Topic outbound = new ActiveMQTopic("RECONNECT.TEST.OUT.TOPIC");
        //Topic inbound = new ActiveMQTopic("RECONNECT.TEST.IN.TOPIC");

        //Topic outbound = new ActiveMQTopic("event");
        //Topic inbound = new ActiveMQTopic("event");


        jmsTopicConnector = new SimpleJmsTopicConnector();

        // Wire the bridges.
        //jmsTopicConnector.setOutboundTopicBridges(new OutboundTopicBridge[]{new OutboundTopicBridge("RECONNECT.TEST.OUT.TOPIC")});
        //jmsTopicConnector.setInboundTopicBridges(new InboundTopicBridge[]{new InboundTopicBridge("RECONNECT.TEST.IN.TOPIC")});
        jmsTopicConnector.setOutboundTopicBridges(new OutboundTopicBridge[]{new OutboundTopicBridge("event.agent")});
        jmsTopicConnector.setInboundTopicBridges(new InboundTopicBridge[]{new InboundTopicBridge("event.region"),new InboundTopicBridge("event.global")});



        // Tell it how to reach the two brokers.
        //jmsTopicConnector.setOutboundTopicConnectionFactory(new ActiveMQConnectionFactory("tcp://localhost:61617"));
        //jmsTopicConnector.setLocalTopicConnectionFactory(new ActiveMQConnectionFactory("tcp://localhost:61616"));

        jmsTopicConnector.setOutboundTopicConnectionFactory(new ActiveMQConnectionFactory("tcp://localhost:" + remotePort));
        //jmsTopicConnector.setLocalTopicConnectionFactory(new ActiveMQConnectionFactory("tcp://localhost:" + localport));
        jmsTopicConnector.setLocalTopicConnectionFactory(new ActiveMQConnectionFactory("vm://localhost"));

        jmsTopicConnector.start();


        while (!jmsTopicConnector.isConnected()) {
            System.out.println("Not connected");
            Thread.sleep(1000);
        }



    }



}