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
	
	HashMap<String,Double> calcSim() throws IOException, ClassNotFoundException{
		Iterator<String> kkmit=kkmKeyWeight.keySet().iterator();
		HashMap<String,String> ip=getIndexPost();
		Double wqi=0.0;
		HashMap<String,Double> wik=new HashMap<String,Double>();//w10,w20,...제곱 더한거 저장
		for(int i=0;i<totalDocument;i++) wik.put(Integer.toString(i), 0.0);
		while(kkmit.hasNext()) {
			String kkmKey=kkmit.next();
			wqi+=Math.pow(kkmKeyWeight.get(kkmKey),2);
			String[] temp=null;
			if(ip.get(kkmKey)!=null) {
				temp=ip.get(kkmKey).split(" ");
				String docid=null;
				String weight=null;
				for(int i=0;i<temp.length-1;i+=2) {
					docid=temp[i];
					weight=temp[i+1];
					Double oldValue=wik.get(docid);
					Double curValue=oldValue+Math.pow(Double.parseDouble(weight),2);
					wik.put(docid, curValue);
				}
			}
		}
		wqi=Math.sqrt(wqi);
		Iterator<String> wikit=wik.keySet().iterator();
		while(wikit.hasNext()) {
			String key=wikit.next();
			Double temp=wik.get(key);
			wik.put(key, 1.0/(wqi*temp));//1.0을 innerproduct의 return 값의 내적값으로 변경.
		}
		
		
		return wik;
	}
	
	void startSearcher() throws ClassNotFoundException, IOException {
		getKeywordWeight(query);
		HashMap<String,Double> sim = calcSim();
		List<String> simKeySetList = new ArrayList<>(sim.keySet());
		Collections.sort(simKeySetList, (o1, o2) -> (sim.get(o2).compareTo(sim.get(o1))));
		for(int i=0;i<3;i++) {
			//System.out.println("key : " + simKeySetList.get(i) + " / " + "value : " +sim.get(simKeySetList.get(i)));
			System.out.println(doc.select("doc").get(Integer.parseInt(simKeySetList.get(i))).select("title").text());
	    }
		
	}
}

