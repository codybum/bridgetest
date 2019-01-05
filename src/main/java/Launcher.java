import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.network.NetworkConnector;

public class Launcher {

    public static ActiveBroker activeBroker;

    public static void main(String args[]) {

        try {


            activeBroker = new ActiveBroker("mybroker");

            int mode = Integer.parseInt(args[0]);

            String port = args[1];
            String remotePort = args[2];

            String URI = "vm://localhost";


            if(mode == 0) {

                activeBroker.addConnection(port);
                activeBroker.startBroker();

                //consumer



                ActiveAgentConsumer activeAgentConsumer = new ActiveAgentConsumer("event.agent", URI, null, null);
                ActiveAgentConsumer ractiveAgentConsumer = new ActiveAgentConsumer("event.region", URI, null, null);
                ActiveAgentConsumer gactiveAgentConsumer = new ActiveAgentConsumer("event.global", URI, null, null);


                ActiveProducerWorker apw = new ActiveProducerWorker("event.agent", URI, null, null);
                ActiveProducerWorker rpw = new ActiveProducerWorker("event.region", URI, null, null);
                ActiveProducerWorker gpw = new ActiveProducerWorker("event.global", URI, null, null);


                while(true) {

                    //apw.sendMessage("global");
                    //rpw.sendMessage("global");
                    //gpw.sendMessage("global");

                    //activeBroker.printDest();

                    Thread.sleep(1000);
                }

            } else if(mode ==1) {

                activeBroker.addConnection(port);
                activeBroker.startBroker();


                //do nothing
                //BridgeRegion bridgeRegion = new BridgeRegion(port,remotePort);

                NetworkConnector networkConnector = activeBroker.AddNetworkConnectorURI("static:tcp://localhost:" + remotePort);

                networkConnector.addDynamicallyIncludedDestination(activeBroker.createTopicDest("event.agent"));
                networkConnector.addDynamicallyIncludedDestination(activeBroker.createTopicDest("event.region"));

                networkConnector.start();



                ActiveAgentConsumer activeAgentConsumer = new ActiveAgentConsumer("event.agent", URI, null, null);
                ActiveAgentConsumer ractiveAgentConsumer = new ActiveAgentConsumer("event.region", URI, null, null);
                ActiveAgentConsumer gactiveAgentConsumer = new ActiveAgentConsumer("event.global", URI, null, null);


                ActiveProducerWorker apw = new ActiveProducerWorker("event.agent", URI, null, null);
                ActiveProducerWorker rpw = new ActiveProducerWorker("event.region", URI, null, null);
                ActiveProducerWorker gpw = new ActiveProducerWorker("event.global", URI, null, null);


                while(true) {

                    /*
                    apw.sendMessage("region");
                    rpw.sendMessage("region");
                    gpw.sendMessage("region");

                    activeBroker.printDest();
                    */
                    Thread.sleep(1000);
                }


            } else if(mode ==2)  {

                activeBroker.startBroker();

                //BridgeAgent bridgeAgent = new BridgeAgent(port,remotePort);
                NetworkConnector networkConnector = activeBroker.AddNetworkConnectorURI("static:tcp://localhost:" + remotePort);

                networkConnector.addDynamicallyIncludedDestination(activeBroker.createTopicDest("event.agent"));

                networkConnector.start();



                ActiveAgentConsumer activeAgentConsumer = new ActiveAgentConsumer("event.agent", URI, null, null);
                ActiveAgentConsumer ractiveAgentConsumer = new ActiveAgentConsumer("event.region", URI, null, null);
                ActiveAgentConsumer gactiveAgentConsumer = new ActiveAgentConsumer("event.global", URI, null, null);


                ActiveProducerWorker apw = new ActiveProducerWorker("event.agent", URI, null, null);
                ActiveProducerWorker rpw = new ActiveProducerWorker("event.region", URI, null, null);
                ActiveProducerWorker gpw = new ActiveProducerWorker("event.global", URI, null, null);

                while(true) {

                        /*
                        apw.sendMessage("agent");
                        rpw.sendMessage("agent");
                        gpw.sendMessage("agent");

                        Thread.sleep(1000);

                      */

                }

            } else if(mode ==3)  {

                String topicName = args[3];

                activeBroker.startBroker();

                //BridgeAgent bridgeAgent = new BridgeAgent(port,remotePort);
                NetworkConnector networkConnector = activeBroker.AddNetworkConnectorURI("static:tcp://localhost:" + remotePort);

                networkConnector.addDynamicallyIncludedDestination(activeBroker.createTopicDest(topicName));

                networkConnector.start();



                ActiveAgentConsumer activeAgentConsumer = new ActiveAgentConsumer(topicName, URI, null, null);

                ActiveProducerWorker apw = new ActiveProducerWorker(topicName, URI, null, null);

                while(true) {

                        /*
                        apw.sendMessage("agent");
                        rpw.sendMessage("agent");
                        gpw.sendMessage("agent");

                        Thread.sleep(1000);

                      */

                }

            }





        } catch(Exception ex) {
            ex.printStackTrace();
        }


    }

    private static String createDataSize(int msgSize) {
        StringBuilder sb = new StringBuilder(msgSize);
        for (int i=0; i<msgSize; i++) {
            sb.append('a');
        }
        return sb.toString();
    }


}
