package reusables;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class XMlGetValue {

	public static String[] run(String xmlPath, String path) {

		try {
			// Parse XML file
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new FileInputStream(new File(xmlPath)));

			// Get XPath expression
			XPathFactory xpathfactory = XPathFactory.newInstance();
			XPath xpath = xpathfactory.newXPath();

			String xpaths[] = path.split(",");
			String xpathValues[] = new String[xpaths.length];

			for (int i = 0; i < xpaths.length; i++) {
				XPathExpression expr = xpath.compile(xpaths[i] + "/text()");
				System.out.println("xpath is " + xpaths[i]);
				
				NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
				System.out.println(nodes.item(0).getNodeValue());
				xpathValues[i] = nodes.item(0).getNodeValue();
			}
			return xpathValues;

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		}

	}

	public static String[] main(String inputXml, String xpath) throws Exception {
		// remove namespace of the xml
		XMLNameSpaceRemoverTarget.main("removeNs.xslt", inputXml,
				System.getProperty("user.home") + "/Desktop/output.xml");
		return run(System.getProperty("user.home") + "/Desktop/output.xml", xpath);
	}

}
