package parse;

public class Pair {
	private String docid;
	private Double tfrequency;
	Pair(String docid,Double tfrequency){
		this.docid=docid;
		this.tfrequency=tfrequency;
	}
	public String getDocid() {
		return docid;
	}
	public Double getTfrequency() {
		return tfrequency;
	}
	
}
