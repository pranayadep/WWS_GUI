package reusables;

import java.io.IOException;
import java.time.LocalDate;

import javax.swing.JOptionPane;

public class test {

	public static void main(String args[]) throws IOException {
		try {
			String name = "pranay:adep";
			String arr[] = null;
			arr = name.split(":");
			System.out.println(arr[0]);

			JOptionPane.showMessageDialog(null, "To Continue Press OK.");
			
			String test = "pranayadep.xml";
			System.out.println(test.length());
			System.out.println(LocalDate.now());
			
			System.out.println(test.trim().length());
			
			System.out.println(test.substring(test.length() - 4));
			
			
			
			

			// Code to make a webservice HTTP request
			/*
			 * File loadFile = new File("H:/XMLs/Soap_Test.xml"); StringBuffer
			 * fileContents = new StringBuffer();
			 * 
			 * @SuppressWarnings("resource") BufferedReader input = new
			 * BufferedReader(new FileReader(loadFile)); String line = null;
			 * while ((line = input.readLine()) != null) { Matcher junkMatcher =
			 * (Pattern.compile("^([\\W]+)<")).matcher(line.trim()); line =
			 * junkMatcher.replaceFirst("<"); fileContents.append(line); }
			 * String xmlValue = fileContents.toString(); ByteArrayOutputStream
			 * bout = new ByteArrayOutputStream(); byte[] buffer = new
			 * byte[xmlValue.length()]; buffer = xmlValue.getBytes();
			 * bout.write(buffer); byte[] b = bout.toByteArray(); String
			 * responseString = ""; String outputString = ""; String wsURL =
			 * "http://10.119.174.210:9080/SaveIncomingReqResp"; URL url = new
			 * URL(wsURL); URLConnection connection = url.openConnection();
			 * HttpURLConnection httpConn = (HttpURLConnection) connection;
			 * 
			 * String SOAPAction =
			 * "http://10.119.174.210:9080/SaveIncomingReqResp"; // Set the
			 * appropriate HTTP parameters.
			 * httpConn.setRequestProperty("Content-Length",
			 * String.valueOf(b.length));
			 * httpConn.setRequestProperty("Content-Type",
			 * "text/xml; charset=utf-8");
			 * httpConn.setRequestProperty("SOAPAction", SOAPAction);
			 * httpConn.setRequestMethod("POST"); httpConn.setDoOutput(true);
			 * httpConn.setDoInput(true); OutputStream out =
			 * httpConn.getOutputStream(); // Write the content of the request
			 * to the outputstream of the HTTP // Connection. out.write(b);
			 * out.close();
			 * 
			 * // Read the response. InputStreamReader isr = new
			 * InputStreamReader(httpConn.getInputStream()); BufferedReader in =
			 * new BufferedReader(isr);
			 * 
			 * // Write the SOAP message response to a String. while
			 * ((responseString = in.readLine()) != null) { outputString =
			 * outputString + responseString; }
			 * 
			 * System.out.println(outputString);
			 */
		}

		catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
}
