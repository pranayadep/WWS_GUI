package reusables;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import testScripts.ExcelRead;

public class SendWSL {

	public static void jmsCall(String xmlValue, String queName, String corrId)
			throws NamingException, JMSException, Exception {
		/*
		 * String xmlVal[] = xmlValue.split("-"); if (xmlVal[1].equals("file"))
		 * { File loadFile = new File(xmlVal[0]); StringBuffer fileContents =
		 * new StringBuffer();
		 * 
		 * @SuppressWarnings("resource") BufferedReader input = new
		 * BufferedReader(new FileReader(loadFile)); String line = null; while
		 * ((line = input.readLine()) != null) { Matcher junkMatcher =
		 * (Pattern.compile("^([\\W]+)<")).matcher(line.trim()); line =
		 * junkMatcher.replaceFirst("<"); fileContents.append(line); } xmlValue
		 * = fileContents.toString(); }
		 */
		System.out.println("Send below xml to WSL : " + xmlValue);

		Properties env = new Properties();
		env.put(Context.SECURITY_PRINCIPAL, "id995079");
		env.put(Context.SECURITY_CREDENTIALS, "WSL_id5079");
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sonicsw.jndi.mfcontext.MFContextFactory");
		// env.put(Context.PROVIDER_URL, "tcp://el2057.bc:2506");
		env.put(Context.PROVIDER_URL, "tcp://el2056.bc:2506,tcp://el2057.bc:2506");
		env.put("com.sonicsw.jndi.mfcontext.domain", "esb_d4");
		InitialContext uJNDI = new InitialContext(env);
		System.out.println("Context created successfully!!!");

		// lookup queue using queue name
		Queue queue = (Queue) uJNDI.lookup(queName);// (ExcelUtil.getData("Credentials",
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
		TextMessage msg = session.createTextMessage(xmlValue);

		// set JMSCorrealtionID
		msg.setJMSCorrelationID(corrId);

		// send message
		producer.send(msg);
		
		System.out.println(session.getAcknowledgeMode());

		// Connection close
		producer.close();
		session.close();
		uConnection.close();
		// System.gc();
		System.out.println("All Connections closed");
		// System.exit(0);
		Thread.sleep(3000);

	}

	public static void main(String args[]) {

		String queName = "gw.wsl.response.mpc.productoffercandidate_reg.v1.in.q";
		String xmlString = "H:/XMLs/MPC_Product_res.xml";
		String coId = "CHK:CHK-10584:ValidateProduct:MPC:5";

		try {
			if (xmlString.equalsIgnoreCase("req")) {
				xmlString = ExcelRead.receivedXML;
			} else if (xmlString.contains("TEMP")) {
				xmlString = ExcelRead.runTimeVar.get(xmlString);
			} else if (xmlString.contains(".xml")) {
				File loadFile = new File(xmlString);
				System.out.println("in conversion");
				StringBuffer fileContents = new StringBuffer();
				@SuppressWarnings("resource")
				BufferedReader input = new BufferedReader(new FileReader(loadFile));
				String line = null;
				while ((line = input.readLine()) != null) {
					Matcher junkMatcher = (Pattern.compile("^([\\W]+)<")).matcher(line.trim());
					line = junkMatcher.replaceFirst("<");
					fileContents.append(line);
				}
				xmlString = fileContents.toString();
			}
			System.out.println(queName + " : " + coId);
			SendWSL.jmsCall(xmlString, queName, coId);
			System.exit(0);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}