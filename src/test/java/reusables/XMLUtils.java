package reusables;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLUtils {

	private static DocumentBuilderFactory factory;
	private static DocumentBuilder builder;
	private static Document doc;
	private static XPathFactory xpathfactory;
	private static XPathExpression expr;
	private static XPath xpath;
	private static NodeList nodes;
	private static Node node;

	// Parse XML file
	public static void buildXmlUtils(String xmlFilePath)
			throws ParserConfigurationException, FileNotFoundException, SAXException, IOException {
		factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		builder = factory.newDocumentBuilder();
		doc = builder.parse(new FileInputStream(new File(xmlFilePath)));
		// Get XPath expression
		xpathfactory = XPathFactory.newInstance();
		xpath = xpathfactory.newXPath();
		xpath.setNamespaceContext(new NamespaceResolver2(doc));
	}

	public static void compile(String Xpath) throws XPathExpressionException {
		expr = xpath.compile(Xpath + "/text()");
	}

	public static NodeList run() throws XPathExpressionException {
		nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		return nodes;
	}

	public static String getValue(NodeList nodes, int i) {
		node = nodes.item(i);
		return node.getNodeValue();
	}

	public static void setValue(Node nodes, String value) {
		node = nodes;
		node.setTextContent(value);
	}

	public static void saveXml(String file) throws TransformerException {
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.transform(new DOMSource(doc), new StreamResult(new File(file)));
	}

	public static String getURI(NodeList nodes, int i) throws XPathExpressionException {
		node = nodes.item(i);
		return node.getBaseURI();
	}

	public static Node prevSibling(NodeList nodes, int i) throws XPathExpressionException {
		node = nodes.item(i);
		return node.getPreviousSibling();
	}

	public static Node nextSibling(NodeList nodes, int i) throws XPathExpressionException {
		node = nodes.item(i);
		return node.getNextSibling();
	}

}

class NamespaceResolver2 implements NamespaceContext {

	private Document sourceDocument;

	public NamespaceResolver2(Document document) {
		sourceDocument = document;
	}

	// The lookup for the namespace uris is delegated to the stored document.
	public String getNamespaceURI(String prefix) {
		if (prefix.equals(XMLConstants.DEFAULT_NS_PREFIX)) {
			return sourceDocument.lookupNamespaceURI(null);
		} else {
			return sourceDocument.lookupNamespaceURI(prefix);
		}
	}

	public String getPrefix(String namespaceURI) {
		return sourceDocument.lookupPrefix(namespaceURI);
	}

	@SuppressWarnings("rawtypes")
	public Iterator getPrefixes(String namespaceURI) {
		return null;
	}
}
