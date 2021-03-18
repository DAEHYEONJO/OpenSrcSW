package parse;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class kuir {

	static String dataPath="/Users/jodaehyeon/Desktop/SimpleIR/SearchIR/src/data";
	static String xmlFilePath="/Users/jodaehyeon/Desktop/SimpleIR/collection.xml";
	public static void main(String[] args) throws ParserConfigurationException, IOException, TransformerException {
		//옵션이-c인지 -k인지에 따라서 다른 것 실
		System.out.println(Integer.toString(args.length));
		if(args.length!=2) return;
		if(args[0].equals("-c")) {
			makeCollection mc=new makeCollection(args[1].toString());
			mc.htmlToXml();
		}else if(args[0].equals("-k")) {
			makeKeyword mk=new makeKeyword(args[1].toString());
			mk.xmlToXml();
		}
	}

}
