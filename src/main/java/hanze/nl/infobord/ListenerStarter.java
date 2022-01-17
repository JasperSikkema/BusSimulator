package hanze.nl.infobord;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public  class ListenerStarter implements Runnable, ExceptionListener {
	private String selector="";
	
	public ListenerStarter() {
	}
	
	public ListenerStarter(String selector) {
		this.selector=selector;
	}

	public void run() {
        try {
            System.out.println("Produce, wait, consume"+ selector);
            connectieOpzetten().setMessageListener(new QueueListener(selector));
        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }

    private MessageConsumer connectieOpzetten() throws JMSException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        connection.setExceptionListener(this);
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createTopic("JSON_Berichten");
        return session.createConsumer(destination,selector);
    }

    public synchronized void onException(JMSException ex) {
        System.out.println("JMS Exception occured.  Shutting down client.");
    }
}