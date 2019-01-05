import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import org.apache.activemq.broker.jmx.ManagementContext;
import org.apache.activemq.broker.region.policy.PolicyEntry;
import org.apache.activemq.broker.region.policy.PolicyMap;
import org.apache.activemq.broker.util.LoggingBrokerPlugin;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.network.NetworkConnector;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class ActiveBroker {
	private TransportConnector connector;
	public BrokerService broker;

	public ActiveBroker(String brokerName) {

		try {


				PolicyEntry entry = new PolicyEntry();
		        entry.setGcInactiveDestinations(true);
		        entry.setInactiveTimeoutBeforeGC(15000);

				ManagementContext mc = new ManagementContext();
				mc.setSuppressMBean("endpoint=dynamicProducer,endpoint=Consumer");


				PolicyMap map = new PolicyMap();
		        map.setDefaultEntry(entry);

				broker = new BrokerService();
				broker.setUseShutdownHook(true);
				broker.setPersistent(false);
				broker.setBrokerName(brokerName);
				broker.setSchedulePeriodForDestinationPurge(2500);
				broker.setDestinationPolicy(map);
				broker.setManagementContext(mc);
				//broker.setSslContext(sslContextBroker);
				broker.setPopulateJMSXUserID(true);
				broker.setUseAuthenticatedPrincipalForJMSXUserID(true);


				LoggingBrokerPlugin lbp = new LoggingBrokerPlugin();
				lbp.setLogAll(false);
				lbp.setLogConnectionEvents(false);
				lbp.setLogConsumerEvents(false);
				lbp.setLogProducerEvents(false);
				lbp.setLogInternalEvents(false);
				lbp.setLogSessionEvents(false);
				lbp.setLogTransactionEvents(false);
				lbp.setPerDestinationLogger(false);


				//broker.setPlugins(new BrokerPlugin[]{lbp});
				//LoggingBrokerPlugin
				//LoggingBrokerPlugin
				/*
				broker.setUseJmx(true);
				broker.getManagementContext().setConnectorPort(2099);
				broker.getManagementContext().setCreateConnector(true);
                */

				//authorizationPlugin = new CrescoAuthorizationPlugin();
				//authenticationPlugin = new CrescoAuthenticationPlugin();
				//broker.setPlugins(new BrokerPlugin[]{authorizationPlugin,authenticationPlugin});
				//<amq:transportConnector uri="ssl://localhost:61616" />

                /*
                connector.setUpdateClusterClients(true);
                connector.setRebalanceClusterClients(true);
                connector.setUpdateClusterClientsOnRemove(true);
                */

				//broker.addConnector(connector);

			/*
			broker.start();

			while (!broker.isStarted()) {
				Thread.sleep(1000);
			}
			*/


		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public void startBroker() {

		try {
			broker.start();

			while (!broker.isStarted()) {
				Thread.sleep(1000);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void addConnection(String port) {

		try {

			int discoveryPort = Integer.parseInt(port);

			if(portAvailable(discoveryPort)) {

				//connector = new TransportConnector();
				//connector.setUri(new URI("tcp://0.0.0.0:" + discoveryPort));
				connector = broker.addConnector(new URI("tcp://0.0.0.0:" + discoveryPort));
				//connector.start();

			}

		} catch(Exception ex) {
			ex.printStackTrace();
		}


	}

    public NetworkConnector AddNetworkConnectorURI(String URI) {
        NetworkConnector bridge = null;
        try {
            bridge = broker.addNetworkConnector(new URI(URI));
            bridge.setName(java.util.UUID.randomUUID().toString());
            bridge.setDuplex(true);
            //bridge.setDynamicOnly(true);
            //bridge.setStaticBridge(true);
            bridge.setPrefetchSize(1);


            //bridge.addDynamicallyIncludedDestination();
            //ActiveMQDestination activeMQDestination = ActiveMQDestination.createDestination("agent",ActiveMQDestination.QUEUE_TYPE);



        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return bridge;
    }


    public void printDest() {
		try {

			ActiveMQDestination[] activeMQDestinations = broker.getDestinations();


			if(activeMQDestinations != null) {
				for (ActiveMQDestination activeMQDestination : activeMQDestinations) {
					System.out.println(activeMQDestination.getPhysicalName());
				}
			}


		} catch(Exception ex) {
		ex.printStackTrace();
		}

	}

	public ActiveMQDestination createTopicDest(String topicName) {

		return ActiveMQDestination.createDestination("topic://" + topicName, ActiveMQDestination.TOPIC_TYPE);

	}

    public List<ActiveMQDestination> createQueueDest(String agentPath) {
		List<ActiveMQDestination> dstList = new ArrayList<>();
		ActiveMQDestination dst = ActiveMQDestination.createDestination("queue://" + agentPath, ActiveMQDestination.QUEUE_TYPE);
		dstList.add(dst);
		return dstList;
	}


	public void AddTransportConnector(String URI) {
		try {
			TransportConnector connector = new TransportConnector();
			connector.setUri(new URI(URI));

			this.broker.addConnector(connector);
			this.broker.startTransportConnector(connector);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public boolean portAvailable(int port) {
		if (port < 0 || port > 65535) {
			throw new IllegalArgumentException("Invalid start port: " + port);
		}

		ServerSocket ss = null;
		DatagramSocket ds = null;
		try {
			ss = new ServerSocket(port);
			ss.setReuseAddress(true);
			ds = new DatagramSocket(port);
			ds.setReuseAddress(true);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally  {
			if (ds != null)  {
				ds.close();
			}

			if (ss != null) {
				try {
					ss.close();
				} catch (IOException e)  {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
}