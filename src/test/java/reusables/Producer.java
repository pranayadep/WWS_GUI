package reusables;

import java.util.Properties;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import progress.message.jclient.Connection;
import progress.message.jclient.ConnectionFactory;
import progress.message.jclient.Queue;

public class Producer {

	public static void jmsCall(String ResPonseXM, String coId , String queueName)
			throws NamingException, JMSException, Exception {
		Thread.sleep(1000);
		// String coId = JDBC_Test.getCoid(connString);

		System.out.println("CO-Relation id is : " + coId);

		/*
		 * File loadFile = new File(filepath); StringBuffer fileContents = new
		 * StringBuffer();
		 * 
		 * @SuppressWarnings("resource") BufferedReader input = new
		 * BufferedReader(new FileReader(loadFile)); String line = null; while
		 * ((line = input.readLine()) != null) { Matcher junkMatcher =
		 * (Pattern.compile("^([\\W]+)<")).matcher(line.trim()); line =
		 * junkMatcher.replaceFirst("<"); fileContents.append(line); }
		 */
		String xmlFile = ResPonseXM;// fileContents.toString();

		Properties env = new Properties();
		env.put(Context.SECURITY_PRINCIPAL, "id995429");
		env.put(Context.SECURITY_CREDENTIALS, "WWS_id5429");
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sonicsw.jndi.mfcontext.MFContextFactory");
		// env.put(Context.PROVIDER_URL, "tcp://el2057.bc:2506");
		env.put(Context.PROVIDER_URL, "tcp://el2056.bc:2506,tcp://el2057.bc:2506");
		env.put("com.sonicsw.jndi.mfcontext.domain", "esb_d4");
		InitialContext uJNDI = new InitialContext(env);
		System.out.println("Context created successfully!!!");

		// lookup queue using queue name
		Queue queue = (Queue) uJNDI.lookup(queueName);
				//("gw.wws.response.wsl.customerpreordering_test.v1.in.q");// (ExcelUtil.getData("Credentials",
																									// "QueueName"));
		System.out.println("Queue lookup successful!!!");

		// create connection factory by passing the connection factory name
		ConnectionFactory conFactory = (ConnectionFactory) uJNDI.lookup("qcf_wws");
		System.out.println("connection factory created!!!");

		// create connection by passing the user name and password
		Connection uConnection = (Connection) conFactory.createConnection("id995429", "WWS_id5429");
		System.out.println("Connection created successfully!!");

		// created session
		Session session = uConnection.createSession(false, QueueSession.AUTO_ACKNOWLEDGE);

		// Start the connection
		uConnection.start();

		// create message producer by queue name variable
		MessageProducer producer = session.createProducer(queue);

		// create message, also here u can read you file and pass it as string
		TextMessage msg = session.createTextMessage(xmlFile);

		// set JMSCorrealtionID
		msg.setJMSCorrelationID(coId);

		// send message
		producer.send(msg);

		// Connection close
		producer.close();
		session.close();
		uConnection.close();
		// System.gc();
		System.out.println("All Connections closed");
		// System.exit(0);
		Thread.sleep(5000);

	}
}