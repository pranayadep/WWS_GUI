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

public class ValidateTageValue {

	public static boolean run(String xmlPath, String path, String value) {

		try {
			// Parse XML file
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new FileInputStream(new File(xmlPath)));

			// Get XPath expression
			XPathFactory xpathfactory = XPathFactory.newInstance();
			XPath xpath = xpathfactory.newXPath();

			String xpathValue = "";

			XPathExpression expr = xpath.compile(path + "/text()");
			NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			System.out.println(nodes.item(0).getNodeValue());
			xpathValue = nodes.item(0).getNodeValue();

			if (xpathValue.equalsIgnoreCase(value)) {
				return true;
			} else
				return false;

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;

		}

	}

	public static void main(String inputXml, String xpath, String value) throws Exception {
		// remove namespace of the xml
		XMLNameSpaceRemoverTarget.main("removeNs.xslt", inputXml,
				System.getProperty("user.home") + "/Desktop/output.xml");
		run(System.getProperty("user.home") + "/Desktop/output.xml", xpath, value);
	}

}
