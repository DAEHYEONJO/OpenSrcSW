package parse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.Element;

public class Main {

	public static void main(String[] args) throws ParserConfigurationException, IOException, TransformerException {
		String filepath="/Users/jodaehyeon/Desktop/SimpleIR/2주차 실습 html";
		
		File files=new File(filepath);
		
		DocumentBuilderFactory docFactory=DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder=docFactory.newDocumentBuilder();
		
		org.w3c.dom.Document document=docBuilder.newDocument();
		
		Element docs=document.createElement("docs");
		Element doc;
		Element title;
		Element body;
		document.appendChild(docs);
		
		int i=0;
		
		for(File file:files.listFiles()) {
			Document fileDoc=Jsoup.parse(file,"UTF-8");
			doc=document.createElement("doc");
			doc.setAttribute("id", Integer.toString(i++));
			docs.appendChild(doc);
			
			title=document.createElement("title");
			title.appendChild(document.createTextNode(fileDoc.select("title").text().toString()));
			doc.appendChild(title);
			
			body=document.createElement("body");
			body.appendChild(document.createTextNode(fileDoc.body().text().toString()));
			doc.appendChild(body);
		}
		TransformerFactory tfFactory=TransformerFactory.newInstance();
		Transformer tf=tfFactory.newTransformer();
		tf.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
		
		DOMSource ds=new DOMSource(document);
		StreamResult result=new StreamResult(new FileOutputStream(new File("/Users/jodaehyeon/Desktop/SimpleIR/collection.xml")));
		
		tf.transform(ds, result);
	}

}
