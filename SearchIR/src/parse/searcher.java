package parse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;

public class searcher{
	
	String query;
	Scanner scan;
	HashMap<String,Double> sim;
	HashMap<String, Double> kkmKeyWeight;
	File xmlFile;
	File postFile;
	Document doc;
	Integer totalDocument;
	
	searcher(String path, String query) throws IOException{
		xmlFile=new File(path+"/xml/collection.xml");
		postFile=new File(path+"/post/index.post");
		doc=Jsoup.parse(xmlFile,"UTF-8");
		totalDocument=doc.select("doc").size();
		this.query=query;
		kkmKeyWeight=new HashMap<String,Double>();
		sim=new HashMap<String,Double>();
		for(int i=0;i<totalDocument;i++) sim.put(Integer.toString(i), 0.0);
		
	}
	void getKeywordWeight(String str) throws ClassNotFoundException, IOException  {
		KeywordExtractor ke=new KeywordExtractor();
		KeywordList kl=ke.extractKeyword(query, true);
		for(Keyword k : kl) {
			kkmKeyWeight.put(k.getString(), 1.0);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	HashMap<String,String> getIndexPost() throws IOException, ClassNotFoundException{
		FileInputStream fs=new FileInputStream(postFile);
		ObjectInputStream o=new ObjectInputStream(fs);
		Object object=o.readObject();
		o.close();
		
		HashMap<String,String> h=(HashMap<String, String>)object;	
		return h;
	}
	
	HashMap<String,Double> innerProduct() throws IOException, ClassNotFoundException{
		Iterator<String> kkmit=kkmKeyWeight.keySet().iterator();
		HashMap<String,String> ip=getIndexPost();
		
		while(kkmit.hasNext()) {
			//문자 1개에 대한 index post file의 value를
			//공백을 delimeter로하여 split 한 후 먼저 나오는것->docid,그담에 나오는것->가중치
			//sim 해쉬맵의 key->먼저나오는거, value+=value+(가중치*kkm해쉬맵의 value)
			String kkmKey=kkmit.next();
			String[] temp=null;
			if(ip.get(kkmKey)!=null) {
				temp=ip.get(kkmKey).split(" ");
				String docid=null;
				Double weight=0.0;
				for(int i=0;i<temp.length-1;i+=2) {
					docid=temp[i];
					weight=Double.parseDouble(temp[i+1]);
					Double oldSimValue=sim.get(docid);
					String curSimValueStr=String.format("%.2f", oldSimValue+(weight*kkmKeyWeight.get(kkmKey)));
					Double curSimValue=Double.parseDouble(curSimValueStr);
					sim.put(docid, curSimValue);
					//System.out.println("docid : "+docid+" weight : "+weight+" oldSimValue : "+oldSimValue);
				}
			}
		}
		
		
		return sim;
	}
	
	void startSearcher() throws ClassNotFoundException, IOException {
		getKeywordWeight(query);
		HashMap<String,Double> sim = innerProduct();
		List<String> simKeySetList = new ArrayList<>(sim.keySet());
		Collections.sort(simKeySetList, (o1, o2) -> (sim.get(o2).compareTo(sim.get(o1))));
		for(int i=0;i<3;i++) {
			//System.out.println("key : " + simKeySetList.get(i) + " / " + "value : " +sim.get(simKeySetList.get(i)));
			System.out.println(doc.select("doc").get(Integer.parseInt(simKeySetList.get(i))).select("title").text());
	    }
		
	}
}
