package parse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.Element;

public class makeCollection {
	String originFilePath = "/Users/jodaehyeon/Desktop/SimpleIR/";
	String dataPath="/Users/jodaehyeon/Desktop/SimpleIR/SearchIR/src/data";
	String collectionXml = "collection.xml";
	String indexXml = "index.xml";
	DocumentBuilderFactory docFactory;
	DocumentBuilder docBuilder;
	org.w3c.dom.Document document;
	org.w3c.dom.Document document2;
	TransformerFactory tfFactory;
	Transformer tf;
	
	File htmlFiles;
	
	makeCollection() throws ParserConfigurationException, TransformerConfigurationException {
		docFactory = DocumentBuilderFactory.newInstance();
		docBuilder = docFactory.newDocumentBuilder();
		document = docBuilder.newDocument();
		document2 = docBuilder.newDocument();
		tfFactory = TransformerFactory.newInstance();
		tf = tfFactory.newTransformer();
		tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		//htmlFiles = new File(dataPath);
	}
	makeCollection(String path) throws TransformerConfigurationException, ParserConfigurationException{
		this();
		htmlFiles = new File(path);
	}
	void htmlToXml() throws IOException, TransformerException {
		int i = 0;
		Element docs = document.createElement("docs");
		document.appendChild(docs);
		for (File f : htmlFiles.listFiles()) {
			Document fileDoc = Jsoup.parse(f, "UTF-8");
			Element doc = document.createElement("doc");
			doc.setAttribute("id", Integer.toString(i++));
			docs.appendChild(doc);

			Element title = document.createElement("title");
			title.appendChild(document.createTextNode(fileDoc.select("title").text().toString()));
			doc.appendChild(title);

			Element body = document.createElement("body");
			body.appendChild(document.createTextNode(fileDoc.body().text().toString()));
			doc.appendChild(body);
		}
		makeXMLFile(document,originFilePath,collectionXml);
	}
	
	 void makeXMLFile(org.w3c.dom.Document document,String s,String e)
			throws FileNotFoundException, TransformerException {
		DOMSource ds = new DOMSource(document);
		StreamResult result = new StreamResult(new FileOutputStream(new File(s + e)));
		tf.transform(ds, result);
	}
}
