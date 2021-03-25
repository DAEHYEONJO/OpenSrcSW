package parse;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class kuir {

	public static void main(String[] args) throws ParserConfigurationException, IOException, TransformerException, ClassNotFoundException {
		System.out.println(Integer.toString(args.length));
		if(args.length!=2) return;
		if(args[0].equals("-c")) {
			makeCollection mc=new makeCollection(args[1].toString());
			mc.htmlToXml();
		}else if(args[0].equals("-k")) {
			makeKeyword mk=new makeKeyword(args[1].toString());
			mk.xmlToXml();
		}
		else if(args[0].equals("-i")) {
			indexer idx=new indexer(args[1].toString());
			idx.makePostFile();
		}
		
		
	}

}
