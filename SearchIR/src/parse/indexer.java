package parse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class indexer {
	
	Double totalDocCount;
	HashMap<String, ArrayList<Pair>> tempMap;
	HashMap<String, String> weightMap;
	File xmlFile;
	Document doc;
	
	indexer(String path) throws IOException{
		xmlFile=new File(path);
		doc=Jsoup.parse(xmlFile,"UTF-8");
		totalDocCount=(double) doc.select("doc").size();
		tempMap=new HashMap<String,ArrayList<Pair>>();
	}
	public void makeTempMap() {
		for(int i=0;i<totalDocCount;i++) {
			String[] split1=doc.select("doc").textNodes().get(i).toString().split("#");
			
			for(int j=0;j<split1.length;j++) {
				ArrayList<Pair> arrp=new ArrayList<Pair>();
				String[] str=split1[j].split(":");
				Pair pair=new Pair(doc.select("doc").get(i).id(),Double.parseDouble(str[1]));
				arrp.add(pair);
				if(tempMap.containsKey(str[0])) {
					tempMap.get(str[0]).add(pair);
				}else {
					tempMap.put(str[0], arrp);
				}
			}
		}
	}
	
	public void setKeyWeight() {
		weightMap=new HashMap<String,String>();
		
		Double weight;
		for(Entry<String, ArrayList<Pair>> e : tempMap.entrySet()) {
			StringBuilder sb=new StringBuilder();
			for(int i=0;i<e.getValue().size();i++) {
				for(int j=0;j<totalDocCount;j++) {
					if(e.getValue().get(i).getDocid().equals(Integer.toString(j))) {
						weight=e.getValue().get(i).getTfrequency()*Math.log10(totalDocCount/e.getValue().size());
						sb.append(e.getValue().get(i).getDocid().toString()).append(" ").append(weight.toString());
					}else {
						weight=0.0;
						sb.append(Integer.toString(j)).append(" ").append(weight.toString());
					}
					if(j!=totalDocCount-1) sb.append(" ");
				}
				
			}
			weightMap.put(e.getKey(), sb.toString());
		}
	}
	
	public void saveToPostFile() throws IOException {
		FileOutputStream fileStream=new FileOutputStream("/Users/jodaehyeon/Desktop/SimpleIR/SearchIR/src/post/index.post");
		
		ObjectOutputStream objectOutputStream=new ObjectOutputStream(fileStream);
		
		objectOutputStream.writeObject(weightMap);
		objectOutputStream.close();
	}
	
	public void makePostFile() throws IOException, ClassNotFoundException {
		makeTempMap();
		setKeyWeight();
		saveToPostFile();
		
		FileInputStream fs=new FileInputStream("/Users/jodaehyeon/Desktop/SimpleIR/SearchIR/src/post/index.post");
		ObjectInputStream o=new ObjectInputStream(fs);
		Object object=o.readObject();
		o.close();
		
		HashMap h=(HashMap)object;
		Iterator<String> it = h.keySet().iterator();
		while(it.hasNext()) {
			String key=it.next();
			String value=weightMap.get(key);
			System.out.println("키 : "+key+" value : "+value);
		}
	}

}
