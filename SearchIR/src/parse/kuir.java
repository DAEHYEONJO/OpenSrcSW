package parse;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class kuir {

	public static void main(String[] args) throws ParserConfigurationException, IOException, TransformerException, ClassNotFoundException {
		System.out.println(Integer.toString(args.length));
		if(args.length==2) {
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
		else if(args.length==4) {
			//collection.xml과 index.post file을 얻기 위하여 명령행 인자로 각 file의 상위 디렉토리명까지 주었습니다.
			if(args[0].equals("-s")&&args[2].equals("-q")) {
				searcher sc=new searcher(args[1].toString(),args[3].toString());
				sc.startSearcher();
			}
		}
		//uptream test
	}

}
