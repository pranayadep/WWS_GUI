package reusables;

import java.util.Properties;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import progress.message.jclient.Queue;

public class MessageReceiver {
	public static String reqXML = "";

	public static String getReq(String queName, String corrId) throws NamingException, JMSException, Exception {

		Properties env = new Properties();
		env.put(Context.SECURITY_PRINCIPAL, "id995429");
		env.put(Context.SECURITY_CREDENTIALS, "WWS_id5429");
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sonicsw.jndi.mfcontext.MFContextFactory");
		// env.put(Context.PROVIDER_URL, "tcp://el2057.bc:2506");
		env.put(Context.PROVIDER_URL, "tcp://el2056.bc:2506,tcp://el2057.bc:2506");
		env.put("com.sonicsw.jndi.mfcontext.domain", "esb_d4");
		InitialContext uJNDI = new InitialContext(env);
		System.out.println("Context created successfully!!!");

		// lookup queue using queue name - "gw.wws.out_test.q"
		Queue queue = (Queue) uJNDI.lookup(queName);
		System.out.println("Queue lookup successful!!!");

		// create connection factory by passing the connection factory name
		QueueConnectionFactory conFactory = (QueueConnectionFactory) uJNDI.lookup("qcf_wws");
		System.out.println("connection factory created!!!");

		// create connection by passing the user name and password
		QueueConnection uConnection = conFactory.createQueueConnection("id995429", "WWS_id5429");
		System.out.println("Connection created successfully!!");

		// created session
		QueueSession session = uConnection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);

		QueueReceiver receiver = session.createReceiver(queue, "JMSCorrelationID='" + corrId + "'");

		// 5) create listener object

		// Start the connection
		uConnection.start();
		System.out.println("Connection Started");
		// PrintStream out = new PrintStream(new
		// FileOutputStream("C:\\Users\\id848699\\Desktop\\1\\filename.xml"));

		try {
			Message m = receiver.receive(100);
			if (m != null) {
				TextMessage message = (TextMessage) m;
				reqXML = message.getText().toString();
				// out.print(message.getText().toString());
				System.out.println("Reading message: " + message.getText().toString());
			} else {
				reqXML = "";
				System.out.println("Message is not present in the out queue");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		session.close();
		uConnection.close();
		// System.gc();
		System.out.println("All Connections closed");
		return reqXML;
	}

	public static void main(String args[]) {
		try {
			getReq("gw.mcom.event_out_test.q", "EOR:EOR-2431:SubmitCustOrder:WSL:1");
			System.exit(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}