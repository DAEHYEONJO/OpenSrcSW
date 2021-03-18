package parse;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Element;

public class makeKeyword extends makeCollection{
	
	File xmlFile;
	
	makeKeyword(String path) throws ParserConfigurationException, TransformerConfigurationException {
		super();
		xmlFile=new File(path);
	}
	
	void xmlToXml() throws IOException, TransformerException {
		Document fd = Jsoup.parse(xmlFile, "UTF-8");
		Element docs=document2.createElement("docs");
		document2.appendChild(docs);
		
		for (int i = 0; i < fd.select("doc").textNodes().size(); i++) {
			Element doc=document2.createElement("doc");
			doc.setAttribute("id", Integer.toString(i));
			docs.appendChild(doc);
			
			Element title = document2.createElement("title");
			title.appendChild(document2.createTextNode(fd.select("title").textNodes().get(i).toString()));
			doc.appendChild(title);
			
			Element body = document2.createElement("body");
			body.appendChild(document2.createTextNode(getKkmaStr(fd.select("doc").textNodes().get(i).toString())));
			doc.appendChild(body);
			
			System.out.println(getKkmaStr(fd.select("doc").textNodes().get(i).toString()));
		}
		makeXMLFile(document2,originFilePath,indexXml);
	}
	
	String getKkmaStr(String str) {
		KeywordExtractor ke=new KeywordExtractor();
		KeywordList kl=ke.extractKeyword(str, true);
		StringBuilder s=new StringBuilder();
		for(int i=0;i<kl.size();i++) {
			s.append(kl.get(i).getString());
			s.append(":");
			s.append(kl.get(i).getCnt());
			if(i!=kl.size()-1)
				s.append("#");
		}
		return s.toString();
	}
}
