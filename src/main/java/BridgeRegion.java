
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.network.jms.InboundTopicBridge;
import org.apache.activemq.network.jms.OutboundTopicBridge;
import org.apache.activemq.network.jms.SimpleJmsTopicConnector;

public class BridgeRegion {

    public BridgeRegion(String localport, String remotePort) throws Exception {


        SimpleJmsTopicConnector jmsTopicConnectorRegion = null;

        jmsTopicConnectorRegion = new SimpleJmsTopicConnector();

        jmsTopicConnectorRegion.setOutboundTopicBridges(new OutboundTopicBridge[]{new OutboundTopicBridge("event.agent"),new OutboundTopicBridge("event.region")});
        jmsTopicConnectorRegion.setInboundTopicBridges(new InboundTopicBridge[]{new InboundTopicBridge("event.global")});

        jmsTopicConnectorRegion.setOutboundTopicConnectionFactory(new ActiveMQConnectionFactory("tcp://localhost:" + remotePort));
        //jmsTopicConnectorRegion.setLocalTopicConnectionFactory(new ActiveMQConnectionFactory("tcp://localhost:" + localport));
        jmsTopicConnectorRegion.setLocalTopicConnectionFactory(new ActiveMQConnectionFactory("vm://localhost"));

        jmsTopicConnectorRegion.start();


        while (!jmsTopicConnectorRegion.isConnected()) {
            System.out.println("Not connected");
            Thread.sleep(1000);
        }

    }



}