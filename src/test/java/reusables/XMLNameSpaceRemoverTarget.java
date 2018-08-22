package reusables;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.swing.JOptionPane;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XMLNameSpaceRemoverTarget {

	static String xsltFilePath, inputXMlFile, outXMLFile;

	public static void main(String xsltPath, String inputFile, String outFile) throws Exception {

		/*
		 * You can integrate this code to a GUI and use it as a tool
		 * -------------- Credits :Pranay --------------
		 */

		try {
			xsltFilePath = xsltPath; // "D:/XMLs/removeNs.xslt";
			inputXMlFile = inputFile; // "D:/XMLs/CDB_XML.xml";
			outXMLFile = outFile; // "D:/XMLs/output.xml";
			String convertInputFile;

			if (inputXMlFile.contains(".xml")) {
				convertInputFile = convert(inputXMlFile);
				Thread.sleep(1000);
				TransformerFactory factory = TransformerFactory.newInstance();
				Source xslt = new StreamSource(new File(xsltFilePath));
				Transformer transformer = factory.newTransformer(xslt);
				Source text = new StreamSource(new File(convertInputFile));
				transformer.transform(text, new StreamResult(new File(outXMLFile)));
			} else {
				convertInputFile = inputXMlFile;
				Path pathXMLFile = Paths.get(outXMLFile + "1");
				Files.write(pathXMLFile, convertInputFile.getBytes(), StandardOpenOption.TRUNCATE_EXISTING,
						StandardOpenOption.WRITE, StandardOpenOption.CREATE);
				TransformerFactory factory = TransformerFactory.newInstance();
				Source xslt = new StreamSource(new File(xsltFilePath));
				Transformer transformer = factory.newTransformer(xslt);
				Source text = new StreamSource(new File(outXMLFile + "1"));
				transformer.transform(text, new StreamResult(new File(outXMLFile)));
			}
			System.out.println("Done");
			Thread.sleep(1000);

		} catch (TransformerConfigurationException e) {
			JOptionPane.showMessageDialog(null, e);
			e.printStackTrace();
		} catch (TransformerException e) {
			JOptionPane.showMessageDialog(null, e);
			e.printStackTrace();
		}

	}

	public static String convert(String inputFile) throws Exception {
		FileInputStream fis = new FileInputStream(inputFile);
		InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
		Reader in = new BufferedReader(isr);
		String outputFile = "C:\\Users\\" + System.getProperty("user.name") + "\\Desktop\\prematureOutput.xml";
		FileOutputStream fos = new FileOutputStream(outputFile);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		Writer out = new BufferedWriter(osw);

		int ch;
		while ((ch = in.read()) > -1) {
			out.write(ch);
		}
		out.close();
		in.close();
		return outputFile;
	}

}
